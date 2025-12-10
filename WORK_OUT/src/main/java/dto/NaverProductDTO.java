package dto;

public class NaverProductDTO {
    private String title;           // 상품명 (HTML 태그 제거된)
    private String link;            // 네이버 쇼핑 상품 URL
    private String image;           // 이미지 URL
    private int lprice;             // 최저가 (숫자)
    private int hprice;             // 최고가 (숫자, 0이면 없음)
    private String mallName;        // 쇼핑몰 이름
    private String productId;       // 상품 ID
    private String brand;           // 브랜드
    private String maker;           // 제조사

    public NaverProductDTO() {
    }

    public NaverProductDTO(String title, String link, String image, int lprice, int hprice,
                           String mallName, String productId, String brand, String maker) {
        this.title = title;
        this.link = link;
        this.image = image;
        this.lprice = lprice;
        this.hprice = hprice;
        this.mallName = mallName;
        this.productId = productId;
        this.brand = brand;
        this.maker = maker;
    }

    // Amazon DTO와 호환성을 위한 메서드
    public String getName() {
        return title;
    }

    public String getUrl() {
        return link;
    }

    public String getPriceString() {
        return lprice > 0 ? String.format("%,d원", lprice) : "가격 정보 없음";
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getLprice() {
        return lprice;
    }

    public void setLprice(int lprice) {
        this.lprice = lprice;
    }

    public int getHprice() {
        return hprice;
    }

    public void setHprice(int hprice) {
        this.hprice = hprice;
    }

    public String getMallName() {
        return mallName;
    }

    public void setMallName(String mallName) {
        this.mallName = mallName;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getMaker() {
        return maker;
    }

    public void setMaker(String maker) {
        this.maker = maker;
    }

    @Override
    public String toString() {
        return "NaverProductDTO{" +
                "title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", lprice=" + lprice +
                ", hprice=" + hprice +
                ", mallName='" + mallName + '\'' +
                ", brand='" + brand + '\'' +
                '}';
    }
}
