package service;

import dao.ExerciseDAO;
import dao.ExerciseDetailDAO;
import dao.ImagesDAO;
import dto.ExerciseDTO;
import dto.ExerciseDetailDTO;
import dto.ImagesDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ExerciseService {
    private static ExerciseService instance = new ExerciseService();
    private ExerciseDAO exerciseDAO;
    private ExerciseDetailDAO exerciseDetailDAO;
    private ImagesDAO imagesDAO;
 

    private ExerciseService() {
        exerciseDAO = ExerciseDAO.getInstance();
        exerciseDetailDAO = ExerciseDetailDAO.getInstance();
        imagesDAO = ImagesDAO.getInstance();
    }

    public static ExerciseService getInstance() {
        return instance;
    }
 
    /**
     * DB에서 모든 운동 목록 조회 (언어별)
     */
    public List<ExerciseDTO> getAllExercises(String language) {
        List<ExerciseDTO> exercises = exerciseDAO.findAll(language);
        setExercisesImages(exercises, language);
        return exercises;
    }
 
    public List<ExerciseDTO> getAllExercises(String language, int page, int pageSize) {
        List<ExerciseDTO> exercises = exerciseDAO.findAll(language, page, pageSize);
        setExercisesImages(exercises, language);
        return exercises;
    }

 
    public ExerciseDTO getExerciseById(String id, String language) {
        ExerciseDTO exercise = exerciseDAO.findById(id, language);
        if (exercise != null) {
            setExerciseImages(exercise, language);
        }
        return exercise;
    }
 
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

 
    public List<ExerciseDTO> getExercisesByPrimaryMuscle(String muscle, String language) {
        List<ExerciseDTO> exercises = exerciseDAO.findByPrimaryMuscle(muscle, language);
        setExercisesImages(exercises, language);
        return exercises;
    }
 
    public List<ExerciseDTO> getExercisesByLevel(String level, String language) {
        List<ExerciseDTO> exercises = exerciseDAO.findByLevel(level, language);
        setExercisesImages(exercises, language);
        return exercises;
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
                localImagePaths = detail.getImages();
                
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
