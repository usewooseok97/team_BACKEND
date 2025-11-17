package data;

import model.CategoryItem;
import dto.ProductDTO;
import java.util.ArrayList;
import java.util.List;

/**
 * 카테고리 및 제품 데이터를 제공하는 클래스
 */
public class CategoryData {

    /**
     * Body Parts 데이터를 반환
     */
    public static List<CategoryItem> getBodyParts() {
        List<CategoryItem> bodyParts = new ArrayList<>();
        bodyParts.add(new CategoryItem("Triceps", "💪", "upper"));
        bodyParts.add(new CategoryItem("Chest", "🦾", "upper"));
        bodyParts.add(new CategoryItem("Biceps", "💪", "upper"));
        bodyParts.add(new CategoryItem("Hamstrings", "🦵", "lower"));
        bodyParts.add(new CategoryItem("Abs", "🔥", "upper"));
        bodyParts.add(new CategoryItem("Back", "🏋️", "upper"));
        bodyParts.add(new CategoryItem("Quads", "🦵", "lower"));
        bodyParts.add(new CategoryItem("Glutes", "🍑", "lower"));
        bodyParts.add(new CategoryItem("Shoulders", "💪", "upper"));
        bodyParts.add(new CategoryItem("Trapezius", "🦾", "upper"));
        bodyParts.add(new CategoryItem("Forearms", "💪", "upper"));
        bodyParts.add(new CategoryItem("Adductors", "🦵", "lower"));
        bodyParts.add(new CategoryItem("Abductors", "🦵", "lower"));
        return bodyParts;
    }

    /**
     * Sports 데이터를 반환
     */
    public static List<CategoryItem> getSports() {
        List<CategoryItem> sports = new ArrayList<>();
        sports.add(new CategoryItem("Swimming", "🏊", "water"));
        sports.add(new CategoryItem("Soccer", "⚽", "ground"));
        sports.add(new CategoryItem("Basketball", "🏀", "ground"));
        sports.add(new CategoryItem("Tennis", "🎾", "ground"));
        sports.add(new CategoryItem("Golf", "⛳", "ground"));
        sports.add(new CategoryItem("Football", "🏈", "ground"));
        sports.add(new CategoryItem("Darts", "🎯", "home"));
        sports.add(new CategoryItem("Boxing", "🥊", "home"));
        sports.add(new CategoryItem("Table Tennis", "🏓", "home"));
        sports.add(new CategoryItem("Pool", "🎱", "home"));
        sports.add(new CategoryItem("Volleyball", "🏐", "ground"));
        sports.add(new CategoryItem("Fencing", "🤺", "etc"));
        sports.add(new CategoryItem("Archery", "🏹", "etc"));
        return sports;
    }

    /**
     * Machines 데이터를 반환
     */
    public static List<CategoryItem> getMachines() {
        List<CategoryItem> machines = new ArrayList<>();
        machines.add(new CategoryItem("Lat Pulldown", "🎰", "upper"));
        machines.add(new CategoryItem("Chest Press", "⚙️", "upper"));
        machines.add(new CategoryItem("Bicep Curl", "🔧", "upper"));
        machines.add(new CategoryItem("Leg Press", "⚡", "lower"));
        machines.add(new CategoryItem("Ab Machine", "🔩", "upper"));
        machines.add(new CategoryItem("Row Machine", "🛠️", "upper"));
        machines.add(new CategoryItem("Leg Extension", "⚒️", "lower"));
        machines.add(new CategoryItem("Glute Machine", "🔨", "lower"));
        machines.add(new CategoryItem("Shoulder Press", "⛏️", "upper"));
        machines.add(new CategoryItem("Cable Machine", "🪛", "etc"));
        machines.add(new CategoryItem("Smith Machine", "🔑", "etc"));
        machines.add(new CategoryItem("Leg Curl", "⚙️", "lower"));
        machines.add(new CategoryItem("Treadmill", "🔗", "etc"));
        return machines;
    }

    /**
     * Products 데이터를 반환
     */
    public static List<ProductDTO> getProducts() {
        List<ProductDTO> products = new ArrayList<>();
        products.add(new ProductDTO("GORNATION", "Dip Belt", 100, 88, "🥘"));
        products.add(new ProductDTO("Rogue", "Monster Bands", 25, 22, "🥘"));
        products.add(new ProductDTO("GORNATION", "Premium Pull Up Station", 122, 102, "🥘"));
        products.add(new ProductDTO("Rogue", "Dumbbells", 50, 45, "🥘"));
        return products;
    }

    /**
     * 특정 카테고리에 해당하는 아이템만 필터링
     */
    public static List<CategoryItem> filterByCategory(List<CategoryItem> items, String category) {
        List<CategoryItem> filtered = new ArrayList<>();
        for (CategoryItem item : items) {
            if (category.equals(item.getCategory())) {
                filtered.add(item);
            }
        }
        return filtered;
    }
}
