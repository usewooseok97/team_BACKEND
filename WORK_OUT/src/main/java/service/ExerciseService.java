package service;

import dao.ExerciseDAO;
import dto.ExerciseDTO;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class ExerciseService {
    private static ExerciseService instance = new ExerciseService();
    private ExerciseDAO exerciseDAO;

    private static final String API_KEY = "1a077de20bmshdbdbe3f303aa16dp143e78jsn02e222a21c6a";
    private static final String API_HOST = "exercisedb.p.rapidapi.com";
    private static final String BASE_URL = "https://exercisedb.p.rapidapi.com";

    private ExerciseService() {
        exerciseDAO = ExerciseDAO.getInstance();
    }

    public static ExerciseService getInstance() {
        return instance;
    }

    public List<String> fetchTargetList() {
        List<String> targets = new ArrayList<>();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/exercises/targetList"))
                    .header("x-rapidapi-key", API_KEY)
                    .header("x-rapidapi-host", API_HOST)
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            JSONArray jsonArray = new JSONArray(response.body());
            for (int i = 0; i < jsonArray.length(); i++) {
                targets.add(jsonArray.getString(i));
            }

            System.out.println("Fetched " + targets.size() + " targets from API");
        } catch (Exception e) {
            System.err.println("Error fetching target list: " + e.getMessage());
            e.printStackTrace();
        }
        return targets;
    }

    public List<ExerciseDTO> fetchExercisesByTarget(String target) {
        List<ExerciseDTO> exercises = new ArrayList<>();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/exercises/target/" + target))
                    .header("x-rapidapi-key", API_KEY)
                    .header("x-rapidapi-host", API_HOST)
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            JSONArray jsonArray = new JSONArray(response.body());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                ExerciseDTO exercise = jsonToDTO(jsonObj);
                exercises.add(exercise);
            }

            System.out.println("Fetched " + exercises.size() + " exercises for target: " + target);
        } catch (Exception e) {
            System.err.println("Error fetching exercises by target: " + e.getMessage());
            e.printStackTrace();
        }
        return exercises;
    }

    public List<ExerciseDTO> fetchAllExercises(int limit) {
        List<ExerciseDTO> exercises = new ArrayList<>();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/exercises?limit=" + limit))
                    .header("x-rapidapi-key", API_KEY)
                    .header("x-rapidapi-host", API_HOST)
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            JSONArray jsonArray = new JSONArray(response.body());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                ExerciseDTO exercise = jsonToDTO(jsonObj);
                exercises.add(exercise);
            }

            System.out.println("Fetched " + exercises.size() + " exercises from API");
        } catch (Exception e) {
            System.err.println("Error fetching all exercises: " + e.getMessage());
            e.printStackTrace();
        }
        return exercises;
    }

    public boolean saveExercisesToDB(List<ExerciseDTO> exercises) {
        try {
            return exerciseDAO.insertMany(exercises);
        } catch (Exception e) {
            System.err.println("Error saving exercises to DB: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean syncExercisesFromAPI(int limit) {
        try {
            List<ExerciseDTO> exercises = fetchAllExercises(limit);
            if (exercises.isEmpty()) {
                System.out.println("No exercises fetched from API");
                return false;
            }

            exerciseDAO.deleteAll();
            System.out.println("Deleted all existing exercises from DB");

            boolean result = saveExercisesToDB(exercises);
            if (result) {
                System.out.println("Successfully synced " + exercises.size() + " exercises to DB");
            }
            return result;
        } catch (Exception e) {
            System.err.println("Error syncing exercises: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public List<ExerciseDTO> getAllExercises() {
        return exerciseDAO.findAll();
    }

    public ExerciseDTO getExerciseById(String id) {
        return exerciseDAO.findById(id);
    }

    public List<ExerciseDTO> getExercisesByTarget(String target) {
        return exerciseDAO.findByTarget(target);
    }

    public List<ExerciseDTO> getExercisesByBodyPart(String bodyPart) {
        return exerciseDAO.findByBodyPart(bodyPart);
    }

    public List<ExerciseDTO> getExercisesByEquipment(String equipment) {
        return exerciseDAO.findByEquipment(equipment);
    }

    public long getExerciseCount() {
        return exerciseDAO.count();
    }

    private ExerciseDTO jsonToDTO(JSONObject json) {
        ExerciseDTO exercise = new ExerciseDTO();

        exercise.setId(json.optString("id", ""));
        exercise.setName(json.optString("name", ""));
        exercise.setBodyPart(json.optString("bodyPart", ""));
        exercise.setTarget(json.optString("target", ""));
        exercise.setEquipment(json.optString("equipment", ""));

        List<String> secondaryMuscles = new ArrayList<>();
        JSONArray secondaryArray = json.optJSONArray("secondaryMuscles");
        if (secondaryArray != null) {
            for (int i = 0; i < secondaryArray.length(); i++) {
                secondaryMuscles.add(secondaryArray.getString(i));
            }
        }
        exercise.setSecondaryMuscles(secondaryMuscles);

        List<String> instructions = new ArrayList<>();
        JSONArray instructionsArray = json.optJSONArray("instructions");
        if (instructionsArray != null) {
            for (int i = 0; i < instructionsArray.length(); i++) {
                instructions.add(instructionsArray.getString(i));
            }
        }
        exercise.setInstructions(instructions);

        exercise.setDescription(json.optString("description", ""));
        exercise.setDifficulty(json.optString("difficulty", ""));
        exercise.setCategory(json.optString("category", ""));

        return exercise;
    }
}
