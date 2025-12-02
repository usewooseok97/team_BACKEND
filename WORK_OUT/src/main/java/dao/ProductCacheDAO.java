package dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import dto.AmazonProductDTO;
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
     * Delete old caches (older than specified hours)
     */
    public boolean deleteOldCache(int hours) {
        try {
            long cutoffTime = System.currentTimeMillis() - (hours * 60 * 60 * 1000L);
            Date cutoffDate = new Date(cutoffTime);

            collection.deleteMany(Filters.lt("lastUpdated", cutoffDate));
            System.out.println("Old product caches deleted (older than " + hours + " hours)");
            return true;
        } catch (Exception e) {
            System.err.println("Error deleting old product caches: " + e.getMessage());
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
                .append("createdAt", cache.getCreatedAt());

        // Convert products list to documents list
        List<Document> productDocs = new ArrayList<>();
        if (cache.getProducts() != null) {
            for (AmazonProductDTO product : cache.getProducts()) {
                Document productDoc = new Document()
                        .append("position", product.getPosition())
                        .append("asin", product.getAsin())
                        .append("name", product.getName())
                        .append("image", product.getImage())
                        .append("hasPrime", product.isHasPrime())
                        .append("isBestSeller", product.isIsBestSeller())
                        .append("stars", product.getStars())
                        .append("url", product.getUrl())
                        .append("priceString", product.getPriceString())
                        .append("price", product.getPrice());
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

        // Convert products documents to AmazonProductDTO list
        List<AmazonProductDTO> products = new ArrayList<>();
        List<Document> productDocs = (List<Document>) doc.get("products");
        if (productDocs != null) {
            for (Document productDoc : productDocs) {
                AmazonProductDTO product = new AmazonProductDTO();
                product.setPosition(productDoc.getInteger("position", 0));
                product.setAsin(productDoc.getString("asin"));
                product.setName(productDoc.getString("name"));
                product.setImage(productDoc.getString("image"));
                product.setHasPrime(productDoc.getBoolean("hasPrime", false));
                product.setIsBestSeller(productDoc.getBoolean("isBestSeller", false));
                product.setStars(productDoc.getDouble("stars") != null ? productDoc.getDouble("stars") : 0.0);
                product.setUrl(productDoc.getString("url"));
                product.setPriceString(productDoc.getString("priceString"));
                product.setPrice(productDoc.getDouble("price") != null ? productDoc.getDouble("price") : 0.0);
                products.add(product);
            }
        }
        cache.setProducts(products);

        return cache;
    }
}
