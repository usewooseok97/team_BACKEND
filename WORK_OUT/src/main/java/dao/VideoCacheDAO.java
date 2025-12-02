package dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import dto.YouTubeVideoDTO;
import dto.VideoCacheDTO;
import mongoutil.MongoConn;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VideoCacheDAO {
    private static VideoCacheDAO instance = new VideoCacheDAO();
    private MongoCollection<Document> collection;

    private VideoCacheDAO() {
        try {
            MongoDatabase database = MongoConn.getDatabase();
            collection = database.getCollection("video_cache");
            System.out.println("VideoCacheDAO initialized successfully");
        } catch (Exception e) {
            System.err.println("VideoCacheDAO initialization failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static VideoCacheDAO getInstance() {
        return instance;
    }

    /**
     * Find cached videos by search query
     */
    public VideoCacheDTO findBySearchQuery(String searchQuery) {
        try {
            Document doc = collection.find(Filters.eq("searchQuery", searchQuery)).first();
            return documentToCache(doc);
        } catch (Exception e) {
            System.err.println("Error finding video cache by search query: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Insert or update video cache (upsert)
     */
    public boolean insertOrUpdate(VideoCacheDTO cache) {
        try {
            Document filter = new Document("searchQuery", cache.getSearchQuery());
            Document update = new Document("$set", cacheToDocument(cache));
            UpdateOptions options = new UpdateOptions().upsert(true);

            collection.updateOne(filter, update, options);
            System.out.println("Video cache updated for query: " + cache.getSearchQuery());
            return true;
        } catch (Exception e) {
            System.err.println("Error inserting/updating video cache: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Delete video cache by search query
     */
    public boolean deleteBySearchQuery(String searchQuery) {
        try {
            collection.deleteOne(Filters.eq("searchQuery", searchQuery));
            System.out.println("Video cache deleted for query: " + searchQuery);
            return true;
        } catch (Exception e) {
            System.err.println("Error deleting video cache: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Delete old caches (older than specified hours)
     */
    public boolean deleteOldCache(int hours) {
        try {
            long cutoffTime = System.currentTimeMillis() - (hours * 60 * 60 * 1000L);
            Date cutoffDate = new Date(cutoffTime);

            collection.deleteMany(Filters.lt("lastUpdated", cutoffDate));
            System.out.println("Old video caches deleted (older than " + hours + " hours)");
            return true;
        } catch (Exception e) {
            System.err.println("Error deleting old video caches: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Convert VideoCacheDTO to MongoDB Document
     */
    private Document cacheToDocument(VideoCacheDTO cache) {
        Document doc = new Document()
                .append("searchQuery", cache.getSearchQuery())
                .append("lastUpdated", cache.getLastUpdated())
                .append("createdAt", cache.getCreatedAt());

        // Convert videos list to documents list
        List<Document> videoDocs = new ArrayList<>();
        if (cache.getVideos() != null) {
            for (YouTubeVideoDTO video : cache.getVideos()) {
                Document videoDoc = new Document()
                        .append("videoId", video.getVideoId())
                        .append("title", video.getTitle())
                        .append("thumbnailUrl", video.getThumbnailUrl())
                        .append("channelName", video.getChannelName())
                        .append("channelId", video.getChannelId())
                        .append("channelAvatar", video.getChannelAvatar())
                        .append("publishedTime", video.getPublishedTime())
                        .append("lengthSeconds", video.getLengthSeconds());
                videoDocs.add(videoDoc);
            }
        }
        doc.append("videos", videoDocs);

        return doc;
    }

    /**
     * Convert MongoDB Document to VideoCacheDTO
     */
    private VideoCacheDTO documentToCache(Document doc) {
        if (doc == null) {
            return null;
        }

        VideoCacheDTO cache = new VideoCacheDTO();
        cache.setId(doc.getObjectId("_id") != null ? doc.getObjectId("_id").toString() : null);
        cache.setSearchQuery(doc.getString("searchQuery"));
        cache.setLastUpdated(doc.getDate("lastUpdated"));
        cache.setCreatedAt(doc.getDate("createdAt"));

        // Convert videos documents to YouTubeVideoDTO list
        List<YouTubeVideoDTO> videos = new ArrayList<>();
        List<Document> videoDocs = (List<Document>) doc.get("videos");
        if (videoDocs != null) {
            for (Document videoDoc : videoDocs) {
                YouTubeVideoDTO video = new YouTubeVideoDTO();
                video.setVideoId(videoDoc.getString("videoId"));
                video.setTitle(videoDoc.getString("title"));
                video.setThumbnailUrl(videoDoc.getString("thumbnailUrl"));
                video.setChannelName(videoDoc.getString("channelName"));
                video.setChannelId(videoDoc.getString("channelId"));
                video.setChannelAvatar(videoDoc.getString("channelAvatar"));
                video.setPublishedTime(videoDoc.getString("publishedTime"));
                video.setLengthSeconds(videoDoc.getInteger("lengthSeconds", 0));
                videos.add(video);
            }
        }
        cache.setVideos(videos);

        return cache;
    }
}
