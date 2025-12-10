package data;

import dto.StoreCategory;
import java.util.ArrayList;
import java.util.List;

/**
 * Static data for store product categories
 * Contains optimized Korean search queries for Naver Shopping API
 */
public class StoreCategoryData {

    private static final List<StoreCategory> CATEGORIES = new ArrayList<>();

    static {
        // Upper Body Equipment
        CATEGORIES.add(new StoreCategory(
            "upper_body",
            "상체 운동",
            "Upper Body",
            "덤벨 or 바벨 or 벤치프레스 or 풀업바 or 푸쉬업바"
        ));

        // Lower Body Equipment
        CATEGORIES.add(new StoreCategory(
            "lower_body",
            "하체 운동",
            "Lower Body",
            "레그프레스 or 스쿼트랙 or 레그컬 or 레그익스텐션 or 런지"
        ));

        // Cardio Equipment
        CATEGORIES.add(new StoreCategory(
            "cardio",
            "유산소 운동",
            "Cardio",
            "러닝머신 or 실내자전거 or 로잉머신 or 스테퍼 or 일립티컬"
        ));

        // Home Workout Equipment
        CATEGORIES.add(new StoreCategory(
            "home_workout",
            "홈트레이닝",
            "Home Workout",
            "요가매트 or 저항밴드 or 짐볼 or 폼롤러 or 홈트레이닝"
        ));
    }

    /**
     * Get all store categories
     */
    public static List<StoreCategory> getAllCategories() {
        return new ArrayList<>(CATEGORIES);
    }

    /**
     * Get category by ID
     */
    public static StoreCategory getCategoryById(String id) {
        for (StoreCategory category : CATEGORIES) {
            if (category.getId().equals(id)) {
                return category;
            }
        }
        return null;
    }

    /**
     * Check if category ID is valid
     */
    public static boolean isValidCategory(String id) {
        return getCategoryById(id) != null;
    }
}
