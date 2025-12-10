package service;

import dao.ProductCacheDAO;
import data.StoreCategoryData;
import dto.NaverProductDTO;
import dto.ProductCacheDTO;
import dto.StoreCategory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for managing store product categories
 * Handles lazy loading, caching, and 24-hour auto-refresh
 */
public class StoreProductService {

    private static StoreProductService instance = new StoreProductService();
    private ProductCacheDAO productCacheDAO;
    private NaverShoppingService naverShoppingService;

    private static final int CACHE_HOURS = 24;
    private static final int PRODUCTS_PER_QUERY = 10; // 5 queries × 10 products = 50 total

    private StoreProductService() {
        productCacheDAO = ProductCacheDAO.getInstance();
        naverShoppingService = NaverShoppingService.getInstance();
    }

    public static StoreProductService getInstance() {
        return instance;
    }

    /**
     * Get products for a category (with lazy loading and caching)
     * @param categoryId The category ID ("upper_body", "lower_body", etc.)
     * @return List of products (max 50, deduplicated)
     */
    public List<NaverProductDTO> getCategoryProducts(String categoryId) {
        try {
            // Validate category
            StoreCategory category = StoreCategoryData.getCategoryById(categoryId);
            if (category == null) {
                System.err.println("Invalid category ID: " + categoryId);
                return new ArrayList<>();
            }

            // Check cache
            ProductCacheDTO cache = productCacheDAO.findByCategory(categoryId);

            // If cache exists and is fresh, return it
            if (cache != null && !needsRefresh(cache)) {
                System.out.println("Returning cached products for category: " + categoryId);
                return cache.getProducts();
            }

            // If cache is stale or missing, initialize/refresh
            System.out.println("Cache stale or missing for category: " + categoryId + ". Initializing...");
            return initializeCategoryProducts(categoryId);

        } catch (Exception e) {
            System.err.println("Error getting category products: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Initialize products for a category (fetch from API and store)
     * @param categoryId The category ID
     * @return List of products
     */
    public List<NaverProductDTO> initializeCategoryProducts(String categoryId) {
        try {
            StoreCategory category = StoreCategoryData.getCategoryById(categoryId);
            if (category == null) {
                System.err.println("Invalid category ID: " + categoryId);
                return new ArrayList<>();
            }

            System.out.println("Fetching products for category: " + categoryId);
            System.out.println("Search query: " + category.getSearchQuery());

            // Fetch products using combined query (5 terms × 10 products)
            List<NaverProductDTO> products = naverShoppingService.getProductsFromCombinedQuery(
                category.getSearchQuery(),
                PRODUCTS_PER_QUERY
            );

            // Deduplicate by productId
            products = deduplicateProducts(products);

            System.out.println("Fetched " + products.size() + " products for category: " + categoryId);

            // Save to cache
            saveToCache(categoryId, products);

            return products;

        } catch (Exception e) {
            System.err.println("Error initializing category products: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Force refresh products for a category
     * @param categoryId The category ID
     * @return List of refreshed products
     */
    public List<NaverProductDTO> refreshCategoryProducts(String categoryId) {
        try {
            System.out.println("Force refreshing products for category: " + categoryId);

            // Delete old cache first
            ProductCacheDTO oldCache = productCacheDAO.findByCategory(categoryId);
            if (oldCache != null) {
                productCacheDAO.deleteBySearchQuery(oldCache.getSearchQuery());
            }

            // Fetch new products
            return initializeCategoryProducts(categoryId);

        } catch (Exception e) {
            System.err.println("Error refreshing category products: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Initialize all categories (useful for admin/maintenance)
     */
    public void initializeAllCategories() {
        List<StoreCategory> categories = StoreCategoryData.getAllCategories();
        System.out.println("Initializing all " + categories.size() + " categories...");

        for (StoreCategory category : categories) {
            try {
                initializeCategoryProducts(category.getId());
                // Small delay to avoid rate limiting
                Thread.sleep(1000);
            } catch (Exception e) {
                System.err.println("Error initializing category " + category.getId() + ": " + e.getMessage());
            }
        }

        System.out.println("All categories initialized.");
    }

    /**
     * Check if cache needs refresh (older than 24 hours)
     */
    private boolean needsRefresh(ProductCacheDTO cache) {
        if (cache == null || cache.getLastUpdated() == null) {
            return true;
        }

        long diff = System.currentTimeMillis() - cache.getLastUpdated().getTime();
        long diffHours = diff / (1000 * 60 * 60);
        return diffHours >= CACHE_HOURS;
    }

    /**
     * Remove duplicate products by productId
     */
    private List<NaverProductDTO> deduplicateProducts(List<NaverProductDTO> products) {
        Map<String, NaverProductDTO> uniqueProducts = new LinkedHashMap<>();

        for (NaverProductDTO product : products) {
            String id = product.getProductId();
            if (id != null && !id.isEmpty() && !uniqueProducts.containsKey(id)) {
                uniqueProducts.put(id, product);
            }
        }

        return new ArrayList<>(uniqueProducts.values());
    }

    /**
     * Save products to cache
     */
    private void saveToCache(String categoryId, List<NaverProductDTO> products) {
        try {
            Date now = new Date();
            ProductCacheDTO cache = new ProductCacheDTO();

            // Use special search query format for store products
            cache.setSearchQuery("STORE_" + categoryId.toUpperCase());
            cache.setProducts(products);
            cache.setLastUpdated(now);
            cache.setCreatedAt(now);
            cache.setCategory(categoryId);
            cache.setStoreCategory(true);

            productCacheDAO.insertOrUpdate(cache);
            System.out.println("Cached " + products.size() + " products for category: " + categoryId);

        } catch (Exception e) {
            System.err.println("Error saving to cache: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Get all categories with product counts
     */
    public Map<String, Integer> getCategoryCounts() {
        Map<String, Integer> counts = new LinkedHashMap<>();
        List<StoreCategory> categories = StoreCategoryData.getAllCategories();

        for (StoreCategory category : categories) {
            ProductCacheDTO cache = productCacheDAO.findByCategory(category.getId());
            int count = (cache != null && cache.getProducts() != null) ? cache.getProducts().size() : 0;
            counts.put(category.getId(), count);
        }

        return counts;
    }
}
