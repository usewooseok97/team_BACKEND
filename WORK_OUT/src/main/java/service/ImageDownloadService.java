package service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 이미지 다운로드 및 저장 서비스
 * 외부 이미지를 서버에 다운로드하여 로컬에서 제공
 */
public class ImageDownloadService {
    private static ImageDownloadService instance = new ImageDownloadService();

    // 이미지 저장 디렉토리 (webapp/images/exercises/)
    private static final String IMAGE_DIR = "images/exercises";

    private ImageDownloadService() {
    }

    public static ImageDownloadService getInstance() {
        return instance;
    }

    /**
     * 외부 이미지 URL을 다운로드하여 서버에 저장
     * @param imageUrl 원본 이미지 URL
     * @param exerciseId 운동 ID (파일명에 사용)
     * @param index 이미지 인덱스 (0, 1, ...)
     * @param webappPath 웹앱 실제 경로 (ServletContext.getRealPath("/"))
     * @return 저장된 이미지의 상대 경로 (예: "images/exercises/bench-press_0.jpg")
     */
    public String downloadAndSaveImage(String imageUrl, String exerciseId, int index, String webappPath) {
        try {
            // URL 정규화
            String fullUrl = imageUrl;
            if (!imageUrl.startsWith("http://") && !imageUrl.startsWith("https://")) {
                fullUrl = "https://" + imageUrl;
            }

            // 이미지 디렉토리 생성
            File imageDirectory = new File(webappPath, IMAGE_DIR);
            if (!imageDirectory.exists()) {
                imageDirectory.mkdirs();
            }

            // 파일 확장자 추출
            String extension = getFileExtension(fullUrl);
            if (extension == null || extension.isEmpty()) {
                extension = "jpg"; // 기본값
            }

            // 안전한 파일명 생성
            String safeExerciseId = exerciseId.replaceAll("[^a-zA-Z0-9-]", "_");
            String fileName = safeExerciseId + "_" + index + "." + extension;
            File outputFile = new File(imageDirectory, fileName);

            // 이미 파일이 존재하면 재다운로드하지 않음
            if (outputFile.exists()) {
                System.out.println("Image already exists: " + fileName);
                return IMAGE_DIR + "/" + fileName;
            }

            // 이미지 다운로드
            URL url = new URL(fullUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36");
            connection.setRequestProperty("Referer", "https://www.google.com");
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                FileOutputStream outputStream = new FileOutputStream(outputFile);

                byte[] buffer = new byte[4096];
                int bytesRead;
                long totalBytes = 0;

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                    totalBytes += bytesRead;

                    // 파일 크기 제한 (10MB)
                    if (totalBytes > 10 * 1024 * 1024) {
                        System.err.println("Image too large, skipping: " + fullUrl);
                        inputStream.close();
                        outputStream.close();
                        outputFile.delete();
                        return null;
                    }
                }

                inputStream.close();
                outputStream.close();

                System.out.println("Downloaded image: " + fileName + " (" + totalBytes + " bytes)");
                return IMAGE_DIR + "/" + fileName;
            } else {
                System.err.println("Failed to download image: " + fullUrl + " (HTTP " + responseCode + ")");
                return null;
            }
        } catch (Exception e) {
            System.err.println("Error downloading image: " + imageUrl);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * URL에서 파일 확장자 추출
     */
    private String getFileExtension(String url) {
        try {
            // URL에서 파일명 부분 추출
            String path = new URL(url).getPath();
            int lastDotIndex = path.lastIndexOf('.');
            int lastSlashIndex = path.lastIndexOf('/');

            if (lastDotIndex > lastSlashIndex && lastDotIndex < path.length() - 1) {
                String ext = path.substring(lastDotIndex + 1).toLowerCase();
                // 쿼리 파라미터 제거
                int questionMarkIndex = ext.indexOf('?');
                if (questionMarkIndex > 0) {
                    ext = ext.substring(0, questionMarkIndex);
                }
                return ext;
            }
        } catch (Exception e) {
            System.err.println("Error extracting extension from URL: " + url);
        }
        return "jpg"; // 기본값
    }

    /**
     * 저장된 이미지 파일 삭제
     */
    public boolean deleteImage(String imagePath, String webappPath) {
        try {
            File imageFile = new File(webappPath, imagePath);
            if (imageFile.exists()) {
                return imageFile.delete();
            }
            return false;
        } catch (Exception e) {
            System.err.println("Error deleting image: " + imagePath);
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 특정 운동의 모든 이미지 삭제
     */
    public void deleteExerciseImages(String exerciseId, String webappPath) {
        try {
            File imageDirectory = new File(webappPath, IMAGE_DIR);
            String safeExerciseId = exerciseId.replaceAll("[^a-zA-Z0-9-]", "_");

            File[] files = imageDirectory.listFiles((dir, name) ->
                name.startsWith(safeExerciseId + "_"));

            if (files != null) {
                for (File file : files) {
                    file.delete();
                    System.out.println("Deleted image: " + file.getName());
                }
            }
        } catch (Exception e) {
            System.err.println("Error deleting images for exercise: " + exerciseId);
            e.printStackTrace();
        }
    }
}
