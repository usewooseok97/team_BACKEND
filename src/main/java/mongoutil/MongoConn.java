package mongoutil;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoConn {

    // 환경변수에서 읽어오거나, 없으면 기본값 사용
    private static final String CONNECTION_STRING = getEnvOrDefault("MONGO_CONNECTION_STRING",
        "mongodb+srv://admin:admin@workoutcluster.snlejtj.mongodb.net/");

    // Define the database name to use
    private static final String DB_NAME = getEnvOrDefault("MONGO_DB_NAME", "WORKOUT_DB");

    private static MongoClient mongoClient = null;

    /**
     * 환경변수 값을 가져오되, 없으면 기본값을 반환
     */
    private static String getEnvOrDefault(String key, String defaultValue) {
        String value = System.getenv(key);
        return (value != null && !value.isEmpty()) ? value : defaultValue;
    }

    static {
        try {
            mongoClient = MongoClients.create(CONNECTION_STRING);
            // Log success message (optional)
            System.out.println("====================================");
            System.out.println("MongoDB Connected Successfully!");
            System.out.println("Connection String: " + CONNECTION_STRING);
            System.out.println("Database Name: " + DB_NAME);
            System.out.println("====================================");
        } catch (Exception e) {
            System.out.println("MongoDB 연결 실패!");
            e.printStackTrace();
        }
    }

    public static MongoDatabase getDatabase() {
        if (mongoClient == null) {
            throw new IllegalStateException("MongoDB client is not initialized");
        }
        return mongoClient.getDatabase(DB_NAME);
    }

    public static void close() {
        if (mongoClient != null) {
            mongoClient.close();
            System.out.println("MongoDB Connection Closed");
        }
    }
}