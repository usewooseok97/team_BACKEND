package dto;

import java.util.List;

/**
 * Images 컬렉션의 데이터를 담는 DTO
 */
public class ImagesDTO {
    private String id;   // 운동 ID
    private List<String> images;  // 이미지 경로 리스트

    // 기본 생성자
    public ImagesDTO() {
    }

    // 전체 생성자
    public ImagesDTO(String id, List<String> images) {
        this.id = id;
        this.images = images;
    }

    // Getter & Setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return "ImagesDTO{" +
                "id='" + id + '\'' +
                ", images=" + images +
                '}';
    }
}
