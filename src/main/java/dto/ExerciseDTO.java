package dto;

import java.util.List;

public class ExerciseDTO {
    private String id;
    private String name;
    private List<String> primaryMuscles;
    private List<String> secondaryMuscles;
    private List<String> images;
    private String level;

    public ExerciseDTO() {
    }

    public ExerciseDTO(String id, String name, List<String> primaryMuscles,
                       List<String> secondaryMuscles, List<String> images, String level) {
        this.id = id;
        this.name = name;
        this.primaryMuscles = primaryMuscles;
        this.secondaryMuscles = secondaryMuscles;
        this.images = images;
        this.level = level;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getPrimaryMuscles() {
        return primaryMuscles;
    }

    public void setPrimaryMuscles(List<String> primaryMuscles) {
        this.primaryMuscles = primaryMuscles;
    }

    public List<String> getSecondaryMuscles() {
        return secondaryMuscles;
    }

    public void setSecondaryMuscles(List<String> secondaryMuscles) {
        this.secondaryMuscles = secondaryMuscles;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "ExerciseDTO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", primaryMuscles=" + primaryMuscles +
                ", secondaryMuscles=" + secondaryMuscles +
                ", images=" + images +
                ", level='" + level + '\'' +
                '}';
    }
}
