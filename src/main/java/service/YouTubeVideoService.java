package service;

import dao.VideoCacheDAO;
import dto.YouTubeVideoDTO;
import dto.VideoCacheDTO;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class YouTubeVideoService {
    private static YouTubeVideoService instance = new YouTubeVideoService();
    private VideoCacheDAO videoCacheDAO;

    // RapidAPI YouTube 환경변수
    private static final String API_KEY = getEnvOrDefault("RAPIDAPI_KEY", "");
    private static final String API_HOST = getEnvOrDefault("YOUTUBE_RAPIDAPI_HOST", "");
    private static final String BASE_URL = getEnvOrDefault("YOUTUBE_API_BASE_URL",
            "https://youtube138.p.rapidapi.com");

    // Groq AI KEY
    private static final String GROQ_API_KEY = System.getenv("GROQ_API_KEY");

    private static String getEnvOrDefault(String key, String defaultValue) {
        String value = System.getenv(key);
        return (value != null && !value.isEmpty()) ? value : defaultValue;
    }

    private YouTubeVideoService() {
        videoCacheDAO = VideoCacheDAO.getInstance();
    }

    public static YouTubeVideoService getInstance() {
        return instance;
    }

    /**
     * ====================================================
     *  AI 기반 검색어 생성 (Groq LLaMA-3.3 70B 사용)
     * ====================================================
     */
    public String generateAIQuery(String exerciseName, String equipment) {
        try {
            if (GROQ_API_KEY == null || GROQ_API_KEY.isEmpty()) {
                System.err.println("Groq API Key Missing — Using fallback query");
                return exerciseName + " exercise proper form gym tutorial";
            }

            String prompt =
                    "Generate a highly accurate YouTube search query for an exercise workout tutorial. "
                            + "It must return proper form, step-by-step, slow motion, and instruction videos. "
                            + "Exercise name: " + exerciseName
                            + ", Equipment: " + equipment
                            + ". Make sure the search query avoids music videos or unrelated content.";

            JSONObject body = new JSONObject();
            body.put("model", "llama-3.3-70b-versatile");

            JSONArray messages = new JSONArray()
                    .put(new JSONObject()
                            .put("role", "user")
                            .put("content", prompt));

            body.put("messages", messages);
            body.put("temperature", 0.2);
            body.put("max_tokens", 25);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.groq.com/openai/v1/chat/completions"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + GROQ_API_KEY)
                    .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
                    .build();

            HttpResponse<String> response =
                    HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            JSONObject json = new JSONObject(response.body());

            return json.getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content")
                    .trim();

        } catch (Exception e) {
            System.out.println("AI Query Generation Failed. Using fallback.");
            return exerciseName + " proper form gym tutorial";
        }
    }

    /**
     * 검색어 결정 → AI 기반 버전
     */
    public String determineSearchQuery(String equipment, String exerciseName) {
        return generateAIQuery(exerciseName, equipment);
    }

    /**
     * ====================================================
     *  메인: 캐시 → API → 저장 → 반환
     * ====================================================
     */
    public List<YouTubeVideoDTO> getVideos(String query, int limit) {
        try {
            VideoCacheDTO cache = videoCacheDAO.findBySearchQuery(query);

            if (cache != null && !isCacheExpired(cache, 24)) {
                System.out.println("Returning cached videos for query: " + query);
                return cache.getVideos();
            }

            System.out.println("Fetching fresh videos from API for query: " + query);
            List<YouTubeVideoDTO> videos = fetchFromAPI(query, limit);

            if (!videos.isEmpty()) saveToCache(query, videos);

            return videos;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * YouTube API 호출 (RapidAPI)
     */
    private List<YouTubeVideoDTO> fetchFromAPI(String query, int limit) {
        List<YouTubeVideoDTO> videos = new ArrayList<>();

        try {
            if (API_KEY == null || API_KEY.isEmpty()) {
                System.err.println("RAPIDAPI_KEY not set!");
                return videos;
            }

            String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
            String requestUrl = BASE_URL + "?q=" + encodedQuery + "&hl=en&gl=US";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(requestUrl))
                    .header("x-rapidapi-key", API_KEY)
                    .header("x-rapidapi-host", API_HOST)
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> response =
                    HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            JSONObject jsonResponse = new JSONObject(response.body());
            JSONArray contentsArray = jsonResponse.optJSONArray("contents");

            if (contentsArray != null) {
                int count = 0;

                for (int i = 0; i < contentsArray.length() && count < limit; i++) {
                    JSONObject item = contentsArray.getJSONObject(i);

                    if ("video".equals(item.optString("type"))) {
                        JSONObject videoJson = item.optJSONObject("video");
                        if (videoJson != null) {
                            YouTubeVideoDTO dto = jsonToDTO(videoJson);
                            if (dto != null && dto.getVideoId() != null && !dto.getVideoId().isEmpty()) {
                                videos.add(dto);
                                count++;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("YouTube API Error: " + e.getMessage());
            e.printStackTrace();
        }

        return videos;
    }

    private boolean isCacheExpired(VideoCacheDTO cache, int hours) {
        if (cache == null || cache.getLastUpdated() == null) return true;

        long diff = System.currentTimeMillis() - cache.getLastUpdated().getTime();
        long hoursDiff = diff / (1000 * 60 * 60);

        return hoursDiff >= hours;
    }

    private void saveToCache(String query, List<YouTubeVideoDTO> videos) {
        try {
            Date now = new Date();
            VideoCacheDTO cache = new VideoCacheDTO();

            cache.setSearchQuery(query);
            cache.setVideos(videos);
            cache.setLastUpdated(now);

            VideoCacheDTO existing = videoCacheDAO.findBySearchQuery(query);
            cache.setCreatedAt(existing == null ? now : existing.getCreatedAt());

            videoCacheDAO.insertOrUpdate(cache);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private YouTubeVideoDTO jsonToDTO(JSONObject json) {
        try {
            YouTubeVideoDTO v = new YouTubeVideoDTO();

            v.setVideoId(json.optString("videoId", ""));
            v.setTitle(json.optString("title", ""));
            v.setPublishedTime(json.optString("publishedTimeText", ""));
            v.setLengthSeconds(json.optInt("lengthSeconds", 0));

            JSONArray thumbs = json.optJSONArray("thumbnails");
            if (thumbs != null && thumbs.length() > 0) {
                JSONObject t = thumbs.getJSONObject(thumbs.length() - 1);
                v.setThumbnailUrl(t.optString("url", ""));
            }

            JSONObject author = json.optJSONObject("author");
            if (author != null) {
                v.setChannelName(author.optString("title", ""));
                v.setChannelId(author.optString("channelId", ""));

                JSONArray avatars = author.optJSONArray("avatar");
                if (avatars != null && avatars.length() > 0) {
                    v.setChannelAvatar(avatars.getJSONObject(0).optString("url", ""));
                }
            }

            return v;

        } catch (Exception e) {
            return null;
        }
    }
}
