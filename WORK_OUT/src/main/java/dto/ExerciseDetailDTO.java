package dto;

import java.util.List;

public class ExerciseDetailDTO {
    private String id;
    private String name;
    private String category;
    private String equipment;
    private String force;
    private List<String> images;
    private List<String> instructions;
    private String level;
    private String mechanic;
    private List<String> primaryMuscles;
    private List<String> secondaryMuscles;

    public ExerciseDetailDTO() {
    }

    public ExerciseDetailDTO(String id, String name, String category, String equipment, String force,
                             List<String> images, List<String> instructions, String level, String mechanic,
                             List<String> primaryMuscles, List<String> secondaryMuscles) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.equipment = equipment;
        this.force = force;
        this.images = images;
        this.instructions = instructions;
        this.level = level;
        this.mechanic = mechanic;
        this.primaryMuscles = primaryMuscles;
        this.secondaryMuscles = secondaryMuscles;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public String getForce() {
        return force;
    }

    public void setForce(String force) {
        this.force = force;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<String> getInstructions() {
        return instructions;
    }

    public void setInstructions(List<String> instructions) {
        this.instructions = instructions;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getMechanic() {
        return mechanic;
    }

    public void setMechanic(String mechanic) {
        this.mechanic = mechanic;
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

    @Override
    public String toString() {
        return "ExerciseDetailDTO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", equipment='" + equipment + '\'' +
                ", force='" + force + '\'' +
                ", level='" + level + '\'' +
                ", mechanic='" + mechanic + '\'' +
                ", primaryMuscles=" + primaryMuscles +
                ", secondaryMuscles=" + secondaryMuscles +
                '}';
    }
}
