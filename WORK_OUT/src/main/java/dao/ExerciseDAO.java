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

    public boolean insert(ExerciseDTO exercise) {
        try {
            Document doc = new Document()
                    .append("id", exercise.getId())
                    .append("name", exercise.getName())
                    .append("primaryMuscles", exercise.getPrimaryMuscles())
                    .append("secondaryMuscles", exercise.getSecondaryMuscles())
                    .append("level", exercise.getLevel());

            collection.insertOne(doc);
            return true;
        } catch (Exception e) {
            System.err.println("Error inserting exercise: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean insertMany(List<ExerciseDTO> exercises) {
        try {
            List<Document> documents = new ArrayList<>();
            for (ExerciseDTO exercise : exercises) {
                Document doc = new Document()
                        .append("id", exercise.getId())
                        .append("name", exercise.getName())
                        .append("primaryMuscles", exercise.getPrimaryMuscles())
                        .append("secondaryMuscles", exercise.getSecondaryMuscles())
                        .append("level", exercise.getLevel());
                documents.add(doc);
            }
            collection.insertMany(documents);
            return true;
        } catch (Exception e) {
            System.err.println("Error inserting exercises: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public ExerciseDTO findById(String id) {
        try {
            Document doc = collection.find(Filters.eq("id", id)).first();
            return documentToDTO(doc);
        } catch (Exception e) {
            System.err.println("Error finding exercise by id: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public List<ExerciseDTO> findAll() {
        List<ExerciseDTO> exercises = new ArrayList<>();
        try {
            for (Document doc : collection.find()) {
                ExerciseDTO exercise = documentToDTO(doc);
                if (exercise != null) {
                    exercises.add(exercise);
                }
            }
        } catch (Exception e) {
            System.err.println("Error finding all exercises: " + e.getMessage());
            e.printStackTrace();
        }
        return exercises;
    }

    public List<ExerciseDTO> findByPrimaryMuscle(String muscle) {
        List<ExerciseDTO> exercises = new ArrayList<>();
        try {
            for (Document doc : collection.find(Filters.eq("primaryMuscles", muscle))) {
                ExerciseDTO exercise = documentToDTO(doc);
                if (exercise != null) {
                    exercises.add(exercise);
                }
            }
        } catch (Exception e) {
            System.err.println("Error finding exercises by primary muscle: " + e.getMessage());
            e.printStackTrace();
        }
        return exercises;
    }

    public List<ExerciseDTO> findByLevel(String level) {
        List<ExerciseDTO> exercises = new ArrayList<>();
        try {
            for (Document doc : collection.find(Filters.eq("level", level))) {
                ExerciseDTO exercise = documentToDTO(doc);
                if (exercise != null) {
                    exercises.add(exercise);
                }
            }
        } catch (Exception e) {
            System.err.println("Error finding exercises by level: " + e.getMessage());
            e.printStackTrace();
        }
        return exercises;
    }

    public List<ExerciseDTO> findByNameContaining(String keyword) {
        List<ExerciseDTO> exercises = new ArrayList<>();
        try {
            // 대소문자 구분 없이 부분 검색
            for (Document doc : collection.find(Filters.regex("name", ".*" + keyword + ".*", "i"))) {
                ExerciseDTO exercise = documentToDTO(doc);
                if (exercise != null) {
                    exercises.add(exercise);
                }
            }
        } catch (Exception e) {
            System.err.println("Error finding exercises by name: " + e.getMessage());
            e.printStackTrace();
        }
        return exercises;
    }

    public List<ExerciseDTO> findByMultipleFields(String keyword) {
        List<ExerciseDTO> exercises = new ArrayList<>();
        try {
            // name, primaryMuscles, secondaryMuscles, level 중 하나라도 일치하면 반환 (대소문자 구분 없이)
            org.bson.conversions.Bson filter = Filters.or(
                Filters.regex("name", ".*" + keyword + ".*", "i"),
                Filters.regex("primaryMuscles", ".*" + keyword + ".*", "i"),
                Filters.regex("secondaryMuscles", ".*" + keyword + ".*", "i"),
                Filters.regex("level", ".*" + keyword + ".*", "i")
            );

            for (Document doc : collection.find(filter)) {
                ExerciseDTO exercise = documentToDTO(doc);
                if (exercise != null) {
                    exercises.add(exercise);
                }
            }
        } catch (Exception e) {
            System.err.println("Error finding exercises by multiple fields: " + e.getMessage());
            e.printStackTrace();
        }
        return exercises;
    }

    public boolean update(ExerciseDTO exercise) {
        try {
            Document doc = new Document()
                    .append("name", exercise.getName())
                    .append("primaryMuscles", exercise.getPrimaryMuscles())
                    .append("secondaryMuscles", exercise.getSecondaryMuscles())
                    .append("level", exercise.getLevel());

            collection.updateOne(Filters.eq("id", exercise.getId()), new Document("$set", doc));
            return true;
        } catch (Exception e) {
            System.err.println("Error updating exercise: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(String id) {
        try {
            collection.deleteOne(Filters.eq("id", id));
            return true;
        } catch (Exception e) {
            System.err.println("Error deleting exercise: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public long count() {
        try {
            return collection.countDocuments();
        } catch (Exception e) {
            System.err.println("Error counting exercises: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    public boolean deleteAll() {
        try {
            collection.deleteMany(new Document());
            return true;
        } catch (Exception e) {
            System.err.println("Error deleting all exercises: " + e.getMessage());
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
        exercise.setId(doc.getString("id"));
        exercise.setName(doc.getString("name"));
        exercise.setPrimaryMuscles((List<String>) doc.get("primaryMuscles"));
        exercise.setSecondaryMuscles((List<String>) doc.get("secondaryMuscles"));
        // images는 images 테이블에서 별도 조회
        exercise.setLevel(doc.getString("level"));

        return exercise;
    }
}
