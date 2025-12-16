package dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import mongoutil.MongoConn;
import dto.ImagesDTO;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import util.LRUCache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Images 컬렉션에 대한 DAO
 * LRU 캐싱을 통한 성능 최적화 지원
 */
public class ImagesDAO {
    private static final String COLLECTION_NAME = "images";
    private static final int CACHE_SIZE = 500;
    private static ImagesDAO instance = new ImagesDAO();
    private MongoCollection<Document> collection;
    private LRUCache<String, ImagesDTO> imageCache;

    private ImagesDAO() {
        MongoDatabase database = MongoConn.getDatabase();
        collection = database.getCollection(COLLECTION_NAME);
        imageCache = new LRUCache<>(CACHE_SIZE);
        System.out.println("ImagesDAO initialized with LRU cache (size=" + CACHE_SIZE + ")");
    }

    public static ImagesDAO getInstance() {
        return instance;
    }

    /**
     * 캐시 키 생성 (ID + 언어 조합)
     */
    private String getCacheKey(String id, String language) {
        return id + "_" + language;
    }

    /**
     * 이미지 데이터 삽입
     */
    public boolean insert(ImagesDTO images) {
        return insert(images, "en");
    }

    /**
     * Insert image data using _id field
     * @param images Image data to insert
     * @param language Language code ("en" or "ko")
     * @return true if successful
     */
    public boolean insert(ImagesDTO images, String language) {
        try {
            Document doc = new Document()
                    .append("_id", images.getId())
                    .append("images", images.getImages());
            collection.insertOne(doc);
            return true;
        } catch (Exception e) {
            System.err.println("Error inserting images (" + language + "): " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * ID로 이미지 조회 (기본값: 영어)
     */
    public ImagesDTO findById(String id) {
        return findById(id, "en");
    }

    /**
     * ID로 이미지 조회 (_id 필드 사용)
     * String 타입과 ObjectId 타입 모두 지원
     * LRU 캐싱 적용
     */
    public ImagesDTO findById(String id, String language) {
        // 1단계: 캐시 확인
        String cacheKey = getCacheKey(id, language);
        ImagesDTO cached = imageCache.get(cacheKey);
        if (cached != null) {
            return cached;
        }

        // 2단계: 캐시 미스 - DB 조회
        try {
            // 2-1: String 타입으로 조회 시도
            Bson filter = Filters.eq("_id", id);
            Document doc = collection.find(filter).first();

            // 2-2: 못 찾았고 ID가 ObjectId 형식이면, ObjectId 타입으로 조회 시도
            if (doc == null && ObjectId.isValid(id)) {
                System.out.println("[DEBUG] String _id not found in images, trying ObjectId for: " + id);
                filter = Filters.eq("_id", new ObjectId(id));
                doc = collection.find(filter).first();
                if (doc != null) {
                    System.out.println("[SUCCESS] Found with ObjectId type in images");
                }
            }

            // 3단계: DB 조회 결과를 캐시에 저장
            ImagesDTO result = doc != null ? documentToDTO(doc) : null;
            if (result != null) {
                imageCache.put(cacheKey, result);
            }

            return result;
        } catch (Exception e) {
            System.err.println("Error finding images by _id (" + language + "): " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 여러 ID로 이미지 벌크 조회 (언어별)
     * MongoDB의 $in 연산자를 사용하여 한 번의 쿼리로 여러 이미지 조회
     * N+1 쿼리 문제 해결을 위한 메서드
     * LRU 캐싱 적용 - 캐시에 없는 항목만 DB 조회
     *
     * @param ids 운동 ID 목록
     * @param language 언어 코드 ("en" or "ko")
     * @return ID를 키로 하는 ImagesDTO 맵
     */
    public Map<String, ImagesDTO> findByIds(List<String> ids, String language) {
        Map<String, ImagesDTO> result = new HashMap<>();

        if (ids == null || ids.isEmpty()) {
            return result;
        }

        try {
            long startTime = System.currentTimeMillis();

            // 1단계: 캐시에서 먼저 조회
            List<String> uncachedIds = new ArrayList<>();
            int cacheHits = 0;
            for (String id : ids) {
                String cacheKey = getCacheKey(id, language);
                ImagesDTO cached = imageCache.get(cacheKey);
                if (cached != null) {
                    result.put(id, cached);
                    cacheHits++;
                } else {
                    uncachedIds.add(id);
                }
            }

            // 2단계: 캐시에 없는 ID들만 DB에서 조회
            if (!uncachedIds.isEmpty()) {
                // 2-1: String 타입 ID로 벌크 조회 ($in 연산자 사용)
                Bson filter = Filters.in("_id", uncachedIds);

                for (Document doc : collection.find(filter)) {
                    ImagesDTO imagesDTO = documentToDTO(doc);
                    if (imagesDTO != null && imagesDTO.getId() != null) {
                        result.put(imagesDTO.getId(), imagesDTO);
                        // 캐시에 저장
                        imageCache.put(getCacheKey(imagesDTO.getId(), language), imagesDTO);
                    }
                }

                // 2-2: String으로 못 찾은 ID들을 ObjectId로 재시도
                if (result.size() < ids.size()) {
                    List<String> notFoundIds = new ArrayList<>();
                    for (String id : uncachedIds) {
                        if (!result.containsKey(id) && ObjectId.isValid(id)) {
                            notFoundIds.add(id);
                        }
                    }

                    if (!notFoundIds.isEmpty()) {
                        System.out.println("[DEBUG] Retrying " + notFoundIds.size() + " IDs with ObjectId type in images");

                        List<ObjectId> objectIds = new ArrayList<>();
                        for (String id : notFoundIds) {
                            objectIds.add(new ObjectId(id));
                        }

                        Bson objectIdFilter = Filters.in("_id", objectIds);
                        for (Document doc : collection.find(objectIdFilter)) {
                            ImagesDTO imagesDTO = documentToDTO(doc);
                            if (imagesDTO != null && imagesDTO.getId() != null) {
                                result.put(imagesDTO.getId(), imagesDTO);
                                // 캐시에 저장
                                imageCache.put(getCacheKey(imagesDTO.getId(), language), imagesDTO);
                            }
                        }

                        System.out.println("[SUCCESS] Found " + result.size() + " total images after ObjectId retry");
                    }
                }
            }

            // 3단계: 성능 로깅 (캐시 히트 정보 포함)
            long duration = System.currentTimeMillis() - startTime;
            int dbQueries = uncachedIds.size();
            System.out.println(String.format(
                "[ImagesDAO] Bulk loaded %d images (%d from cache, %d from DB) for %d IDs in %dms (%s)",
                result.size(), cacheHits, dbQueries, ids.size(), duration, language
            ));

            return result;

        } catch (Exception e) {
            System.err.println("Error finding images by IDs (" + language + "): " + e.getMessage());
            e.printStackTrace();
            return result;
        }
    }

    /**
     * 이미지 데이터 업데이트
     */
    public boolean update(String id, ImagesDTO images) {
        return update(id, images, "en");
    }

    /**
     * Update image data using _id field
     * @param id Exercise ID
     * @param images Image data to update
     * @param language Language code ("en" or "ko")
     * @return true if successful
     */
    public boolean update(String id, ImagesDTO images, String language) {
        try {
            Bson filter = Filters.eq("_id", id);
            Bson updates = Updates.set("images", images.getImages());
            collection.updateOne(filter, updates);

            // 캐시 무효화
            String cacheKey = getCacheKey(id, language);
            imageCache.remove(cacheKey);

            return true;
        } catch (Exception e) {
            System.err.println("Error updating images (" + language + "): " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Document를 ImagesDTO로 변환
     */
    private ImagesDTO documentToDTO(Document doc) {
        ImagesDTO images = new ImagesDTO();
        // _id는 ObjectId 또는 String일 수 있으므로 안전하게 처리
        Object idObj = doc.get("_id");
        if (idObj != null) {
            images.setId(idObj.toString());
        }
        images.setImages((List<String>) doc.get("images"));
        return images;
    }
}
