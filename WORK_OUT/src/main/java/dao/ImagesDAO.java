package dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import mongoutil.MongoConn;
import dto.ImagesDTO;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

/**
 * Images 컬렉션에 대한 DAO
 */
public class ImagesDAO {
    private static final String COLLECTION_NAME = "images";
    private static ImagesDAO instance = new ImagesDAO();
    private MongoCollection<Document> collection;

    private ImagesDAO() {
        MongoDatabase database = MongoConn.getDatabase();
        collection = database.getCollection(COLLECTION_NAME);
    }

    public static ImagesDAO getInstance() {
        return instance;
    }

    /**
     * 이미지 데이터 삽입
     */
    public boolean insert(ImagesDTO images) {
        try {
            Document doc = new Document()
                    .append("id", images.getId())
                    .append("images", images.getImages());
            collection.insertOne(doc);
            return true;
        } catch (Exception e) {
            System.err.println("Error inserting images: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * ID로 이미지 조회
     */
    public ImagesDTO findById(String id) {
        try {
            Bson filter = Filters.eq("id", id);
            Document doc = collection.find(filter).first();
            return doc != null ? documentToDTO(doc) : null;
        } catch (Exception e) {
            System.err.println("Error finding images by id: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 이미지 데이터 업데이트
     */
    public boolean update(String id, ImagesDTO images) {
        try {
            Bson filter = Filters.eq("id", id);
            Bson updates = Updates.set("images", images.getImages());
            collection.updateOne(filter, updates);
            return true;
        } catch (Exception e) {
            System.err.println("Error updating images: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * ID로 이미지 삭제
     */
    public boolean deleteById(String id) {
        try {
            Bson filter = Filters.eq("id", id);
            collection.deleteOne(filter);
            return true;
        } catch (Exception e) {
            System.err.println("Error deleting images: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 모든 이미지 데이터 조회
     */
    public List<ImagesDTO> findAll() {
        List<ImagesDTO> imagesList = new ArrayList<>();
        try {
            for (Document doc : collection.find()) {
                imagesList.add(documentToDTO(doc));
            }
        } catch (Exception e) {
            System.err.println("Error finding all images: " + e.getMessage());
            e.printStackTrace();
        }
        return imagesList;
    }

    /**
     * 모든 이미지 데이터 삭제
     */
    public boolean deleteAll() {
        try {
            collection.deleteMany(new Document());
            return true;
        } catch (Exception e) {
            System.err.println("Error deleting all images: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 이미지가 존재하는지 확인
     */
    public boolean existsById(String id) {
        try {
            Bson filter = Filters.eq("id", id);
            return collection.countDocuments(filter) > 0;
        } catch (Exception e) {
            System.err.println("Error checking images existence: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Document를 ImagesDTO로 변환
     */
    private ImagesDTO documentToDTO(Document doc) {
        ImagesDTO images = new ImagesDTO();
        images.setId(doc.getString("id"));
        images.setImages((List<String>) doc.get("images"));
        return images;
    }
}
