package dto;

import java.util.List;

public class ExerciseDTO {
    private String id;
    private String name;
    private String bodyPart;
    private String target;
    private String equipment;
    private List<String> secondaryMuscles;
    private List<String> instructions;
    private String description;
    private String difficulty;
    private String category;
    private String gifUrl;

    public ExerciseDTO() {
    }

    public ExerciseDTO(String id, String name, String bodyPart, String target, String equipment,
                       List<String> secondaryMuscles, List<String> instructions, String description,
                       String difficulty, String category, String gifUrl) {
        this.id = id;
        this.name = name;
        this.bodyPart = bodyPart;
        this.target = target;
        this.equipment = equipment;
        this.secondaryMuscles = secondaryMuscles;
        this.instructions = instructions;
        this.description = description;
        this.difficulty = difficulty;
        this.category = category;
        this.gifUrl = gifUrl;
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

    public String getBodyPart() {
        return bodyPart;
    }

    public void setBodyPart(String bodyPart) {
        this.bodyPart = bodyPart;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public List<String> getSecondaryMuscles() {
        return secondaryMuscles;
    }

    public void setSecondaryMuscles(List<String> secondaryMuscles) {
        this.secondaryMuscles = secondaryMuscles;
    }

    public List<String> getInstructions() {
        return instructions;
    }

    public void setInstructions(List<String> instructions) {
        this.instructions = instructions;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getGifUrl() {
        return gifUrl;
    }

    public void setGifUrl(String gifUrl) {
        this.gifUrl = gifUrl;
    }

    @Override
    public String toString() {
        return "ExerciseDTO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", bodyPart='" + bodyPart + '\'' +
                ", target='" + target + '\'' +
                ", equipment='" + equipment + '\'' +
                ", difficulty='" + difficulty + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}
