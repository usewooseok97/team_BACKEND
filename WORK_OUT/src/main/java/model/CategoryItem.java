package model;

/**
 * 카테고리 아이템을 표현하는 모델 클래스
 */
public class CategoryItem {
    private String name;
    private String icon;
    private String category;

    public CategoryItem() {
    }

    public CategoryItem(String name, String icon, String category) {
        this.name = name;
        this.icon = icon;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
