package dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 고객센터 문의 게시글을 표현하는 DTO 클래스
 */
public class InquiryDTO {
    private String id;              // 게시글 ID (Primary Key)
    private String title;           // 제목
    private String content;         // 내용
    private String author;          // 작성자 username
    private String authorName;      // 작성자 이름
    private LocalDateTime createdAt; // 작성일시
    private int viewCount;          // 조회수
    private String status;          // 상태 ("답변대기" / "답변완료")
    private List<ReplyDTO> replies; // 답변 목록
    private List<CommentDTO> comments; // 게시글 댓글 목록

    public InquiryDTO() {
        this.viewCount = 0;
        this.status = "답변대기";
        this.replies = new ArrayList<>();
        this.comments = new ArrayList<>();
        this.createdAt = LocalDateTime.now();
    }

    public InquiryDTO(String title, String content, String author, String authorName) {
        this();
        this.title = title;
        this.content = content;
        this.author = author;
        this.authorName = authorName;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ReplyDTO> getReplies() {
        return replies;
    }

    public void setReplies(List<ReplyDTO> replies) {
        this.replies = replies;
    }

    public List<CommentDTO> getComments() {
        return comments;
    }

    public void setComments(List<CommentDTO> comments) {
        this.comments = comments;
    }

    /**
     * 답변 추가
     */
    public void addReply(ReplyDTO reply) {
        if (this.replies == null) {
            this.replies = new ArrayList<>();
        }
        this.replies.add(reply);
        this.status = "답변완료";
    }

    /**
     * 게시글 댓글 추가
     */
    public void addComment(CommentDTO comment) {
        if (this.comments == null) {
            this.comments = new ArrayList<>();
        }
        this.comments.add(comment);
    }

    /**
     * 답변 내부 클래스
     */
    public static class ReplyDTO {
        private String id;              // 답변 ID
        private String content;         // 답변 내용
        private String author;          // 답변 작성자 username
        private String authorName;      // 답변 작성자 이름
        private LocalDateTime createdAt; // 답변 작성일시
        private List<CommentDTO> comments; // 댓글 목록

        public ReplyDTO() {
            this.createdAt = LocalDateTime.now();
            this.comments = new ArrayList<>();
        }

        public ReplyDTO(String content, String author, String authorName) {
            this();
            this.content = content;
            this.author = author;
            this.authorName = authorName;
        }

        // Getters and Setters
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getAuthorName() {
            return authorName;
        }

        public void setAuthorName(String authorName) {
            this.authorName = authorName;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
        }

        public List<CommentDTO> getComments() {
            return comments;
        }

        public void setComments(List<CommentDTO> comments) {
            this.comments = comments;
        }

        /**
         * 댓글 추가
         */
        public void addComment(CommentDTO comment) {
            if (this.comments == null) {
                this.comments = new ArrayList<>();
            }
            this.comments.add(comment);
        }
    }

    /**
     * 댓글 내부 클래스 (답변에 대한 댓글)
     */
    public static class CommentDTO {
        private String id;              // 댓글 ID
        private String content;         // 댓글 내용
        private String author;          // 댓글 작성자 username
        private String authorName;      // 댓글 작성자 이름
        private LocalDateTime createdAt; // 댓글 작성일시

        public CommentDTO() {
            this.createdAt = LocalDateTime.now();
        }

        public CommentDTO(String content, String author, String authorName) {
            this();
            this.content = content;
            this.author = author;
            this.authorName = authorName;
        }

        // Getters and Setters
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getAuthorName() {
            return authorName;
        }

        public void setAuthorName(String authorName) {
            this.authorName = authorName;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
        }
    }

    @Override
    public String toString() {
        return "InquiryDTO{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", status='" + status + '\'' +
                ", viewCount=" + viewCount +
                ", createdAt=" + createdAt +
                '}';
    }
}

