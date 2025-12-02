package dto;

public class YouTubeVideoDTO {
    private String videoId;
    private String title;
    private String thumbnailUrl;
    private String channelName;
    private String channelId;
    private String channelAvatar;
    private String publishedTime;
    private int lengthSeconds;

    public YouTubeVideoDTO() {
    }

    public YouTubeVideoDTO(String videoId, String title, String thumbnailUrl,
                           String channelName, String channelId, String channelAvatar,
                           String publishedTime, int lengthSeconds) {
        this.videoId = videoId;
        this.title = title;
        this.thumbnailUrl = thumbnailUrl;
        this.channelName = channelName;
        this.channelId = channelId;
        this.channelAvatar = channelAvatar;
        this.publishedTime = publishedTime;
        this.lengthSeconds = lengthSeconds;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getChannelAvatar() {
        return channelAvatar;
    }

    public void setChannelAvatar(String channelAvatar) {
        this.channelAvatar = channelAvatar;
    }

    public String getPublishedTime() {
        return publishedTime;
    }

    public void setPublishedTime(String publishedTime) {
        this.publishedTime = publishedTime;
    }

    public int getLengthSeconds() {
        return lengthSeconds;
    }

    public void setLengthSeconds(int lengthSeconds) {
        this.lengthSeconds = lengthSeconds;
    }

    @Override
    public String toString() {
        return "YouTubeVideoDTO{" +
                "videoId='" + videoId + '\'' +
                ", title='" + title + '\'' +
                ", channelName='" + channelName + '\'' +
                ", publishedTime='" + publishedTime + '\'' +
                ", lengthSeconds=" + lengthSeconds +
                '}';
    }
}
