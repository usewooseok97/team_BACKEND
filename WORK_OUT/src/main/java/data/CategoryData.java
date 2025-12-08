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
        bodyParts.add(new CategoryItem("triceps", "삼두근", "💪", "upper"));
        bodyParts.add(new CategoryItem("chest", "가슴", "🏋️", "upper"));
        bodyParts.add(new CategoryItem("biceps", "이두근", "💪", "upper"));
        bodyParts.add(new CategoryItem("hamstrings", "햄스트링", "🦵", "lower"));
        bodyParts.add(new CategoryItem("abdominals", "복근", "王", "upper"));
        bodyParts.add(new CategoryItem("back", "등", "🏋️", "upper"));
        bodyParts.add(new CategoryItem("lats", "광배근", "🏋️", "upper"));
        bodyParts.add(new CategoryItem("quadriceps", "대퇴사두근", "🦵", "lower"));
        bodyParts.add(new CategoryItem("glutes", "둔근", "💪", "upper"));
        bodyParts.add(new CategoryItem("shoulders", "어깨", "💪", "upper"));
        bodyParts.add(new CategoryItem("forearms", "전완근", "💪", "upper"));
        bodyParts.add(new CategoryItem("adductors", "내전근", "🦵", "lower"));
        return bodyParts;
    }

    /**
     * Sports 데이터를 반환
     */
    public static List<CategoryItem> getSports() {
        List<CategoryItem> sports = new ArrayList<>();
        sports.add(new CategoryItem("Swimming", "수영", "🏊", "water"));
        sports.add(new CategoryItem("Soccer", "축구", "⚽", "ground"));
        sports.add(new CategoryItem("Basketball", "농구", "🏀", "ground"));
        sports.add(new CategoryItem("Tennis", "테니스", "🎾", "ground"));
        sports.add(new CategoryItem("Golf", "골프", "⛳", "ground"));
        sports.add(new CategoryItem("Football", "미식축구", "🏈", "ground"));
        sports.add(new CategoryItem("Darts", "다트", "🎯", "home"));
        sports.add(new CategoryItem("Boxing", "복싱", "🥊", "home"));
        sports.add(new CategoryItem("Table Tennis", "탁구", "🏓", "home"));
        sports.add(new CategoryItem("Pool", "당구", "🎱", "home"));
        sports.add(new CategoryItem("Volleyball", "배구", "🏐", "ground"));
        sports.add(new CategoryItem("Fencing", "펜싱", "🤺", "etc"));
        sports.add(new CategoryItem("Archery", "양궁", "🏹", "etc"));
        return sports;
    }

    /**
     * Machines 데이터를 반환
     */
    public static List<CategoryItem> getMachines() {
        List<CategoryItem> machines = new ArrayList<>();
        machines.add(new CategoryItem("Lat Pulldown", "랫 풀다운", "🎰", "upper"));
        machines.add(new CategoryItem("Chest Press", "체스트 프레스", "⚙️", "upper"));
        machines.add(new CategoryItem("Bicep Curl", "바이셉 컬", "🔧", "upper"));
        machines.add(new CategoryItem("Leg Press", "레그 프레스", "⚡", "lower"));
        machines.add(new CategoryItem("Ab Machine", "복근 머신", "🔩", "upper"));
        machines.add(new CategoryItem("Row Machine", "로우 머신", "🛠️", "upper"));
        machines.add(new CategoryItem("Leg Extension", "레그 익스텐션", "⚒️", "lower"));
        machines.add(new CategoryItem("Glute Machine", "둔근 머신", "🔨", "lower"));
        machines.add(new CategoryItem("Shoulder Press", "숄더 프레스", "⛏️", "upper"));
        machines.add(new CategoryItem("Cable Machine", "케이블 머신", "🪛", "etc"));
        machines.add(new CategoryItem("Smith Machine", "스미스 머신", "🔑", "etc"));
        machines.add(new CategoryItem("Leg Curl", "레그 컬", "⚙️", "lower"));
        machines.add(new CategoryItem("Treadmill", "런닝머신", "🔗", "etc"));
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