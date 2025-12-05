package service;

import dao.ProductCacheDAO;
import dto.AmazonProductDTO;
import dto.ProductCacheDTO;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class AmazonProductService {

    private static AmazonProductService instance = new AmazonProductService();
    private ProductCacheDAO productCacheDAO;

    // Environment variables
    private static final String API_KEY = getEnvOrDefault("RAPIDAPI_KEY", "");
    private static final String API_HOST = getEnvOrDefault("AMAZON_RAPIDAPI_HOST", "");
    private static final String BASE_URL = getBaseUrl();

    private static String getBaseUrl() {
        String base = System.getenv("AMAZON_API_BASE_URL");
        if (base == null || base.isEmpty()) return "";
        if (!base.endsWith("/search")) {
            base = base + "/search";  
        }
        return base;
    }

    private static String getEnvOrDefault(String key, String defaultValue) {
        String value = System.getenv(key);
        return (value != null && !value.isEmpty()) ? value : defaultValue;
    }

    private AmazonProductService() {
        productCacheDAO = ProductCacheDAO.getInstance();
    }

    public static AmazonProductService getInstance() {
        return instance;
    }

    /**
     * Splits a combined query by 'or', fetches products for the first 2 terms,
     * and returns a combined list.
     * @param combinedQuery A string containing search terms separated by " or ".
     * @param limitPerQuery The number of products to fetch for each term.
     * @return A list of AmazonProductDTO objects.
     */
    public List<AmazonProductDTO> getProductsFromCombinedQuery(String combinedQuery, int limitPerQuery) {
        if (combinedQuery == null || combinedQuery.isBlank()) {
            return new ArrayList<>();
        }

        List<String> searchTerms = Arrays.stream(combinedQuery.split(" or "))
                .map(String::trim)
                .filter(term -> !term.isEmpty())
                .collect(Collectors.toList());

        List<AmazonProductDTO> allProducts = new ArrayList<>();
        int termsToProcess = Math.min(searchTerms.size(), 2); // Process at most 2 terms

        for (int i = 0; i < termsToProcess; i++) {
            String term = searchTerms.get(i);
            List<AmazonProductDTO> productsForTerm = getProducts(term, limitPerQuery);
            allProducts.addAll(productsForTerm);
        }

        return allProducts;
    }

    // Get products (with caching)
    public List<AmazonProductDTO> getProducts(String query, int limit) {
        try {
            ProductCacheDTO cache = productCacheDAO.findBySearchQuery(query);

            if (cache != null && !isCacheExpired(cache, 24)) {
                System.out.println("Returning cached products for query: " + query);
                return cache.getProducts();
            }

            System.out.println("Fetching products from Amazon API for query: " + query);
            List<AmazonProductDTO> products = fetchFromAPI(query, limit);

            if (!products.isEmpty()) {
                saveToCache(query, products);
            }

            return products;
        } catch (Exception e) {
            System.err.println("Error getting products: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Fetch products from RapidAPI
    private List<AmazonProductDTO> fetchFromAPI(String query, int limit) {
        List<AmazonProductDTO> products = new ArrayList<>();
        try {
            if (API_KEY == null || API_KEY.isEmpty()) {
                System.err.println("ERROR: RAPIDAPI_KEY is not set!");
                return products;
            }

            String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
            String requestUrl = BASE_URL + "?q=" + encodedQuery + "&country=us";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(requestUrl))
                    .header("x-rapidapi-key", API_KEY)
                    .header("x-rapidapi-host", API_HOST)
                    .GET()
                    .build();

            HttpResponse<String> response =
                    HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            JSONObject jsonResponse = new JSONObject(response.body());
            JSONArray resultArray = jsonResponse.optJSONArray("results");

            if (resultArray != null) {
                for (int i = 0; i < Math.min(resultArray.length(), limit); i++) {
                    JSONObject productJson = resultArray.getJSONObject(i);
                    AmazonProductDTO product = jsonToDTO(productJson);
                    products.add(product);
                }
            }

        } catch (Exception e) {
            System.err.println("Error fetching API products: " + e.getMessage());
        }

        return products;
    }

    // Convert JSON → DTO
    private AmazonProductDTO jsonToDTO(JSONObject json) {
        AmazonProductDTO product = new AmazonProductDTO();

        product.setPosition(json.optInt("position", 0));
        product.setAsin(json.optString("asin", ""));
        product.setName(json.optString("name", ""));
        product.setImage(json.optString("image", ""));
        product.setHasPrime(json.optBoolean("has_prime", false));
        product.setIsBestSeller(json.optBoolean("is_best_seller", false));
        product.setStars(json.optDouble("stars", 0.0));
        product.setUrl(json.optString("url", ""));

        JSONObject spec = json.optJSONObject("spec");
        if (spec != null) {
            product.setPriceString(spec.optString("price_string", ""));
            product.setPrice(spec.optDouble("price", 0.0));
        }

        return product;
    }

    /**
     * ====================================================
     *  AI 기반 검색어 생성 (Groq LLaMA-3.3 70B 사용)
     * ====================================================
     */
    public String generateAIQuery(String exerciseName, String equipment) {
        try {
            String groqApiKey = System.getenv("GROQ_API_KEY");

            if (groqApiKey == null || groqApiKey.isEmpty()) {
                System.err.println("Groq API Key Missing — Using fallback query");
                return generateFallbackQuery(equipment, exerciseName);
            }

            String prompt =
                    "Generate a precise Amazon search keyword for exercise equipment. "
                            + "Return ONLY the product search term, no explanations. "
                            + "Exercise name: " + exerciseName
                            + ", Equipment: " + equipment
                            + ". Focus on the specific equipment needed for this exercise.";

            JSONObject body = new JSONObject();
            body.put("model", "llama-3.3-70b-versatile");

            JSONArray messages = new JSONArray()
                    .put(new JSONObject()
                            .put("role", "user")
                            .put("content", prompt));

            body.put("messages", messages);
            body.put("temperature", 0.2);
            body.put("max_tokens", 25);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.groq.com/openai/v1/chat/completions"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + groqApiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
                    .build();

            HttpResponse<String> response =
                    HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            JSONObject json = new JSONObject(response.body());

            String aiQuery = json.getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content")
                    .trim();

            System.out.println("AI Generated Query for Amazon: " + aiQuery);
            return aiQuery;

        } catch (Exception e) {
            System.out.println("AI Query Generation Failed. Using fallback.");
            e.printStackTrace();
            return generateFallbackQuery(equipment, exerciseName);
        }
    }


    /**
     * Fallback 쿼리 생성
     */
    private String generateFallbackQuery(String equipment, String exerciseName) {
        if (equipment != null && !equipment.equalsIgnoreCase("bodyweight") && !equipment.isBlank()) {
            return equipment + " for " + exerciseName;
        }
        return exerciseName + " exercise equipment";
    }

    /**
     * 검색어 결정 → AI 기반 버전
     */
    public String determineSearchQuery(String equipment, String exerciseName) {
        return generateAIQuery(exerciseName, equipment);
    }

    // Cache expired?
    private boolean isCacheExpired(ProductCacheDTO cache, int hours) {
        long diff = System.currentTimeMillis() - cache.getLastUpdated().getTime();
        long diffHours = diff / (1000 * 60 * 60);
        return diffHours >= hours;
    }

    // Save to DB cache
    private void saveToCache(String query, List<AmazonProductDTO> products) {
        try {
            Date now = new Date();
            ProductCacheDTO cache = new ProductCacheDTO();

            cache.setSearchQuery(query);
            cache.setProducts(products);
            cache.setLastUpdated(now);

            ProductCacheDTO old = productCacheDAO.findBySearchQuery(query);
            cache.setCreatedAt(old != null ? old.getCreatedAt() : now);

            productCacheDAO.insertOrUpdate(cache);
        } catch (Exception e) {
            System.err.println("Cache save error: " + e.getMessage());
        }
    }
}
