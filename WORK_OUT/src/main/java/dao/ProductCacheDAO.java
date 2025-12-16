package dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import dto.NaverProductDTO;
import dto.ProductCacheDTO;
import mongoutil.MongoConn;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProductCacheDAO {
    private static ProductCacheDAO instance = new ProductCacheDAO();
    private MongoCollection<Document> collection;

    private ProductCacheDAO() {
        try {
            MongoDatabase database = MongoConn.getDatabase();
            collection = database.getCollection("products");
            System.out.println("ProductCacheDAO initialized successfully");
        } catch (Exception e) {
            System.err.println("ProductCacheDAO initialization failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static ProductCacheDAO getInstance() {
        return instance;
    }

    /**
     * Find cached products by search query
     */
    public ProductCacheDTO findBySearchQuery(String searchQuery) {
        try {
            Document doc = collection.find(Filters.eq("searchQuery", searchQuery)).first();
            return documentToCache(doc);
        } catch (Exception e) {
            System.err.println("Error finding product cache by search query: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Find cached products by category (for store products)
     */
    public ProductCacheDTO findByCategory(String category) {
        try {
            Document doc = collection.find(
                Filters.and(
                    Filters.eq("category", category),
                    Filters.eq("storeCategory", true)
                )
            ).first();
            return documentToCache(doc);
        } catch (Exception e) {
            System.err.println("Error finding product cache by category: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Insert or update product cache (upsert)
     */
    public boolean insertOrUpdate(ProductCacheDTO cache) {
        try {
            Document filter = new Document("searchQuery", cache.getSearchQuery());
            Document update = new Document("$set", cacheToDocument(cache));
            UpdateOptions options = new UpdateOptions().upsert(true);

            collection.updateOne(filter, update, options);
            System.out.println("Product cache updated for query: " + cache.getSearchQuery());
            return true;
        } catch (Exception e) {
            System.err.println("Error inserting/updating product cache: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Delete product cache by search query
     */
    public boolean deleteBySearchQuery(String searchQuery) {
        try {
            collection.deleteOne(Filters.eq("searchQuery", searchQuery));
            System.out.println("Product cache deleted for query: " + searchQuery);
            return true;
        } catch (Exception e) {
            System.err.println("Error deleting product cache: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


    /**
     * Convert ProductCacheDTO to MongoDB Document
     */
    private Document cacheToDocument(ProductCacheDTO cache) {
        Document doc = new Document()
                .append("searchQuery", cache.getSearchQuery())
                .append("lastUpdated", cache.getLastUpdated())
                .append("createdAt", cache.getCreatedAt())
                .append("category", cache.getCategory())
                .append("storeCategory", cache.isStoreCategory());

        // Convert products list to documents list
        List<Document> productDocs = new ArrayList<>();
        if (cache.getProducts() != null) {
            for (NaverProductDTO product : cache.getProducts()) {
                Document productDoc = new Document()
                        .append("title", product.getTitle())
                        .append("link", product.getLink())
                        .append("image", product.getImage())
                        .append("lprice", product.getLprice())
                        .append("hprice", product.getHprice())
                        .append("mallName", product.getMallName())
                        .append("productId", product.getProductId())
                        .append("brand", product.getBrand())
                        .append("maker", product.getMaker());
                productDocs.add(productDoc);
            }
        }
        doc.append("products", productDocs);

        return doc;
    }

    /**
     * Convert MongoDB Document to ProductCacheDTO
     */
    private ProductCacheDTO documentToCache(Document doc) {
        if (doc == null) {
            return null;
        }

        ProductCacheDTO cache = new ProductCacheDTO();
        cache.setId(doc.getObjectId("_id") != null ? doc.getObjectId("_id").toString() : null);
        cache.setSearchQuery(doc.getString("searchQuery"));
        cache.setLastUpdated(doc.getDate("lastUpdated"));
        cache.setCreatedAt(doc.getDate("createdAt"));
        cache.setCategory(doc.getString("category"));
        cache.setStoreCategory(doc.getBoolean("storeCategory", false));

        // Convert products documents to NaverProductDTO list
        List<NaverProductDTO> products = new ArrayList<>();
        Object productsObj = doc.get("products");

        if (productsObj instanceof List) {
            List<?> productDocsRaw = (List<?>) productsObj;
            for (Object productObj : productDocsRaw) {
                if (productObj instanceof Document) {
                    Document productDoc = (Document) productObj;
                    NaverProductDTO product = new NaverProductDTO();
                    product.setTitle(productDoc.getString("title"));
                    product.setLink(productDoc.getString("link"));
                    product.setImage(productDoc.getString("image"));
                    product.setLprice(productDoc.getInteger("lprice", 0));
                    product.setHprice(productDoc.getInteger("hprice", 0));
                    product.setMallName(productDoc.getString("mallName"));
                    product.setProductId(productDoc.getString("productId"));
                    product.setBrand(productDoc.getString("brand"));
                    product.setMaker(productDoc.getString("maker"));
                    products.add(product);
                }
            }
        }
        cache.setProducts(products);

        return cache;
    }
}
