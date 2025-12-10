package service;

import dao.ProductCacheDAO;
import dto.NaverProductDTO;
import dto.ProductCacheDTO;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class NaverShoppingService {

    private static NaverShoppingService instance = new NaverShoppingService();
    private ProductCacheDAO productCacheDAO;

    // Environment variables
    private static final String CLIENT_ID = getEnvOrDefault("NAVER_CLIENT_ID", "");
    private static final String CLIENT_SECRET = getEnvOrDefault("NAVER_CLIENT_SECRET", "");
    private static final String API_URL = "https://openapi.naver.com/v1/search/shop.json";

    private static String getEnvOrDefault(String key, String defaultValue) {
        String value = System.getenv(key);
        return (value != null && !value.isEmpty()) ? value : defaultValue;
    }

    private NaverShoppingService() {
        productCacheDAO = ProductCacheDAO.getInstance();
    }

    public static NaverShoppingService getInstance() {
        return instance;
    }

    /**
     * Splits a combined query by 'or', fetches products for the first 2 terms,
     * and returns a combined list.
     * @param combinedQuery A string containing search terms separated by " or ".
     * @param limitPerQuery The number of products to fetch for each term.
     * @return A list of NaverProductDTO objects.
     */
    public List<NaverProductDTO> getProductsFromCombinedQuery(String combinedQuery, int limitPerQuery) {
        if (combinedQuery == null || combinedQuery.isBlank()) {
            return new ArrayList<>();
        }

        List<String> searchTerms = Arrays.stream(combinedQuery.split(" or "))
                .map(String::trim)
                .filter(term -> !term.isEmpty())
                .collect(Collectors.toList());

        List<NaverProductDTO> allProducts = new ArrayList<>();
        int termsToProcess = Math.min(searchTerms.size(), 5); // Process at most 5 terms (for store: 5 queries × 10 products = 50)

        for (int i = 0; i < termsToProcess; i++) {
            String term = searchTerms.get(i);
            List<NaverProductDTO> productsForTerm = getProducts(term, limitPerQuery);
            allProducts.addAll(productsForTerm);
        }

        return allProducts;
    }

    /**
     * Get products (with caching)
     */
    public List<NaverProductDTO> getProducts(String query, int limit) {
        try {
            ProductCacheDTO cache = productCacheDAO.findBySearchQuery(query);

            if (cache != null && !isCacheExpired(cache, 24)) {
                System.out.println("Returning cached products for query: " + query);
                return cache.getProducts();
            }

            System.out.println("Fetching products from Naver API for query: " + query);
            List<NaverProductDTO> products = fetchFromAPI(query, limit);

            if (!products.isEmpty()) {
                saveToCache(query, products);
            }

            return products;
        } catch (Exception e) {
            System.err.println("Error getting products: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Fetch products from Naver Shopping API (HttpURLConnection 방식)
     */
    private List<NaverProductDTO> fetchFromAPI(String query, int limit) {
        List<NaverProductDTO> products = new ArrayList<>();

        try {
            if (CLIENT_ID == null || CLIENT_ID.isEmpty()) {
                System.err.println("ERROR: NAVER_CLIENT_ID is not set!");
                return products;
            }

            if (CLIENT_SECRET == null || CLIENT_SECRET.isEmpty()) {
                System.err.println("ERROR: NAVER_CLIENT_SECRET is not set!");
                return products;
            }

            String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
            String apiURL = API_URL + "?query=" + encodedQuery + "&display=" + limit;

            Map<String, String> requestHeaders = new HashMap<>();
            requestHeaders.put("X-Naver-Client-Id", CLIENT_ID);
            requestHeaders.put("X-Naver-Client-Secret", CLIENT_SECRET);

            String responseBody = get(apiURL, requestHeaders);

            // JSON 파싱
            JSONObject jsonResponse = new JSONObject(responseBody);
            JSONArray items = jsonResponse.optJSONArray("items");

            if (items != null) {
                for (int i = 0; i < items.length(); i++) {
                    JSONObject item = items.getJSONObject(i);
                    NaverProductDTO product = jsonToDTO(item);
                    products.add(product);
                }
            }

        } catch (Exception e) {
            System.err.println("Naver API 호출 실패: " + e.getMessage());
            e.printStackTrace();
        }

        return products;
    }

    /**
     * HTTP GET 요청 (Naver 예제 방식)
     */
    private String get(String apiUrl, Map<String, String> requestHeaders) {
        HttpURLConnection con = connect(apiUrl);
        try {
            con.setRequestMethod("GET");
            for(Map.Entry<String, String> header : requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                return readBody(con.getInputStream());
            } else {
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }

    /**
     * URL 연결
     */
    private HttpURLConnection connect(String apiUrl) {
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection)url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다: " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다: " + apiUrl, e);
        }
    }

    /**
     * Response Body 읽기
     */
    private String readBody(InputStream body) {
        InputStreamReader streamReader = new InputStreamReader(body);

        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();

            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }

            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는 데 실패했습니다.", e);
        }
    }

    /**
     * JSON → DTO 변환
     */
    private NaverProductDTO jsonToDTO(JSONObject json) {
        NaverProductDTO product = new NaverProductDTO();

        // HTML 태그 제거
        String title = json.optString("title", "");
        title = title.replaceAll("<[^>]*>", "");
        product.setTitle(title);

        product.setLink(json.optString("link", ""));
        product.setImage(json.optString("image", ""));
        product.setMallName(json.optString("mallName", ""));
        product.setProductId(json.optString("productId", ""));
        product.setBrand(json.optString("brand", ""));
        product.setMaker(json.optString("maker", ""));

        // 가격 파싱
        String lpriceStr = json.optString("lprice", "0");
        String hpriceStr = json.optString("hprice", "0");
        try {
            product.setLprice(Integer.parseInt(lpriceStr));
        } catch (NumberFormatException e) {
            product.setLprice(0);
        }
        try {
            product.setHprice(Integer.parseInt(hpriceStr));
        } catch (NumberFormatException e) {
            product.setHprice(0);
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

 String prompt ="Generate a precise Naver Shopping search keyword for exercise equipment. "

+ "Return the product search term, no explanations. "

+ "Exercise name: " + exerciseName

+ ", Equipment: " + equipment

+ ". Focus on the specific equipment needed for this exercise. "

    + "search keword to same language return exercise equipment "

+ "네이버 쇼핑 검색에 최적화된 키워드를 생성하세요.";

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

            System.out.println("AI Generated Query for Naver Shopping: " + aiQuery);
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
            return equipment + " " + exerciseName;
        }
        return exerciseName + " 운동 기구";
    }

    /**
     * 검색어 결정 → AI 기반 버전
     */
    public String determineSearchQuery(String equipment, String exerciseName) {
        return generateAIQuery(exerciseName, equipment);
    }

    /**
     * Cache expired?
     */
    private boolean isCacheExpired(ProductCacheDTO cache, int hours) {
        long diff = System.currentTimeMillis() - cache.getLastUpdated().getTime();
        long diffHours = diff / (1000 * 60 * 60);
        return diffHours >= hours;
    }

    /**
     * Save to DB cache
     */
    private void saveToCache(String query, List<NaverProductDTO> products) {
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
