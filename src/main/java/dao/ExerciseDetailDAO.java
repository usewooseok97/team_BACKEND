package dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import dto.ExerciseDetailDTO;
import mongoutil.MongoConn;
import org.bson.Document;

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

    public boolean insert(ExerciseDetailDTO exerciseDetail) {
        try {
            Document doc = new Document()
                    .append("id", exerciseDetail.getId())
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

            collection.insertOne(doc);
            return true;
        } catch (Exception e) {
            System.err.println("Error inserting exercise detail: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean insertMany(List<ExerciseDetailDTO> exerciseDetails) {
        try {
            List<Document> documents = new ArrayList<>();
            for (ExerciseDetailDTO exerciseDetail : exerciseDetails) {
                Document doc = new Document()
                        .append("id", exerciseDetail.getId())
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
            collection.insertMany(documents);
            return true;
        } catch (Exception e) {
            System.err.println("Error inserting exercise details: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public ExerciseDetailDTO findById(String id) {
        try {
            Document doc = collection.find(Filters.eq("id", id)).first();
            return documentToDTO(doc);
        } catch (Exception e) {
            System.err.println("Error finding exercise detail by id: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public List<ExerciseDetailDTO> findAll() {
        List<ExerciseDetailDTO> exerciseDetails = new ArrayList<>();
        try {
            for (Document doc : collection.find()) {
                ExerciseDetailDTO exerciseDetail = documentToDTO(doc);
                if (exerciseDetail != null) {
                    exerciseDetails.add(exerciseDetail);
                }
            }
        } catch (Exception e) {
            System.err.println("Error finding all exercise details: " + e.getMessage());
            e.printStackTrace();
        }
        return exerciseDetails;
    }

    public boolean update(ExerciseDetailDTO exerciseDetail) {
        try {
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

            collection.updateOne(Filters.eq("id", exerciseDetail.getId()), new Document("$set", doc));
            return true;
        } catch (Exception e) {
            System.err.println("Error updating exercise detail: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(String id) {
        try {
            collection.deleteOne(Filters.eq("id", id));
            return true;
        } catch (Exception e) {
            System.err.println("Error deleting exercise detail: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public long count() {
        try {
            return collection.countDocuments();
        } catch (Exception e) {
            System.err.println("Error counting exercise details: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    public boolean deleteAll() {
        try {
            collection.deleteMany(new Document());
            return true;
        } catch (Exception e) {
            System.err.println("Error deleting all exercise details: " + e.getMessage());
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
        exerciseDetail.setId(doc.getString("id"));
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
