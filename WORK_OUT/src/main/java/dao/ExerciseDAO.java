package dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.*;
import dto.ExerciseDTO;
import mongoutil.MongoConn;
import org.bson.*;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class ExerciseDAO {
    private static final String COLLECTION_NAME = "exercises";
    private static ExerciseDAO instance = new ExerciseDAO();
    private MongoCollection<Document> collection;

    private ExerciseDAO() {
        try {
            MongoDatabase database = MongoConn.getDatabase();
            collection = database.getCollection(COLLECTION_NAME);
            System.out.println("ExerciseDAO initialized successfully with collection: " + COLLECTION_NAME);
        } catch (Exception e) {
            System.err.println("ExerciseDAO initialization failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static ExerciseDAO getInstance() {
        return instance;
    }

    /**
     * 언어에 따라 컬렉션 이름 반환
     */
    private String getCollectionName(String language) {
        return "ko".equals(language) ? "k_exercises" : "exercises";
    }

    /**
     * 언어별 컬렉션 가져오기
     */
    private MongoCollection<Document> getCollection(String language) {
        MongoDatabase database = MongoConn.getDatabase();
        return database.getCollection(getCollectionName(language));
    }

    public boolean insert(ExerciseDTO exercise) {
        return insert(exercise, "en");
    }

    /**
     * Insert exercise into language-specific collection
     * @param exercise Exercise to insert
     * @param language Language code ("en" or "ko")
     * @return true if successful
     */
    public boolean insert(ExerciseDTO exercise, String language) {
        try {
            MongoCollection<Document> col = getCollection(language);
            Document doc = new Document()
                    .append("_id", exercise.getId())
                    .append("name", exercise.getName())
                    .append("primaryMuscles", exercise.getPrimaryMuscles())
                    .append("secondaryMuscles", exercise.getSecondaryMuscles())
                    .append("level", exercise.getLevel());

            col.insertOne(doc);
            return true;
        } catch (Exception e) {
            System.err.println("Error inserting exercise (" + language + "): " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean insertMany(List<ExerciseDTO> exercises) {
        return insertMany(exercises, "en");
    }

    /**
     * Insert multiple exercises into language-specific collection
     * @param exercises List of exercises to insert
     * @param language Language code ("en" or "ko")
     * @return true if successful
     */
    public boolean insertMany(List<ExerciseDTO> exercises, String language) {
        try {
            MongoCollection<Document> col = getCollection(language);
            List<Document> documents = new ArrayList<>();
            for (ExerciseDTO exercise : exercises) {
                Document doc = new Document()
                        .append("_id", exercise.getId())
                        .append("name", exercise.getName())
                        .append("primaryMuscles", exercise.getPrimaryMuscles())
                        .append("secondaryMuscles", exercise.getSecondaryMuscles())
                        .append("level", exercise.getLevel());
                documents.add(doc);
            }
            col.insertMany(documents);
            return true;
        } catch (Exception e) {
            System.err.println("Error inserting exercises (" + language + "): " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public ExerciseDTO findById(String id) {
        return findById(id, "en");  // 기본값: 영어
    }

    /**
     * ID로 운동 조회 (언어별)
     * String 타입과 ObjectId 타입 모두 지원
     */
    public ExerciseDTO findById(String id, String language) {
        try {
            MongoCollection<Document> col = getCollection(language);

            // 1단계: String 타입으로 조회 시도
            Document doc = col.find(Filters.eq("_id", id)).first();

            // 2단계: 못 찾았고 ID가 ObjectId 형식이면, ObjectId 타입으로 조회 시도
            if (doc == null && org.bson.types.ObjectId.isValid(id)) {
                System.out.println("[DEBUG] String _id not found, trying ObjectId for: " + id);
                doc = col.find(Filters.eq("_id", new org.bson.types.ObjectId(id))).first();
                if (doc != null) {
                    System.out.println("[SUCCESS] Found with ObjectId type");
                }
            }

            return documentToDTO(doc);
        } catch (Exception e) {
            System.err.println("Error finding exercise by id (" + language + "): " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public List<ExerciseDTO> findAll() {
        return findAll("en");  // 기본값: 영어
    }

    /**
     * 모든 운동 조회 (언어별)
     */
    public List<ExerciseDTO> findAll(String language) {
        List<ExerciseDTO> exercises = new ArrayList<>();
        try {
            MongoCollection<Document> col = getCollection(language);
            for (Document doc : col.find()) {
                ExerciseDTO exercise = documentToDTO(doc);
                if (exercise != null) {
                    exercises.add(exercise);
                }
            }
        } catch (Exception e) {
            System.err.println("Error finding all exercises (" + language + "): " + e.getMessage());
            e.printStackTrace();
        }
        return exercises;
    }

    /**
     * 모든 운동 조회 (언어별, 페이지네이션 지원)
     * @param language 언어 코드
     * @param page 페이지 번호 (1부터 시작)
     * @param pageSize 페이지당 항목 수
     * @return 해당 페이지의 운동 목록
     */
    public List<ExerciseDTO> findAll(String language, int page, int pageSize) {
        List<ExerciseDTO> exercises = new ArrayList<>();
        try {
            MongoCollection<Document> col = getCollection(language);
            int skip = (page - 1) * pageSize;

            for (Document doc : col.find().skip(skip).limit(pageSize)) {
                ExerciseDTO exercise = documentToDTO(doc);
                if (exercise != null) {
                    exercises.add(exercise);
                }
            }
        } catch (Exception e) {
            System.err.println("Error finding all exercises with pagination (" + language + "): " + e.getMessage());
            e.printStackTrace();
        }
        return exercises;
    }

    public List<ExerciseDTO> findByPrimaryMuscle(String muscle) {
        return findByPrimaryMuscle(muscle, "en");  // 기본값: 영어
    }

    /**
     * Primary muscle로 필터링 (언어별)
     */
    public List<ExerciseDTO> findByPrimaryMuscle(String muscle, String language) {
        List<ExerciseDTO> exercises = new ArrayList<>();
        try {
            MongoCollection<Document> col = getCollection(language);
            for (Document doc : col.find(Filters.eq("primaryMuscles", muscle))) {
                ExerciseDTO exercise = documentToDTO(doc);
                if (exercise != null) {
                    exercises.add(exercise);
                }
            }
        } catch (Exception e) {
            System.err.println("Error finding exercises by primary muscle (" + language + "): " + e.getMessage());
            e.printStackTrace();
        }
        return exercises;
    }

    public List<ExerciseDTO> findByLevel(String level) {
        return findByLevel(level, "en");  // 기본값: 영어
    }

    /**
     * Level로 필터링 (언어별)
     */
    public List<ExerciseDTO> findByLevel(String level, String language) {
        List<ExerciseDTO> exercises = new ArrayList<>();
        try {
            MongoCollection<Document> col = getCollection(language);
            for (Document doc : col.find(Filters.eq("level", level))) {
                ExerciseDTO exercise = documentToDTO(doc);
                if (exercise != null) {
                    exercises.add(exercise);
                }
            }
        } catch (Exception e) {
            System.err.println("Error finding exercises by level (" + language + "): " + e.getMessage());
            e.printStackTrace();
        }
        return exercises;
    }

    public List<ExerciseDTO> findByNameContaining(String keyword) {
        return findByNameContaining(keyword, "en");
    }

    /**
     * Find exercises by name containing keyword (language-aware)
     * @param keyword Keyword to search
     * @param language Language code ("en" or "ko")
     * @return List of matching exercises
     */
    public List<ExerciseDTO> findByNameContaining(String keyword, String language) {
        List<ExerciseDTO> exercises = new ArrayList<>();
        try {
            MongoCollection<Document> col = getCollection(language);
            // 대소문자 구분 없이 부분 검색
            for (Document doc : col.find(Filters.regex("name", ".*" + keyword + ".*", "i"))) {
                ExerciseDTO exercise = documentToDTO(doc);
                if (exercise != null) {
                    exercises.add(exercise);
                }
            }
        } catch (Exception e) {
            System.err.println("Error finding exercises by name (" + language + "): " + e.getMessage());
            e.printStackTrace();
        }
        return exercises;
    }

    public List<ExerciseDTO> findByMultipleFields(String keyword) {
        return findByMultipleFields(keyword, "en");  // 기본값: 영어
    }

    /**
     * 여러 필드에서 검색 (언어별)
     */
    public List<ExerciseDTO> findByMultipleFields(String keyword, String language) {
        List<ExerciseDTO> exercises = new ArrayList<>();
        try {
            MongoCollection<Document> col = getCollection(language);
            // name, primaryMuscles, secondaryMuscles, level 중 하나라도 일치하면 반환 (대소문자 구분 없이)
            org.bson.conversions.Bson filter = Filters.or(
                Filters.regex("name", ".*" + keyword + ".*", "i"),
                Filters.regex("primaryMuscles", ".*" + keyword + ".*", "i"),
                Filters.regex("secondaryMuscles", ".*" + keyword + ".*", "i"),
                Filters.regex("level", ".*" + keyword + ".*", "i")
            );

            for (Document doc : col.find(filter)) {
                ExerciseDTO exercise = documentToDTO(doc);
                if (exercise != null) {
                    exercises.add(exercise);
                }
            }
        } catch (Exception e) {
            System.err.println("Error finding exercises by multiple fields (" + language + "): " + e.getMessage());
            e.printStackTrace();
        }
        return exercises;
    }

    /**
     * 여러 필드에서 검색 (언어별, 페이지네이션 지원)
     * @param keyword 검색어
     * @param language 언어 코드
     * @param page 페이지 번호 (1부터 시작)
     * @param pageSize 페이지당 항목 수
     * @return 해당 페이지의 검색 결과
     */
    public List<ExerciseDTO> findByMultipleFields(String keyword, String language, int page, int pageSize) {
        List<ExerciseDTO> exercises = new ArrayList<>();
        try {
            MongoCollection<Document> col = getCollection(language);
            int skip = (page - 1) * pageSize;

            org.bson.conversions.Bson filter = Filters.or(
                Filters.regex("name", ".*" + keyword + ".*", "i"),
                Filters.regex("primaryMuscles", ".*" + keyword + ".*", "i"),
                Filters.regex("secondaryMuscles", ".*" + keyword + ".*", "i"),
                Filters.regex("level", ".*" + keyword + ".*", "i")
            );

            for (Document doc : col.find(filter).skip(skip).limit(pageSize)) {
                ExerciseDTO exercise = documentToDTO(doc);
                if (exercise != null) {
                    exercises.add(exercise);
                }
            }
        } catch (Exception e) {
            System.err.println("Error finding exercises by multiple fields with pagination (" + language + "): " + e.getMessage());
            e.printStackTrace();
        }
        return exercises;
    }

    /**
     * 검색 결과 총 개수 반환
     * @param keyword 검색어
     * @param language 언어 코드
     * @return 검색 결과 총 개수
     */
    public long countByMultipleFields(String keyword, String language) {
        try {
            MongoCollection<Document> col = getCollection(language);
            org.bson.conversions.Bson filter = Filters.or(
                Filters.regex("name", ".*" + keyword + ".*", "i"),
                Filters.regex("primaryMuscles", ".*" + keyword + ".*", "i"),
                Filters.regex("secondaryMuscles", ".*" + keyword + ".*", "i"),
                Filters.regex("level", ".*" + keyword + ".*", "i")
            );
            return col.countDocuments(filter);
        } catch (Exception e) {
            System.err.println("Error counting search results (" + language + "): " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    public boolean update(ExerciseDTO exercise) {
        return update(exercise, "en");
    }

    /**
     * Update exercise in language-specific collection
     * @param exercise Exercise to update
     * @param language Language code ("en" or "ko")
     * @return true if successful
     */
    public boolean update(ExerciseDTO exercise, String language) {
        try {
            MongoCollection<Document> col = getCollection(language);
            Document doc = new Document()
                    .append("name", exercise.getName())
                    .append("primaryMuscles", exercise.getPrimaryMuscles())
                    .append("secondaryMuscles", exercise.getSecondaryMuscles())
                    .append("level", exercise.getLevel());

            col.updateOne(Filters.eq("_id", exercise.getId()), new Document("$set", doc));
            return true;
        } catch (Exception e) {
            System.err.println("Error updating exercise (" + language + "): " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(String id) {
        return delete(id, "en");
    }

    /**
     * Delete exercise from language-specific collection
     * @param id Exercise ID to delete
     * @param language Language code ("en" or "ko")
     * @return true if successful
     */
    public boolean delete(String id, String language) {
        try {
            MongoCollection<Document> col = getCollection(language);
            col.deleteOne(Filters.eq("_id", id));
            return true;
        } catch (Exception e) {
            System.err.println("Error deleting exercise (" + language + "): " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public long count() {
        return count("en");  // 기본값: 영어
    }

    /**
     * 운동 개수 조회 (언어별)
     */
    public long count(String language) {
        try {
            MongoCollection<Document> col = getCollection(language);
            return col.countDocuments();
        } catch (Exception e) {
            System.err.println("Error counting exercises (" + language + "): " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    public boolean deleteAll() {
        return deleteAll("en");
    }

    /**
     * Delete all exercises from language-specific collection
     * CRITICAL: This protects Korean data by deleting only the specified language collection
     * @param language Language code ("en" or "ko")
     * @return true if successful
     */
    public boolean deleteAll(String language) {
        try {
            MongoCollection<Document> col = getCollection(language);
            col.deleteMany(new Document());
            System.out.println("Deleted all exercises from " + language + " collection");
            return true;
        } catch (Exception e) {
            System.err.println("Error deleting all exercises (" + language + "): " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    private ExerciseDTO documentToDTO(Document doc) {
        if (doc == null) {
            return null;
        }

        ExerciseDTO exercise = new ExerciseDTO();
        // _id는 ObjectId 또는 String일 수 있으므로 안전하게 처리
        Object idObj = doc.get("_id");
        if (idObj != null) {
            exercise.setId(idObj.toString());
        }
        exercise.setName(doc.getString("name"));
        exercise.setPrimaryMuscles((List<String>) doc.get("primaryMuscles"));
        exercise.setSecondaryMuscles((List<String>) doc.get("secondaryMuscles"));
        // images는 images 테이블에서 별도 조회
        exercise.setLevel(doc.getString("level"));

        return exercise;
    }
}
