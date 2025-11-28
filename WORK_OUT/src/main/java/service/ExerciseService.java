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
import java.util.Base64;
import java.util.List;

public class ExerciseService {
    private static ExerciseService instance = new ExerciseService();
    private ExerciseDAO exerciseDAO;

    // 환경변수에서 읽어오거나, 없으면 기본값 사용
    private static final String API_KEY = getEnvOrDefault("RAPIDAPI_KEY","");
    private static final String API_HOST = getEnvOrDefault("RAPIDAPI_HOST","");
    private static final String BASE_URL = getEnvOrDefault("EXERCISE_API_BASE_URL","");

    /**
     * 환경변수 값을 가져오되, 없으면 기본값을 반환
     */
    private static String getEnvOrDefault(String key, String defaultValue) {
        String value = System.getenv(key);
        return (value != null && !value.isEmpty()) ? value : defaultValue;
    }

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

    public List<ExerciseDTO> searchByName(String keyword) {
        return exerciseDAO.findByNameContaining(keyword);
    }

    public List<ExerciseDTO> searchByMultipleFields(String keyword) {
        return exerciseDAO.findByMultipleFields(keyword);
    }

    public long getExerciseCount() {
        return exerciseDAO.count();
    }

    /**
     * 이미지 API에서 이미지를 다운로드하고 Base64로 인코딩
     */
    private String fetchAndEncodeImage(String exerciseId, int resolution) {
        try {
            String imageUrl = "https://exercisedb.p.rapidapi.com/image?resolution=" + resolution + "&exerciseId=" + exerciseId;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(imageUrl))
                    .header("x-rapidapi-key", API_KEY)
                    .header("x-rapidapi-host", API_HOST)
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<byte[]> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofByteArray());

            // Base64 인코딩
            String base64 = Base64.getEncoder().encodeToString(response.body());

            // Data URI 형식으로 반환
            return "data:image/gif;base64," + base64;
        } catch (Exception e) {
            System.err.println("Error fetching image for exercise " + exerciseId + ": " + e.getMessage());
            return null;
        }
    }

    /**
     * 기존 DB의 모든 운동 데이터에 gifUrl 추가/업데이트
     * 이 메서드는 한 번만 실행하면 됨
     */
    public int updateAllExerciseImages() {
        try {
            List<ExerciseDTO> exercises = exerciseDAO.findAll();
            int updatedCount = 0;

            for (ExerciseDTO exercise : exercises) {
                String gifUrl = exercise.getGifUrl();

                // gifUrl이 없거나 비어있거나 URL 형식이면 Base64로 변환
                if (gifUrl == null || gifUrl.isEmpty() || gifUrl.startsWith("http")) {
                    String exerciseId = exercise.getId();
                    if (exerciseId != null && !exerciseId.isEmpty()) {
                        // 720 해상도로 이미지 다운로드 및 인코딩
                        String base64Image = fetchAndEncodeImage(exerciseId, 720);
                        if (base64Image != null) {
                            exerciseDAO.updateGifUrl(exerciseId, base64Image);
                            updatedCount++;
                            System.out.println("Updated image for exercise: " + exerciseId);
                        }
                    }
                }
            }

            System.out.println("Updated gifUrl for " + updatedCount + " exercises");
            return updatedCount;
        } catch (Exception e) {
            System.err.println("Error updating exercise images: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
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

        // gifUrl 처리: API에서 제공하지 않으면 이미지 다운로드 및 Base64 인코딩
        String gifUrl = json.optString("gifUrl", "");
        if (gifUrl.isEmpty()) {
            String exerciseId = exercise.getId();
            if (!exerciseId.isEmpty()) {
                // 720 해상도로 이미지 다운로드 및 Base64 인코딩
                gifUrl = fetchAndEncodeImage(exerciseId, 720);
            }
        }
        exercise.setGifUrl(gifUrl);

        return exercise;
    }
}
