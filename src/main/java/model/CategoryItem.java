package model;

/**
 * 카테고리 아이템을 표현하는 모델 클래스
 */
public class CategoryItem {
    private String name;
    private String nameKo;
    private String icon;
    private String category;

    public CategoryItem() {
    }

    public CategoryItem(String name, String icon, String category) {
        this.name = name;
        this.nameKo = name; // 기본값으로 영어 이름 사용
        this.icon = icon;
        this.category = category;
    }

    // 새로운 생성자 추가 (한글 이름 포함)
    public CategoryItem(String name, String nameKo, String icon, String category) {
        this.name = name;
        this.nameKo = nameKo;
        this.icon = icon;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameKo() {
        return nameKo;
    }

    public void setNameKo(String nameKo) {
        this.nameKo = nameKo;
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