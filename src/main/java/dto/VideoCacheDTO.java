package dto;

import java.util.Date;
import java.util.List;

public class VideoCacheDTO {
    private String id;
    private String searchQuery;
    private List<YouTubeVideoDTO> videos;
    private Date lastUpdated;
    private Date createdAt;

    public VideoCacheDTO() {
    }

    public VideoCacheDTO(String id, String searchQuery, List<YouTubeVideoDTO> videos,
                         Date lastUpdated, Date createdAt) {
        this.id = id;
        this.searchQuery = searchQuery;
        this.videos = videos;
        this.lastUpdated = lastUpdated;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public List<YouTubeVideoDTO> getVideos() {
        return videos;
    }

    public void setVideos(List<YouTubeVideoDTO> videos) {
        this.videos = videos;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "VideoCacheDTO{" +
                "id='" + id + '\'' +
                ", searchQuery='" + searchQuery + '\'' +
                ", videosCount=" + (videos != null ? videos.size() : 0) +
                ", lastUpdated=" + lastUpdated +
                ", createdAt=" + createdAt +
                '}';
    }
}
