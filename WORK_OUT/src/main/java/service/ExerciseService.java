package service;

import dao.ExerciseDAO;
import dao.ExerciseDetailDAO;
import dao.ImagesDAO;
import dto.ExerciseDTO;
import dto.ExerciseDetailDTO;
import dto.ImagesDTO;
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
    private ExerciseDetailDAO exerciseDetailDAO;
    private ImageDownloadService imageDownloadService;
    private ImagesDAO imagesDAO;

    // 환경변수에서 읽어오거나, 없으면 기본값 사용
    private static final String API_KEY = getEnvOrDefault("RAPIDAPI_KEY","");
    private static final String API_HOST = getEnvOrDefault("EXERCISE_LIST_HOST","");
    private static final String BASE_URL = getEnvOrDefault("EXERCISE_LIST_API_BASE_URL","");

    /**
     * 환경변수 값을 가져오되, 없으면 기본값을 반환
     */
    private static String getEnvOrDefault(String key, String defaultValue) {
        String value = System.getenv(key);
        return (value != null && !value.isEmpty()) ? value : defaultValue;
    }

    private ExerciseService() {
        exerciseDAO = ExerciseDAO.getInstance();
        exerciseDetailDAO = ExerciseDetailDAO.getInstance();
        imageDownloadService = ImageDownloadService.getInstance();
        imagesDAO = ImagesDAO.getInstance();
    }

    public static ExerciseService getInstance() {
        return instance;
    }

    /**
     * API에서 전체 운동 ID 목록을 가져옵니다
     */
    public List<String> fetchExerciseIds() {
        List<String> exerciseIds = new ArrayList<>();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/exercises"))
                    .header("x-rapidapi-key", API_KEY)
                    .header("x-rapidapi-host", API_HOST)
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("API Response: " + response.body());

            JSONObject jsonResponse = new JSONObject(response.body());
            // API 응답 키가 "excercises_ids" (오타가 있음)
            JSONArray idsArray = jsonResponse.optJSONArray("excercises_ids");

            if (idsArray != null) {
                for (int i = 0; i < idsArray.length(); i++) {
                    exerciseIds.add(idsArray.getString(i));
                }
            }

            System.out.println("Fetched " + exerciseIds.size() + " exercise IDs from API");
        } catch (Exception e) {
            System.err.println("Error fetching exercise IDs: " + e.getMessage());
            e.printStackTrace();
        }
        return exerciseIds;
    }

    /**
     * API에서 특정 운동의 상세 정보를 가져옵니다
     */
    public ExerciseDetailDTO fetchExerciseDetail(String exerciseId) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/exercise/" + exerciseId))
                    .header("x-rapidapi-key", API_KEY)
                    .header("x-rapidapi-host", API_HOST)
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            JSONObject json = new JSONObject(response.body());
            return jsonToDetailDTO(json);
        } catch (Exception e) {
            System.err.println("Error fetching exercise detail for " + exerciseId + ": " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * API에서 전체 운동 데이터를 가져와서 DB에 저장합니다
     * 1. exercise IDs 목록 조회
     * 2. 각 ID의 상세 정보 조회
     * 3. exerciseDetails 테이블에 저장
     * 4. exercises 테이블에 요약 정보 저장
     */
    public boolean syncAllExercisesFromAPI(int limit) {
        try {
            // 1. exercise IDs 목록 조회
            List<String> exerciseIds = fetchExerciseIds();
            if (exerciseIds.isEmpty()) {
                System.out.println("No exercise IDs fetched from API");
                return false;
            }

            // limit 적용
            if (limit > 0 && limit < exerciseIds.size()) {
                exerciseIds = exerciseIds.subList(0, limit);
            }

            System.out.println("Processing " + exerciseIds.size() + " exercises...");

            // 기존 데이터 삭제
            exerciseDAO.deleteAll();
            exerciseDetailDAO.deleteAll();
            System.out.println("Deleted all existing exercise data from DB");

            List<ExerciseDTO> exercises = new ArrayList<>();
            List<ExerciseDetailDTO> exerciseDetails = new ArrayList<>();

            // 2. 각 ID의 상세 정보 조회
            int count = 0;
            for (String exerciseId : exerciseIds) {
                ExerciseDetailDTO detail = fetchExerciseDetail(exerciseId);
                if (detail != null) {
                    exerciseDetails.add(detail);

                    // exercises 테이블용 요약 데이터 생성
                    ExerciseDTO exercise = new ExerciseDTO();
                    exercise.setId(detail.getId());
                    exercise.setName(detail.getName());
                    exercise.setPrimaryMuscles(detail.getPrimaryMuscles());
                    exercise.setSecondaryMuscles(detail.getSecondaryMuscles());
                    exercise.setImages(detail.getImages());
                    exercise.setLevel(detail.getLevel());
                    exercises.add(exercise);

                    count++;
                    if (count % 10 == 0) {
                        System.out.println("Processed " + count + "/" + exerciseIds.size() + " exercises");
                    }
                }

                // API 부하 방지를 위한 짧은 대기
                Thread.sleep(100);
            }

            // 3. DB에 저장
            boolean detailsSaved = exerciseDetailDAO.insertMany(exerciseDetails);
            boolean exercisesSaved = exerciseDAO.insertMany(exercises);

            if (detailsSaved && exercisesSaved) {
                System.out.println("Successfully synced " + exercises.size() + " exercises to DB");
                return true;
            } else {
                System.err.println("Failed to save exercises to DB");
                return false;
            }
        } catch (Exception e) {
            System.err.println("Error syncing exercises: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * DB에서 모든 운동 목록 조회
     */
    public List<ExerciseDTO> getAllExercises() {
        List<ExerciseDTO> exercises = exerciseDAO.findAll();
        setExercisesImages(exercises);
        return exercises;
    }

    /**
     * DB에서 특정 운동의 요약 정보 조회
     */
    public ExerciseDTO getExerciseById(String id) {
        ExerciseDTO exercise = exerciseDAO.findById(id);
        if (exercise != null) {
            setExerciseImages(exercise);
        }
        return exercise;
    }

    /**
     * DB에서 특정 운동의 상세 정보 조회
     */
    public ExerciseDetailDTO getExerciseDetailById(String id) {
        ExerciseDetailDTO exerciseDetail = exerciseDetailDAO.findById(id);
        if (exerciseDetail != null) {
            setExerciseDetailImages(exerciseDetail);
        }
        return exerciseDetail;
    }

    /**
     * 운동 이름으로 검색
     */
    public List<ExerciseDTO> searchByName(String keyword) {
        List<ExerciseDTO> exercises = exerciseDAO.findByNameContaining(keyword);
        setExercisesImages(exercises);
        return exercises;
    }

    /**
     * 여러 필드에서 검색
     */
    public List<ExerciseDTO> searchByMultipleFields(String keyword) {
        List<ExerciseDTO> exercises = exerciseDAO.findByMultipleFields(keyword);
        setExercisesImages(exercises);
        return exercises;
    }

    /**
     * Primary muscle로 필터링
     */
    public List<ExerciseDTO> getExercisesByPrimaryMuscle(String muscle) {
        List<ExerciseDTO> exercises = exerciseDAO.findByPrimaryMuscle(muscle);
        setExercisesImages(exercises);
        return exercises;
    }

    /**
     * Level로 필터링
     */
    public List<ExerciseDTO> getExercisesByLevel(String level) {
        List<ExerciseDTO> exercises = exerciseDAO.findByLevel(level);
        setExercisesImages(exercises);
        return exercises;
    }

    /**
     * 운동 개수 조회
     */
    public long getExerciseCount() {
        return exerciseDAO.count();
    }

    /**
     * 기존 exerciseDetails 테이블의 데이터를 사용하여 exercises 테이블을 채웁니다
     * API 호출 없이 이미 저장된 상세 데이터에서 요약 정보를 추출합니다
     */
    public boolean syncExercisesFromDetails() {
        return syncExercisesFromDetails(null);
    }

    /**
     * 기존 exerciseDetails 테이블의 데이터를 사용하여 exercises 테이블을 채웁니다
     * API 호출 없이 이미 저장된 상세 데이터에서 요약 정보를 추출합니다
     * @param webappPath 웹앱 실제 경로 (이미지 다운로드용, null이면 이미지 다운로드 생략)
     */
    public boolean syncExercisesFromDetails(String webappPath) {
        try {
            // 1. exerciseDetails 테이블에서 모든 데이터 가져오기
            List<ExerciseDetailDTO> exerciseDetails = exerciseDetailDAO.findAll();

            if (exerciseDetails.isEmpty()) {
                System.out.println("No exercise details found in exerciseDetails");
                return false;
            }

            System.out.println("Found " + exerciseDetails.size() + " exercise details");

            // 2. exercises 테이블 초기화
            exerciseDAO.deleteAll();
            System.out.println("Deleted all existing exercises");

            // 3. exerciseDetails에서 exercises 생성 및 이미지 다운로드
            List<ExerciseDTO> exercises = new ArrayList<>();
            List<ImagesDTO> imagesList = new ArrayList<>();
            int count = 0;

            for (ExerciseDetailDTO detail : exerciseDetails) {
                ExerciseDTO exercise = new ExerciseDTO();
                exercise.setId(detail.getId());
                exercise.setName(detail.getName());
                exercise.setPrimaryMuscles(detail.getPrimaryMuscles());
                exercise.setSecondaryMuscles(detail.getSecondaryMuscles());
                exercise.setLevel(detail.getLevel());
                exercises.add(exercise);

                // 이미지 다운로드 및 로컬 경로 저장
                List<String> localImagePaths = new ArrayList<>();
                if (webappPath != null && detail.getImages() != null && !detail.getImages().isEmpty()) {
                    for (int i = 0; i < detail.getImages().size(); i++) {
                        String imageUrl = detail.getImages().get(i);
                        String localPath = imageDownloadService.downloadAndSaveImage(
                            imageUrl, detail.getId(), i, webappPath);
                        if (localPath != null) {
                            localImagePaths.add(localPath);
                        }
                    }
                }

                // 다운로드 실패 시 원본 URL 사용
                if (localImagePaths.isEmpty() && detail.getImages() != null) {
                    localImagePaths = detail.getImages();
                }

                // images 테이블에 저장할 데이터 준비
                String id = detail.getId();
                ImagesDTO imageData = new ImagesDTO(id, localImagePaths);
                imagesList.add(imageData);

                count++;
                if (count % 10 == 0) {
                    System.out.println("Processed " + count + "/" + exerciseDetails.size() + " exercises");
                }
            }

            // 4. exercises 테이블에 저장
            boolean success = exerciseDAO.insertMany(exercises);

            // 5. images 테이블에 저장 (upsert 방식)
            if (success) {
                int updatedCount = 0;
                int insertedCount = 0;

                for (ImagesDTO imageData : imagesList) {
                    String id = imageData.getId();
                    ImagesDTO existing = imagesDAO.findById(id);

                    if (existing != null) {
                        // 업데이트
                        imagesDAO.update(id, imageData);
                        updatedCount++;
                    } else {
                        // 새로 삽입
                        imagesDAO.insert(imageData);
                        insertedCount++;
                    }
                }

                System.out.println("Images table updated: " + updatedCount + " updated, " + insertedCount + " inserted");
            }

            if (success) {
                System.out.println("Successfully created " + exercises.size() + " exercises from exerciseDetails");
                return true;
            } else {
                System.err.println("Failed to save exercises");
                return false;
            }
        } catch (Exception e) {
            System.err.println("Error syncing exercises from details: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * exerciseDetails의 이미지를 다운로드하여 images 테이블에 저장
     * @param webappPath 웹앱 실제 경로 (이미지 다운로드용)
     */
    public boolean syncImagesFromExerciseDetails(String webappPath) {
        try {
            // 1. exerciseDetails 테이블에서 모든 데이터 가져오기
            List<ExerciseDetailDTO> exerciseDetails = exerciseDetailDAO.findAll();

            if (exerciseDetails.isEmpty()) {
                System.out.println("No exercise details found");
                return false;
            }

            System.out.println("Found " + exerciseDetails.size() + " exercise details");

            // 2. 이미지 다운로드 및 images 테이블에 저장
            int updatedCount = 0;
            int insertedCount = 0;
            int processedCount = 0;

            for (ExerciseDetailDTO detail : exerciseDetails) {
                // 이미지 다운로드
                List<String> localImagePaths = new ArrayList<>();
                if (webappPath != null && detail.getImages() != null && !detail.getImages().isEmpty()) {
                    for (int i = 0; i < detail.getImages().size(); i++) {
                        String imageUrl = detail.getImages().get(i);
                        String localPath = imageDownloadService.downloadAndSaveImage(
                            imageUrl, detail.getId(), i, webappPath);
                        if (localPath != null) {
                            localImagePaths.add(localPath);
                        }
                    }
                }

                // 다운로드 실패 시 원본 URL 사용
                if (localImagePaths.isEmpty() && detail.getImages() != null) {
                    localImagePaths = detail.getImages();
                }

                // images 테이블에 저장할 데이터 준비
                String id = detail.getId();
                ImagesDTO imageData = new ImagesDTO(id, localImagePaths);

                // 기존 데이터 확인
                ImagesDTO existing = imagesDAO.findById(id);
                if (existing != null) {
                    // 업데이트
                    imagesDAO.update(id, imageData);
                    updatedCount++;
                } else {
                    // 새로 삽입
                    imagesDAO.insert(imageData);
                    insertedCount++;
                }

                processedCount++;
                if (processedCount % 10 == 0) {
                    System.out.println("Processed " + processedCount + "/" + exerciseDetails.size() + " images");
                }
            }

            System.out.println("Images sync completed: " + updatedCount + " updated, " + insertedCount + " inserted");
            return true;
        } catch (Exception e) {
            System.err.println("Error syncing images from exerciseDetails: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * JSON을 ExerciseDetailDTO로 변환
     */
    private ExerciseDetailDTO jsonToDetailDTO(JSONObject json) {
        ExerciseDetailDTO detail = new ExerciseDetailDTO();

        detail.setId(json.optString("id", ""));
        detail.setName(json.optString("name", ""));
        detail.setCategory(json.optString("category", ""));
        detail.setEquipment(json.optString("equipment", ""));
        detail.setForce(json.optString("force", ""));
        detail.setLevel(json.optString("level", ""));
        detail.setMechanic(json.optString("mechanic", ""));

        // images 배열 처리
        List<String> images = new ArrayList<>();
        JSONArray imagesArray = json.optJSONArray("images");
        if (imagesArray != null) {
            for (int i = 0; i < imagesArray.length(); i++) {
                images.add(imagesArray.getString(i));
            }
        }
        detail.setImages(images);

        // instructions 배열 처리
        List<String> instructions = new ArrayList<>();
        JSONArray instructionsArray = json.optJSONArray("instructions");
        if (instructionsArray != null) {
            for (int i = 0; i < instructionsArray.length(); i++) {
                instructions.add(instructionsArray.getString(i));
            }
        }
        detail.setInstructions(instructions);

        // primaryMuscles 배열 처리
        List<String> primaryMuscles = new ArrayList<>();
        JSONArray primaryArray = json.optJSONArray("primaryMuscles");
        if (primaryArray != null) {
            for (int i = 0; i < primaryArray.length(); i++) {
                primaryMuscles.add(primaryArray.getString(i));
            }
        }
        detail.setPrimaryMuscles(primaryMuscles);

        // secondaryMuscles 배열 처리
        List<String> secondaryMuscles = new ArrayList<>();
        JSONArray secondaryArray = json.optJSONArray("secondaryMuscles");
        if (secondaryArray != null) {
            for (int i = 0; i < secondaryArray.length(); i++) {
                secondaryMuscles.add(secondaryArray.getString(i));
            }
        }
        detail.setSecondaryMuscles(secondaryMuscles);

        return detail;
    }

    /**
     * Exercise에 이미지 설정
     */
    private void setExerciseImages(ExerciseDTO exercise) {
        if (exercise == null || exercise.getId() == null) {
            return;
        }

        ImagesDTO images = imagesDAO.findById(exercise.getId());

        if (images != null && images.getImages() != null) {
            exercise.setImages(images.getImages());
        }
    }

    /**
     * ExerciseDetail에 이미지 설정
     */
    private void setExerciseDetailImages(ExerciseDetailDTO exerciseDetail) {
        if (exerciseDetail == null || exerciseDetail.getId() == null) {
            return;
        }

        ImagesDTO images = imagesDAO.findById(exerciseDetail.getId());

        if (images != null && images.getImages() != null) {
            exerciseDetail.setImages(images.getImages());
        }
    }

    /**
     * Exercise 리스트에 이미지 설정
     */
    private void setExercisesImages(List<ExerciseDTO> exercises) {
        if (exercises == null || exercises.isEmpty()) {
            return;
        }

        for (ExerciseDTO exercise : exercises) {
            setExerciseImages(exercise);
        }
    }
}
