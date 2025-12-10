package dto;

public class StoreCategory {
    private String id;              // "upper_body", "lower_body", "cardio", "home_workout"
    private String displayName;     // Korean name: "상체 운동"
    private String displayNameEn;   // English name: "Upper Body"
    private String searchQuery;     // Combined query for Naver API

    public StoreCategory() {
    }

    public StoreCategory(String id, String displayName, String displayNameEn, String searchQuery) {
        this.id = id;
        this.displayName = displayName;
        this.displayNameEn = displayNameEn;
        this.searchQuery = searchQuery;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayNameEn() {
        return displayNameEn;
    }

    public void setDisplayNameEn(String displayNameEn) {
        this.displayNameEn = displayNameEn;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    @Override
    public String toString() {
        return "StoreCategory{" +
                "id='" + id + '\'' +
                ", displayName='" + displayName + '\'' +
                ", displayNameEn='" + displayNameEn + '\'' +
                ", searchQuery='" + searchQuery + '\'' +
                '}';
    }
}
