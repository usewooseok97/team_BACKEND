package dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import dto.ExerciseDetailDTO;
import mongoutil.MongoConn;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class ExerciseDetailDAO {
    private static final String COLLECTION_NAME = "exerciseDetails";
    private static ExerciseDetailDAO instance = new ExerciseDetailDAO();
    private MongoCollection<Document> collection;

    private ExerciseDetailDAO() {
        try {
            MongoDatabase database = MongoConn.getDatabase();
            collection = database.getCollection(COLLECTION_NAME);
            System.out.println("ExerciseDetailDAO initialized successfully with collection: " + COLLECTION_NAME);
        } catch (Exception e) {
            System.err.println("ExerciseDetailDAO initialization failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static ExerciseDetailDAO getInstance() {
        return instance;
    }

    /**
     * 언어에 따라 컬렉션 이름 반환
     */
    private String getCollectionName(String language) {
        return "ko".equals(language) ? "k_exerciseDetails" : "exerciseDetails";
    }

    /**
     * 언어별 컬렉션 가져오기
     */
    private MongoCollection<Document> getCollection(String language) {
        MongoDatabase database = MongoConn.getDatabase();
        return database.getCollection(getCollectionName(language));
    }

    public boolean insert(ExerciseDetailDTO exerciseDetail) {
        return insert(exerciseDetail, "en");
    }

    /**
     * Insert exercise detail into language-specific collection
     * @param exerciseDetail Exercise detail to insert
     * @param language Language code ("en" or "ko")
     * @return true if successful
     */
    public boolean insert(ExerciseDetailDTO exerciseDetail, String language) {
        try {
            MongoCollection<Document> col = getCollection(language);
            Document doc = new Document()
                    .append("_id", exerciseDetail.getId())
                    .append("name", exerciseDetail.getName())
                    .append("category", exerciseDetail.getCategory())
                    .append("equipment", exerciseDetail.getEquipment())
                    .append("force", exerciseDetail.getForce())
                    .append("images", exerciseDetail.getImages())
                    .append("instructions", exerciseDetail.getInstructions())
                    .append("level", exerciseDetail.getLevel())
                    .append("mechanic", exerciseDetail.getMechanic())
                    .append("primaryMuscles", exerciseDetail.getPrimaryMuscles())
                    .append("secondaryMuscles", exerciseDetail.getSecondaryMuscles());

            col.insertOne(doc);
            return true;
        } catch (Exception e) {
            System.err.println("Error inserting exercise detail (" + language + "): " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean insertMany(List<ExerciseDetailDTO> exerciseDetails) {
        return insertMany(exerciseDetails, "en");
    }

    /**
     * Insert multiple exercise details into language-specific collection
     * @param exerciseDetails List of exercise details to insert
     * @param language Language code ("en" or "ko")
     * @return true if successful
     */
    public boolean insertMany(List<ExerciseDetailDTO> exerciseDetails, String language) {
        try {
            MongoCollection<Document> col = getCollection(language);
            List<Document> documents = new ArrayList<>();
            for (ExerciseDetailDTO exerciseDetail : exerciseDetails) {
                Document doc = new Document()
                        .append("_id", exerciseDetail.getId())
                        .append("name", exerciseDetail.getName())
                        .append("category", exerciseDetail.getCategory())
                        .append("equipment", exerciseDetail.getEquipment())
                        .append("force", exerciseDetail.getForce())
                        .append("images", exerciseDetail.getImages())
                        .append("instructions", exerciseDetail.getInstructions())
                        .append("level", exerciseDetail.getLevel())
                        .append("mechanic", exerciseDetail.getMechanic())
                        .append("primaryMuscles", exerciseDetail.getPrimaryMuscles())
                        .append("secondaryMuscles", exerciseDetail.getSecondaryMuscles());
                documents.add(doc);
            }
            col.insertMany(documents);
            return true;
        } catch (Exception e) {
            System.err.println("Error inserting exercise details (" + language + "): " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public ExerciseDetailDTO findById(String id) {
        return findById(id, "en");  // 기본값: 영어
    }

    /**
     * ID로 운동 상세 정보 조회 (언어별)
     * String 타입과 ObjectId 타입 모두 지원
     */
    public ExerciseDetailDTO findById(String id, String language) {
        try {
            MongoCollection<Document> col = getCollection(language);

            // 1단계: String 타입으로 조회 시도
            Document doc = col.find(Filters.eq("_id", id)).first();

            // 2단계: 못 찾았고 ID가 ObjectId 형식이면, ObjectId 타입으로 조회 시도
            if (doc == null && ObjectId.isValid(id)) {
                System.out.println("[DEBUG] String _id not found, trying ObjectId for: " + id);
                doc = col.find(Filters.eq("_id", new ObjectId(id))).first();
                if (doc != null) {
                    System.out.println("[SUCCESS] Found with ObjectId type");
                }
            }

            return documentToDTO(doc);
        } catch (Exception e) {
            System.err.println("Error finding exercise detail by id (" + language + "): " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public List<ExerciseDetailDTO> findAll() {
        return findAll("en");  // 기본값: 영어
    }

    /**
     * 모든 운동 상세 정보 조회 (언어별)
     */
    public List<ExerciseDetailDTO> findAll(String language) {
        List<ExerciseDetailDTO> exerciseDetails = new ArrayList<>();
        try {
            MongoCollection<Document> col = getCollection(language);
            for (Document doc : col.find()) {
                ExerciseDetailDTO exerciseDetail = documentToDTO(doc);
                if (exerciseDetail != null) {
                    exerciseDetails.add(exerciseDetail);
                }
            }
        } catch (Exception e) {
            System.err.println("Error finding all exercise details (" + language + "): " + e.getMessage());
            e.printStackTrace();
        }
        return exerciseDetails;
    }

    public boolean update(ExerciseDetailDTO exerciseDetail) {
        return update(exerciseDetail, "en");
    }

    /**
     * Update exercise detail in language-specific collection
     * @param exerciseDetail Exercise detail to update
     * @param language Language code ("en" or "ko")
     * @return true if successful
     */
    public boolean update(ExerciseDetailDTO exerciseDetail, String language) {
        try {
            MongoCollection<Document> col = getCollection(language);
            Document doc = new Document()
                    .append("name", exerciseDetail.getName())
                    .append("category", exerciseDetail.getCategory())
                    .append("equipment", exerciseDetail.getEquipment())
                    .append("force", exerciseDetail.getForce())
                    .append("images", exerciseDetail.getImages())
                    .append("instructions", exerciseDetail.getInstructions())
                    .append("level", exerciseDetail.getLevel())
                    .append("mechanic", exerciseDetail.getMechanic())
                    .append("primaryMuscles", exerciseDetail.getPrimaryMuscles())
                    .append("secondaryMuscles", exerciseDetail.getSecondaryMuscles());

            col.updateOne(Filters.eq("_id", exerciseDetail.getId()), new Document("$set", doc));
            return true;
        } catch (Exception e) {
            System.err.println("Error updating exercise detail (" + language + "): " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(String id) {
        return delete(id, "en");
    }

    /**
     * Delete exercise detail from language-specific collection
     * @param id Exercise detail ID to delete
     * @param language Language code ("en" or "ko")
     * @return true if successful
     */
    public boolean delete(String id, String language) {
        try {
            MongoCollection<Document> col = getCollection(language);
            col.deleteOne(Filters.eq("_id", id));
            return true;
        } catch (Exception e) {
            System.err.println("Error deleting exercise detail (" + language + "): " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public long count() {
        return count("en");  // 기본값: 영어
    }

    /**
     * 운동 상세 정보 개수 조회 (언어별)
     */
    public long count(String language) {
        try {
            MongoCollection<Document> col = getCollection(language);
            return col.countDocuments();
        } catch (Exception e) {
            System.err.println("Error counting exercise details (" + language + "): " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    public boolean deleteAll() {
        return deleteAll("en");
    }

    /**
     * Delete all exercise details from language-specific collection
     * CRITICAL: This protects Korean data by deleting only the specified language collection
     * @param language Language code ("en" or "ko")
     * @return true if successful
     */
    public boolean deleteAll(String language) {
        try {
            MongoCollection<Document> col = getCollection(language);
            col.deleteMany(new Document());
            System.out.println("Deleted all exercise details from " + language + " collection");
            return true;
        } catch (Exception e) {
            System.err.println("Error deleting all exercise details (" + language + "): " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    private ExerciseDetailDTO documentToDTO(Document doc) {
        if (doc == null) {
            return null;
        }

        ExerciseDetailDTO exerciseDetail = new ExerciseDetailDTO();
        // _id는 ObjectId 또는 String일 수 있으므로 안전하게 처리
        Object idObj = doc.get("_id");
        if (idObj != null) {
            exerciseDetail.setId(idObj.toString());
        }
        exerciseDetail.setName(doc.getString("name"));
        exerciseDetail.setCategory(doc.getString("category"));
        exerciseDetail.setEquipment(doc.getString("equipment"));
        exerciseDetail.setForce(doc.getString("force"));
        exerciseDetail.setImages((List<String>) doc.get("images"));
        exerciseDetail.setInstructions((List<String>) doc.get("instructions"));
        exerciseDetail.setLevel(doc.getString("level"));
        exerciseDetail.setMechanic(doc.getString("mechanic"));
        exerciseDetail.setPrimaryMuscles((List<String>) doc.get("primaryMuscles"));
        exerciseDetail.setSecondaryMuscles((List<String>) doc.get("secondaryMuscles"));

        return exerciseDetail;
    }
}
