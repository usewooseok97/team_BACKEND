package dto;

/**
 * 제품 정보를 표현하는 DTO 클래스
 */
public class ProductDTO {
    private String brand;
    private String name;
    private int originalPrice;
    private int price;
    private String image;

    public ProductDTO() {
    }

    public ProductDTO(String brand, String name, int originalPrice, int price, String image) {
        this.brand = brand;
        this.name = name;
        this.originalPrice = originalPrice;
        this.price = price;
        this.image = image;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(int originalPrice) {
        this.originalPrice = originalPrice;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    /**
     * 할인율 계산
     */
    public int getDiscountRate() {
        if (originalPrice == 0) {
            return 0;
        }
        return (int) (((originalPrice - price) / (double) originalPrice) * 100);
    }
}
