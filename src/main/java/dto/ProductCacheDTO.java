package dto;

import java.util.Date;
import java.util.List;

public class ProductCacheDTO {
    private String id;
    private String searchQuery;
    private List<AmazonProductDTO> products;
    private Date lastUpdated;
    private Date createdAt;

    public ProductCacheDTO() {
    }

    public ProductCacheDTO(String id, String searchQuery, List<AmazonProductDTO> products,
                           Date lastUpdated, Date createdAt) {
        this.id = id;
        this.searchQuery = searchQuery;
        this.products = products;
        this.lastUpdated = lastUpdated;
        this.createdAt = createdAt;
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

    public List<AmazonProductDTO> getProducts() {
        return products;
    }

    public void setProducts(List<AmazonProductDTO> products) {
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

    @Override
    public String toString() {
        return "ProductCacheDTO{" +
                "id='" + id + '\'' +
                ", searchQuery='" + searchQuery + '\'' +
                ", productsCount=" + (products != null ? products.size() : 0) +
                ", lastUpdated=" + lastUpdated +
                ", createdAt=" + createdAt +
                '}';
    }
}
