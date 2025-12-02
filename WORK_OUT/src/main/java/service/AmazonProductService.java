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
    private static final String BASE_URL = getEnvOrDefault("AMAZON_API_BASE_URL", "");

    /**
     * Get environment variable with fallback to default
     */
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
     * Get products with caching
     * - First check cache (if < 24h old, return cached)
     * - Otherwise fetch from API and cache result
     */
    public List<AmazonProductDTO> getProducts(String query, int limit) {
        try {
            // 1. Check cache
            ProductCacheDTO cache = productCacheDAO.findBySearchQuery(query);

            // 2. If cache exists and not expired (< 24 hours), return cached products
            if (cache != null && !isCacheExpired(cache, 24)) {
                System.out.println("Returning cached products for query: " + query);
                return cache.getProducts();
            }

            // 3. Cache expired or doesn't exist - fetch from API
            System.out.println("Fetching fresh products from API for query: " + query);
            List<AmazonProductDTO> products = fetchFromAPI(query, limit);

            // 4. Save to cache
            if (!products.isEmpty()) {
                saveToCache(query, products);
            }

            // 5. Return products
            return products;
        } catch (Exception e) {
            System.err.println("Error getting products: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Fetch products from Amazon API
     */
    private List<AmazonProductDTO> fetchFromAPI(String query, int limit) {
        List<AmazonProductDTO> products = new ArrayList<>();
        try {
            // Check environment variables
            System.out.println("=== Environment Check ===");
            System.out.println("API_KEY exists: " + (API_KEY != null && !API_KEY.isEmpty()));
            System.out.println("API_HOST: " + API_HOST);
            System.out.println("BASE_URL: " + BASE_URL);
            System.out.println("========================");

            if (API_KEY == null || API_KEY.isEmpty()) {
                System.err.println("ERROR: RAPIDAPI_KEY environment variable is not set!");
                return products;
            }

            // URL encode the query
            String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);

            // Build request URL
            String requestUrl = BASE_URL + "?q=" + encodedQuery + "&country=us";
            System.out.println("Request URL: " + requestUrl);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(requestUrl))
                    .header("x-rapidapi-key", API_KEY)
                    .header("x-rapidapi-host", API_HOST)
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            // Log the raw response for debugging
            System.out.println("=== API Response Debug ===");
            System.out.println("Status Code: " + response.statusCode());
            System.out.println("Response Body: " + response.body());
            System.out.println("========================");

            // Parse JSON response
            JSONObject jsonResponse = new JSONObject(response.body());

            // Debug: print all keys in the response
            System.out.println("JSON Keys: " + jsonResponse.keySet());

            JSONArray resultArray = jsonResponse.optJSONArray("results");

            if (resultArray != null) {
                System.out.println("Found 'results' array with " + resultArray.length() + " items");
                int count = Math.min(resultArray.length(), limit);
                for (int i = 0; i < count; i++) {
                    JSONObject productJson = resultArray.getJSONObject(i);
                    AmazonProductDTO product = jsonToDTO(productJson);
                    products.add(product);
                }
            } else {
                System.out.println("WARNING: 'result' array is null or not found in response");
                // Try alternative keys
                if (jsonResponse.has("data")) {
                    System.out.println("Trying 'data' key...");
                    JSONArray dataArray = jsonResponse.optJSONArray("data");
                    if (dataArray != null) {
                        int count = Math.min(dataArray.length(), limit);
                        for (int i = 0; i < count; i++) {
                            JSONObject productJson = dataArray.getJSONObject(i);
                            AmazonProductDTO product = jsonToDTO(productJson);
                            products.add(product);
                        }
                    }
                }
            }

            System.out.println("Fetched " + products.size() + " products from API for query: " + query);
        } catch (Exception e) {
            System.err.println("Error fetching products from API: " + e.getMessage());
            e.printStackTrace();
        }
        return products;
    }

    /**
     * Determine search query from exercise data
     * Priority: equipment (if not "body weight") -> exercise name
     */
    public String determineSearchQuery(String equipment, String exerciseName) {
        // If equipment is meaningful (not "body weight" or empty), use it
        if (equipment != null && !equipment.trim().isEmpty()
                && !equipment.equalsIgnoreCase("body weight")
                && !equipment.equalsIgnoreCase("bodyweight")) {
            return equipment;
        }

        // Otherwise use exercise name
        return exerciseName != null ? exerciseName : "";
    }

    /**
     * Check if cache is expired
     */
    private boolean isCacheExpired(ProductCacheDTO cache, int hours) {
        if (cache == null || cache.getLastUpdated() == null) {
            return true;
        }
        long diffInMillis = System.currentTimeMillis() - cache.getLastUpdated().getTime();
        long diffInHours = diffInMillis / (1000 * 60 * 60);
        return diffInHours >= hours;
    }

    /**
     * Save products to cache
     */
    private void saveToCache(String query, List<AmazonProductDTO> products) {
        try {
            Date now = new Date();
            ProductCacheDTO cache = new ProductCacheDTO();
            cache.setSearchQuery(query);
            cache.setProducts(products);
            cache.setLastUpdated(now);

            // Check if this is a new cache entry
            ProductCacheDTO existing = productCacheDAO.findBySearchQuery(query);
            if (existing == null) {
                cache.setCreatedAt(now);
            } else {
                cache.setCreatedAt(existing.getCreatedAt());
            }

            productCacheDAO.insertOrUpdate(cache);
            System.out.println("Saved " + products.size() + " products to cache for query: " + query);
        } catch (Exception e) {
            System.err.println("Error saving products to cache: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Convert JSON object to AmazonProductDTO
     */
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

        // Parse spec object for price information
        JSONObject spec = json.optJSONObject("spec");
        if (spec != null) {
            product.setPriceString(spec.optString("price_string", ""));
            product.setPrice(spec.optDouble("price", 0.0));
        }

        return product;
    }
}
