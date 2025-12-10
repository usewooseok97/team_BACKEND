package dto;

import java.util.Date;
import java.util.List;

public class ProductCacheDTO {
    private String id;
    private String searchQuery;
    private List<NaverProductDTO> products;
    private Date lastUpdated;
    private Date createdAt;
    private String category;           // "upper_body", "lower_body", "cardio", "home_workout"
    private boolean storeCategory;     // true for store products, false for exercise products

    public ProductCacheDTO() {
    }

    public ProductCacheDTO(String id, String searchQuery, List<NaverProductDTO> products,
                           Date lastUpdated, Date createdAt) {
        this.id = id;
        this.searchQuery = searchQuery;
        this.products = products;
        this.lastUpdated = lastUpdated;
        this.createdAt = createdAt;
    }

    public ProductCacheDTO(String id, String searchQuery, List<NaverProductDTO> products,
                           Date lastUpdated, Date createdAt, String category, boolean storeCategory) {
        this.id = id;
        this.searchQuery = searchQuery;
        this.products = products;
        this.lastUpdated = lastUpdated;
        this.createdAt = createdAt;
        this.category = category;
        this.storeCategory = storeCategory;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public List<NaverProductDTO> getProducts() {
        return products;
    }

    public void setProducts(List<NaverProductDTO> products) {
        this.products = products;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isStoreCategory() {
        return storeCategory;
    }

    public void setStoreCategory(boolean storeCategory) {
        this.storeCategory = storeCategory;
    }

    @Override
    public String toString() {
        return "ProductCacheDTO{" +
                "id='" + id + '\'' +
                ", searchQuery='" + searchQuery + '\'' +
                ", productsCount=" + (products != null ? products.size() : 0) +
                ", lastUpdated=" + lastUpdated +
                ", createdAt=" + createdAt +
                ", category='" + category + '\'' +
                ", storeCategory=" + storeCategory +
                '}';
    }
}
