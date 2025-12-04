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
import java.util.Date;
import java.util.List;

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

    // AI Query Generator
    public String determineSearchQuery(String equipment, String exerciseName) {
        String baseQuery =
                (equipment != null && !equipment.equalsIgnoreCase("bodyweight") && !equipment.isBlank())
                        ? equipment
                        : exerciseName;

        try {
            String prompt = String.format(
                    "Generate a concise Amazon search keyword for exercise equipment related to '%s' (exercise: '%s'). Return only the keyword.",
                    equipment, exerciseName);

            String aiQuery = OpenAIKeywordGenerator.generateKeyword(prompt);

            if (aiQuery != null && !aiQuery.isEmpty()) {
                return aiQuery;
            }
        } catch (Exception e) {
            System.err.println("AI Query Error: " + e.getMessage());
        }

        return baseQuery;
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
