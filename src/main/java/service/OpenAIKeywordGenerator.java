package service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;

public class OpenAIKeywordGenerator {

    private static final String GROQ_API_KEY = System.getenv("GROQ_API_KEY");

    public static String generateKeyword(String prompt) throws Exception {
        if (GROQ_API_KEY == null || GROQ_API_KEY.isEmpty()) {
            System.err.println("Groq API Key is missing!");
            return "";
        }

        JSONObject json = new JSONObject();
        json.put("model", "llama-3.1-8b-instant");  
        JSONArray messages = new JSONArray();

        JSONObject userMsg = new JSONObject();
        userMsg.put("role", "user");
        userMsg.put("content", prompt);
        messages.put(userMsg);

        json.put("messages", messages);
        json.put("temperature", 0.5);
        json.put("max_tokens", 20);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.groq.com/openai/v1/chat/completions"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + GROQ_API_KEY)
                .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
                .build();

        HttpResponse<String> response =
                HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        JSONObject responseJson = new JSONObject(response.body());

        if (responseJson.has("choices")) {
            return responseJson
                    .getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content")
                    .trim();
        } else {
            System.err.println("Groq API Error: " + response.body());
            return "";
        }
    }
}
