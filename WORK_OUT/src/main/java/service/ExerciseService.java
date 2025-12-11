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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
     * DB에서 모든 운동 목록 조회 (기본값: 영어)
     */
    public List<ExerciseDTO> getAllExercises() {
        return getAllExercises("en");
    }

    /**
     * DB에서 모든 운동 목록 조회 (언어별)
     */
    public List<ExerciseDTO> getAllExercises(String language) {
        List<ExerciseDTO> exercises = exerciseDAO.findAll(language);
        setExercisesImages(exercises, language);
        return exercises;
    }

    /**
     * DB에서 모든 운동 목록 조회 (언어별, 페이지네이션 지원)
     * @param language 언어 코드
     * @param page 페이지 번호
     * @param pageSize 페이지당 항목 수
     * @return 해당 페이지의 운동 목록
     */
    public List<ExerciseDTO> getAllExercises(String language, int page, int pageSize) {
        List<ExerciseDTO> exercises = exerciseDAO.findAll(language, page, pageSize);
        setExercisesImages(exercises, language);
        return exercises;
    }

    /**
     * DB에서 특정 운동의 요약 정보 조회 (기본값: 영어)
     */
    public ExerciseDTO getExerciseById(String id) {
        return getExerciseById(id, "en");
    }

    /**
     * DB에서 특정 운동의 요약 정보 조회 (언어별)
     */
    public ExerciseDTO getExerciseById(String id, String language) {
        ExerciseDTO exercise = exerciseDAO.findById(id, language);
        if (exercise != null) {
            setExerciseImages(exercise, language);
        }
        return exercise;
    }

    /**
     * DB에서 특정 운동의 상세 정보 조회 (기본값: 영어)
     */
    public ExerciseDetailDTO getExerciseDetailById(String id) {
        return getExerciseDetailById(id, "en");
    }

    /**
     * DB에서 특정 운동의 상세 정보 조회 (언어별)
     * _id로 조회하며, 현재 언어 컬렉션에 없으면 다른 언어 컬렉션에서 자동으로 재시도
     */
    public ExerciseDetailDTO getExerciseDetailById(String id, String language) {
        // 1단계: 현재 언어 컬렉션에서 조회
        ExerciseDetailDTO exerciseDetail = exerciseDetailDAO.findById(id, language);

        // 2단계: 못 찾으면 다른 언어 컬렉션에서 조회 (fallback)
        if (exerciseDetail == null) {
            String alternativeLanguage = "ko".equals(language) ? "en" : "ko";
            System.out.println("Exercise not found in " + language + " collection. Trying " + alternativeLanguage + " collection...");
            exerciseDetail = exerciseDetailDAO.findById(id, alternativeLanguage);

            if (exerciseDetail != null) {
                System.out.println("Exercise found in " + alternativeLanguage + " collection.");
                // 이미지는 대체 언어 컬렉션에서 로드
                setExerciseDetailImages(exerciseDetail, alternativeLanguage);
            }
        } else {
            setExerciseDetailImages(exerciseDetail, language);
        }

        return exerciseDetail;
    }

    /**
     * 운동 이름으로 검색 (기본값: 영어)
     */
    public List<ExerciseDTO> searchByName(String keyword) {
        return searchByName(keyword, "en");
    }

    /**
     * 운동 이름으로 검색 (언어별)
     */
    public List<ExerciseDTO> searchByName(String keyword, String language) {
        List<ExerciseDTO> exercises = exerciseDAO.findByNameContaining(keyword, language);
        setExercisesImages(exercises, language);
        return exercises;
    }

    /**
     * 여러 필드에서 검색 (기본값: 영어)
     */
    public List<ExerciseDTO> searchByMultipleFields(String keyword) {
        return searchByMultipleFields(keyword, "en");
    }

    /**
     * 여러 필드에서 검색 (언어별)
     */
    public List<ExerciseDTO> searchByMultipleFields(String keyword, String language) {
        List<ExerciseDTO> exercises = exerciseDAO.findByMultipleFields(keyword, language);
        setExercisesImages(exercises, language);
        return exercises;
    }

    /**
     * 여러 필드에서 검색 (언어별, 페이지네이션 지원)
     * @param keyword 검색어
     * @param language 언어 코드
     * @param page 페이지 번호
     * @param pageSize 페이지당 항목 수
     * @return 해당 페이지의 검색 결과
     */
    public List<ExerciseDTO> searchByMultipleFields(String keyword, String language, int page, int pageSize) {
        List<ExerciseDTO> exercises = exerciseDAO.findByMultipleFields(keyword, language, page, pageSize);
        setExercisesImages(exercises, language);
        return exercises;
    }

    /**
     * 검색 결과 총 개수 반환
     * @param keyword 검색어
     * @param language 언어 코드
     * @return 검색 결과 총 개수
     */
    public long getSearchResultCount(String keyword, String language) {
        return exerciseDAO.countByMultipleFields(keyword, language);
    }

    /**
     * Primary muscle로 필터링 (기본값: 영어)
     */
    public List<ExerciseDTO> getExercisesByPrimaryMuscle(String muscle) {
        return getExercisesByPrimaryMuscle(muscle, "en");
    }

    /**
     * Primary muscle로 필터링 (언어별)
     */
    public List<ExerciseDTO> getExercisesByPrimaryMuscle(String muscle, String language) {
        List<ExerciseDTO> exercises = exerciseDAO.findByPrimaryMuscle(muscle, language);
        setExercisesImages(exercises, language);
        return exercises;
    }

    /**
     * Level로 필터링 (기본값: 영어)
     */
    public List<ExerciseDTO> getExercisesByLevel(String level) {
        return getExercisesByLevel(level, "en");
    }

    /**
     * Level로 필터링 (언어별)
     */
    public List<ExerciseDTO> getExercisesByLevel(String level, String language) {
        List<ExerciseDTO> exercises = exerciseDAO.findByLevel(level, language);
        setExercisesImages(exercises, language);
        return exercises;
    }

    /**
     * 운동 개수 조회 (기본값: 영어)
     */
    public long getExerciseCount() {
        return getExerciseCount("en");
    }

    /**
     * 운동 개수 조회 (언어별)
     */
    public long getExerciseCount(String language) {
        return exerciseDAO.count(language);
    }

    /**
     * ID 목록으로 운동 목록 조회 (언어별)
     * 언어 변경 시 같은 운동들을 새로운 언어로 보여주기 위해 사용
     * @param ids 운동 ID 목록
     * @param language 언어 코드
     * @return 해당 ID들의 운동 목록
     */
    public List<ExerciseDTO> getExercisesByIds(List<String> ids, String language) {
        if (ids == null || ids.isEmpty()) {
            return new ArrayList<>();
        }

        List<ExerciseDTO> exercises = new ArrayList<>();
        for (String id : ids) {
            ExerciseDTO exercise = getExerciseById(id, language);
            if (exercise != null) {
                exercises.add(exercise);
            }
        }
        return exercises;
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
     * 영어와 한국어 컬렉션을 모두 동기화합니다
     * @param webappPath 웹앱 실제 경로 (이미지 다운로드용, null이면 이미지 다운로드 생략)
     */
    public boolean syncExercisesFromDetails(String webappPath) {
        boolean enSuccess = syncExercisesFromDetailsForLanguage("en", webappPath);
        boolean koSuccess = syncExercisesFromDetailsForLanguage("ko", webappPath);
        return enSuccess && koSuccess;
    }

    /**
     * 특정 언어의 exerciseDetails를 사용하여 exercises 테이블을 동기화합니다
     * @param language 언어 코드 ("en" or "ko")
     * @param webappPath 웹앱 실제 경로 (이미지 다운로드용, null이면 이미지 다운로드 생략)
     */
    private boolean syncExercisesFromDetailsForLanguage(String language, String webappPath) {
        try {
            System.out.println("Starting sync for language: " + language);

            // 1. exerciseDetails 테이블에서 모든 데이터 가져오기
            List<ExerciseDetailDTO> exerciseDetails = exerciseDetailDAO.findAll(language);

            if (exerciseDetails.isEmpty()) {
                System.out.println("No exercise details found in exerciseDetails (" + language + ")");
                return false;
            }

            System.out.println("Found " + exerciseDetails.size() + " exercise details (" + language + ")");

            // 2. exercises 테이블 초기화
            exerciseDAO.deleteAll(language);
            System.out.println("Deleted all existing exercises (" + language + ")");

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
                    System.out.println("Processed " + count + "/" + exerciseDetails.size() + " exercises (" + language + ")");
                }
            }

            // 4. exercises 테이블에 저장
            boolean success = exerciseDAO.insertMany(exercises, language);

            // 5. images 테이블에 저장 (upsert 방식)
            if (success) {
                int updatedCount = 0;
                int insertedCount = 0;

                for (ImagesDTO imageData : imagesList) {
                    String id = imageData.getId();
                    ImagesDTO existing = imagesDAO.findById(id, language);

                    if (existing != null) {
                        // 업데이트
                        imagesDAO.update(id, imageData, language);
                        updatedCount++;
                    } else {
                        // 새로 삽입
                        imagesDAO.insert(imageData, language);
                        insertedCount++;
                    }
                }

                System.out.println("Images table updated (" + language + "): " + updatedCount + " updated, " + insertedCount + " inserted");
            }

            if (success) {
                System.out.println("Successfully created " + exercises.size() + " exercises from exerciseDetails (" + language + ")");
                return true;
            } else {
                System.err.println("Failed to save exercises (" + language + ")");
                return false;
            }
        } catch (Exception e) {
            System.err.println("Error syncing exercises from details (" + language + "): " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * exerciseDetails의 이미지를 다운로드하여 images 테이블에 저장
     * 영어와 한국어 컬렉션을 모두 동기화합니다
     * @param webappPath 웹앱 실제 경로 (이미지 다운로드용)
     */
    public boolean syncImagesFromExerciseDetails(String webappPath) {
        boolean enSuccess = syncImagesFromExerciseDetailsForLanguage("en", webappPath);
        boolean koSuccess = syncImagesFromExerciseDetailsForLanguage("ko", webappPath);
        return enSuccess && koSuccess;
    }

    /**
     * 특정 언어의 exerciseDetails 이미지를 다운로드하여 images 테이블에 저장
     * @param language 언어 코드 ("en" or "ko")
     * @param webappPath 웹앱 실제 경로 (이미지 다운로드용)
     */
    private boolean syncImagesFromExerciseDetailsForLanguage(String language, String webappPath) {
        try {
            System.out.println("Starting image sync for language: " + language);

            // 1. exerciseDetails 테이블에서 모든 데이터 가져오기
            List<ExerciseDetailDTO> exerciseDetails = exerciseDetailDAO.findAll(language);

            if (exerciseDetails.isEmpty()) {
                System.out.println("No exercise details found (" + language + ")");
                return false;
            }

            System.out.println("Found " + exerciseDetails.size() + " exercise details (" + language + ")");

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
                ImagesDTO existing = imagesDAO.findById(id, language);
                if (existing != null) {
                    // 업데이트
                    imagesDAO.update(id, imageData, language);
                    updatedCount++;
                } else {
                    // 새로 삽입
                    imagesDAO.insert(imageData, language);
                    insertedCount++;
                }

                processedCount++;
                if (processedCount % 10 == 0) {
                    System.out.println("Processed " + processedCount + "/" + exerciseDetails.size() + " images (" + language + ")");
                }
            }

            System.out.println("Images sync completed (" + language + "): " + updatedCount + " updated, " + insertedCount + " inserted");
            return true;
        } catch (Exception e) {
            System.err.println("Error syncing images from exerciseDetails (" + language + "): " + e.getMessage());
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
     * Exercise에 이미지 설정 (기본값: 영어)
     */
    private void setExerciseImages(ExerciseDTO exercise) {
        setExerciseImages(exercise, "en");
    }

    /**
     * Exercise에 이미지 설정 (언어별)
     */
    private void setExerciseImages(ExerciseDTO exercise, String language) {
        if (exercise == null || exercise.getId() == null) {
            return;
        }

        ImagesDTO images = imagesDAO.findById(exercise.getId(), language);

        if (images != null && images.getImages() != null) {
            exercise.setImages(images.getImages());
        }
    }

    /**
     * ExerciseDetail에 이미지 설정 (기본값: 영어)
     */
    private void setExerciseDetailImages(ExerciseDetailDTO exerciseDetail) {
        setExerciseDetailImages(exerciseDetail, "en");
    }

    /**
     * ExerciseDetail에 이미지 설정 (언어별)
     */
    private void setExerciseDetailImages(ExerciseDetailDTO exerciseDetail, String language) {
        if (exerciseDetail == null || exerciseDetail.getId() == null) {
            return;
        }

        ImagesDTO images = imagesDAO.findById(exerciseDetail.getId(), language);

        if (images != null && images.getImages() != null) {
            exerciseDetail.setImages(images.getImages());
        }
    }

    /**
     * Exercise 리스트에 이미지 설정 (기본값: 영어)
     */
    private void setExercisesImages(List<ExerciseDTO> exercises) {
        setExercisesImages(exercises, "en");
    }

    /**
     * Exercise 리스트에 이미지 설정 (언어별)
     * N+1 쿼리 문제 해결: 벌크 조회 사용
     */
    private void setExercisesImages(List<ExerciseDTO> exercises, String language) {
        if (exercises == null || exercises.isEmpty()) {
            return;
        }

        long startTime = System.currentTimeMillis();

        try {
            // 1. 모든 운동 ID 수집
            List<String> ids = exercises.stream()
                .filter(e -> e != null && e.getId() != null)
                .map(ExerciseDTO::getId)
                .collect(Collectors.toList());

            if (ids.isEmpty()) {
                return;
            }

            // 2. 한 번에 모든 이미지 조회 (N+1 해결!)
            Map<String, ImagesDTO> imagesMap = imagesDAO.findByIds(ids, language);

            // 3. 각 운동에 이미지 매핑
            int matchedCount = 0;
            for (ExerciseDTO exercise : exercises) {
                if (exercise != null && exercise.getId() != null) {
                    ImagesDTO images = imagesMap.get(exercise.getId());
                    if (images != null && images.getImages() != null) {
                        exercise.setImages(images.getImages());
                        matchedCount++;
                    } else {
                        exercise.setImages(new ArrayList<>());
                    }
                }
            }

            // 4. 성능 로깅
            long duration = System.currentTimeMillis() - startTime;
            System.out.println(String.format(
                "[ExerciseService] Set images for %d/%d exercises in %dms (%s)",
                matchedCount, exercises.size(), duration, language
            ));

        } catch (Exception e) {
            System.err.println("Error setting exercises images: " + e.getMessage());
            e.printStackTrace();

            // Fallback: 벌크 조회 실패 시 개별 조회로 복구
            System.out.println("[FALLBACK] Using individual image queries");
            for (ExerciseDTO exercise : exercises) {
                setExerciseImages(exercise, language);
            }
        }
    }
}
