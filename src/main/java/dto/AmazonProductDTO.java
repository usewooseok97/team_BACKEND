package dto;

public class AmazonProductDTO {
    private int position;
    private String asin;
    private String name;
    private String image;
    private boolean hasPrime;
    private boolean isBestSeller;
    private double stars;
    private String url;
    private String priceString;
    private double price;

    public AmazonProductDTO() {
    }

    public AmazonProductDTO(int position, String asin, String name, String image,
                            boolean hasPrime, boolean isBestSeller, double stars,
                            String url, String priceString, double price) {
        this.position = position;
        this.asin = asin;
        this.name = name;
        this.image = image;
        this.hasPrime = hasPrime;
        this.isBestSeller = isBestSeller;
        this.stars = stars;
        this.url = url;
        this.priceString = priceString;
        this.price = price;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getAsin() {
        return asin;
    }

    public void setAsin(String asin) {
        this.asin = asin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isHasPrime() {
        return hasPrime;
    }

    public void setHasPrime(boolean hasPrime) {
        this.hasPrime = hasPrime;
    }

    public boolean isIsBestSeller() {
        return isBestSeller;
    }

    public void setIsBestSeller(boolean isBestSeller) {
        this.isBestSeller = isBestSeller;
    }

    public double getStars() {
        return stars;
    }

    public void setStars(double stars) {
        this.stars = stars;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPriceString() {
        return priceString;
    }

    public void setPriceString(String priceString) {
        this.priceString = priceString;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "AmazonProductDTO{" +
                "position=" + position +
                ", asin='" + asin + '\'' +
                ", name='" + name + '\'' +
                ", hasPrime=" + hasPrime +
                ", isBestSeller=" + isBestSeller +
                ", stars=" + stars +
                ", price=" + price +
                '}';
    }
}
