package dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.model.Sorts;
import dto.InquiryDTO;
import mongoutil.MongoConn;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 고객센터 문의 게시글을 관리하는 DAO 클래스 (MongoDB 기반)
 */
public class InquiryDAO {
    // 싱글톤 패턴
    private static InquiryDAO instance = new InquiryDAO();

    private MongoCollection<Document> inquiryCollection;

    private InquiryDAO() {
        MongoDatabase database = MongoConn.getDatabase();
        inquiryCollection = database.getCollection("customer_board");
    }

    public static InquiryDAO getInstance() {
        return instance;
    }

    /**
     * InquiryDTO를 MongoDB Document로 변환
     */
    private Document inquiryToDocument(InquiryDTO inquiry) {
        Document doc = new Document();
        if (inquiry.getId() != null) {
            doc.append("_id", new ObjectId(inquiry.getId()));
        }
        doc.append("title", inquiry.getTitle())
           .append("content", inquiry.getContent())
           .append("author", inquiry.getAuthor())
           .append("authorName", inquiry.getAuthorName())
           .append("viewCount", inquiry.getViewCount())
           .append("status", inquiry.getStatus() != null ? inquiry.getStatus() : "답변대기")
           .append("createdAt", Date.from(inquiry.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant()));

        // 게시글 댓글 목록 추가
        if (inquiry.getComments() != null && !inquiry.getComments().isEmpty()) {
            List<Document> commentDocs = new ArrayList<>();
            for (InquiryDTO.CommentDTO comment : inquiry.getComments()) {
                Document commentDoc = new Document();
                if (comment.getId() != null) {
                    commentDoc.append("_id", new ObjectId(comment.getId()));
                }
                commentDoc.append("content", comment.getContent())
                        .append("author", comment.getAuthor())
                        .append("authorName", comment.getAuthorName())
                        .append("createdAt", Date.from(comment.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant()));
                commentDocs.add(commentDoc);
            }
            doc.append("comments", commentDocs);
        }

        // 답변 목록 추가
        if (inquiry.getReplies() != null && !inquiry.getReplies().isEmpty()) {
            List<Document> replyDocs = new ArrayList<>();
            for (InquiryDTO.ReplyDTO reply : inquiry.getReplies()) {
                Document replyDoc = new Document();
                if (reply.getId() != null) {
                    replyDoc.append("_id", new ObjectId(reply.getId()));
                }
                replyDoc.append("content", reply.getContent())
                        .append("author", reply.getAuthor())
                        .append("authorName", reply.getAuthorName())
                        .append("createdAt", Date.from(reply.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant()));

                // 댓글 목록 추가
                if (reply.getComments() != null && !reply.getComments().isEmpty()) {
                    List<Document> commentDocs = new ArrayList<>();
                    for (InquiryDTO.CommentDTO comment : reply.getComments()) {
                        Document commentDoc = new Document();
                        if (comment.getId() != null) {
                            commentDoc.append("_id", new ObjectId(comment.getId()));
                        }
                        commentDoc.append("content", comment.getContent())
                                .append("author", comment.getAuthor())
                                .append("authorName", comment.getAuthorName())
                                .append("createdAt", Date.from(comment.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant()));
                        commentDocs.add(commentDoc);
                    }
                    replyDoc.append("comments", commentDocs);
                }

                replyDocs.add(replyDoc);
            }
            doc.append("replies", replyDocs);
        }

        return doc;
    }

    /**
     * MongoDB Document를 InquiryDTO로 변환
     */
    private InquiryDTO documentToInquiry(Document doc) {
        if (doc == null) return null;

        InquiryDTO inquiry = new InquiryDTO();
        inquiry.setId(doc.getObjectId("_id").toString());
        inquiry.setTitle(doc.getString("title"));
        inquiry.setContent(doc.getString("content"));
        inquiry.setAuthor(doc.getString("author"));
        inquiry.setAuthorName(doc.getString("authorName"));
        inquiry.setViewCount(doc.getInteger("viewCount", 0));
        inquiry.setStatus(doc.getString("status"));

        // 작성일시 변환
        if (doc.containsKey("createdAt")) {
            Date createdAt = doc.getDate("createdAt");
            inquiry.setCreatedAt(LocalDateTime.ofInstant(createdAt.toInstant(), ZoneId.systemDefault()));
        }

        // 게시글 댓글 목록 변환
        if (doc.containsKey("comments")) {
            @SuppressWarnings("unchecked")
            List<Document> commentDocs = (List<Document>) doc.get("comments");
            List<InquiryDTO.CommentDTO> comments = new ArrayList<>();
            if (commentDocs != null) {
                for (Document commentDoc : commentDocs) {
                    InquiryDTO.CommentDTO comment = new InquiryDTO.CommentDTO();
                    if (commentDoc.containsKey("_id")) {
                        comment.setId(commentDoc.getObjectId("_id").toString());
                    }
                    comment.setContent(commentDoc.getString("content"));
                    comment.setAuthor(commentDoc.getString("author"));
                    comment.setAuthorName(commentDoc.getString("authorName"));
                    if (commentDoc.containsKey("createdAt")) {
                        Date commentCreatedAt = commentDoc.getDate("createdAt");
                        comment.setCreatedAt(LocalDateTime.ofInstant(commentCreatedAt.toInstant(), ZoneId.systemDefault()));
                    }
                    comments.add(comment);
                }
            }
            inquiry.setComments(comments);
        }

        // 답변 목록 변환
        if (doc.containsKey("replies")) {
            @SuppressWarnings("unchecked")
            List<Document> replyDocs = (List<Document>) doc.get("replies");
            List<InquiryDTO.ReplyDTO> replies = new ArrayList<>();
            if (replyDocs != null) {
                for (Document replyDoc : replyDocs) {
                    InquiryDTO.ReplyDTO reply = new InquiryDTO.ReplyDTO();
                    if (replyDoc.containsKey("_id")) {
                        reply.setId(replyDoc.getObjectId("_id").toString());
                    }
                    reply.setContent(replyDoc.getString("content"));
                    reply.setAuthor(replyDoc.getString("author"));
                    reply.setAuthorName(replyDoc.getString("authorName"));
                    if (replyDoc.containsKey("createdAt")) {
                        Date replyCreatedAt = replyDoc.getDate("createdAt");
                        reply.setCreatedAt(LocalDateTime.ofInstant(replyCreatedAt.toInstant(), ZoneId.systemDefault()));
                    }

                    // 댓글 목록 변환
                    if (replyDoc.containsKey("comments")) {
                        @SuppressWarnings("unchecked")
                        List<Document> commentDocs = (List<Document>) replyDoc.get("comments");
                        List<InquiryDTO.CommentDTO> comments = new ArrayList<>();
                        if (commentDocs != null) {
                            for (Document commentDoc : commentDocs) {
                                InquiryDTO.CommentDTO comment = new InquiryDTO.CommentDTO();
                                if (commentDoc.containsKey("_id")) {
                                    comment.setId(commentDoc.getObjectId("_id").toString());
                                }
                                comment.setContent(commentDoc.getString("content"));
                                comment.setAuthor(commentDoc.getString("author"));
                                comment.setAuthorName(commentDoc.getString("authorName"));
                                if (commentDoc.containsKey("createdAt")) {
                                    Date commentCreatedAt = commentDoc.getDate("createdAt");
                                    comment.setCreatedAt(LocalDateTime.ofInstant(commentCreatedAt.toInstant(), ZoneId.systemDefault()));
                                }
                                comments.add(comment);
                            }
                        }
                        reply.setComments(comments);
                    }

                    replies.add(reply);
                }
            }
            inquiry.setReplies(replies);
        }

        return inquiry;
    }

    /**
     * 게시글 작성
     */
    public boolean create(InquiryDTO inquiry) {
        try {
            System.out.println("=== 게시글 작성 시작 ===");
            System.out.println("Title: " + inquiry.getTitle());
            System.out.println("Author: " + inquiry.getAuthor());

            Document doc = inquiryToDocument(inquiry);
            System.out.println("저장할 Document: " + doc.toJson());
            inquiryCollection.insertOne(doc);
            System.out.println("게시글 작성 성공!");
            return true;
        } catch (Exception e) {
            System.out.println("게시글 작성 오류 발생:");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * ID로 게시글 조회 (조회수 증가)
     */
    public InquiryDTO findById(String id) {
        try {
            Document doc = inquiryCollection.find(Filters.eq("_id", new ObjectId(id))).first();
            if (doc != null) {
                // 조회수 증가
                inquiryCollection.updateOne(
                    Filters.eq("_id", new ObjectId(id)),
                    Updates.inc("viewCount", 1)
                );
                // 조회수 증가된 값으로 다시 조회
                doc = inquiryCollection.find(Filters.eq("_id", new ObjectId(id))).first();
            }
            return documentToInquiry(doc);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * ID로 게시글 조회 (조회수 증가 없음)
     */
    public InquiryDTO findByIdWithoutIncrement(String id) {
        try {
            Document doc = inquiryCollection.find(Filters.eq("_id", new ObjectId(id))).first();
            return documentToInquiry(doc);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 게시글 목록 조회 (페이징)
     */
    public List<InquiryDTO> findAll(int page, int pageSize, String searchType, String searchKeyword) {
        List<InquiryDTO> inquiries = new ArrayList<>();
        try {
            com.mongodb.client.FindIterable<Document> findIterable;

            // 검색 조건 설정
            if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
                if ("title".equals(searchType)) {
                    findIterable = inquiryCollection.find(
                        Filters.regex("title", searchKeyword, "i")
                    ).sort(Sorts.descending("createdAt"));
                } else if ("content".equals(searchType)) {
                    findIterable = inquiryCollection.find(
                        Filters.regex("content", searchKeyword, "i")
                    ).sort(Sorts.descending("createdAt"));
                } else {
                    // 제목 또는 내용 검색
                    findIterable = inquiryCollection.find(
                        Filters.or(
                            Filters.regex("title", searchKeyword, "i"),
                            Filters.regex("content", searchKeyword, "i")
                        )
                    ).sort(Sorts.descending("createdAt"));
                }
            } else {
                findIterable = inquiryCollection.find().sort(Sorts.descending("createdAt"));
            }

            // 페이징 적용
            int skip = (page - 1) * pageSize;
            findIterable = findIterable.skip(skip).limit(pageSize);

            for (Document doc : findIterable) {
                inquiries.add(documentToInquiry(doc));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return inquiries;
    }

    /**
     * 전체 게시글 수 조회 (검색 조건 포함)
     */
    public int getTotalCount(String searchType, String searchKeyword) {
        try {
            long count;
            if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
                if ("title".equals(searchType)) {
                    count = inquiryCollection.countDocuments(
                        Filters.regex("title", searchKeyword, "i")
                    );
                } else if ("content".equals(searchType)) {
                    count = inquiryCollection.countDocuments(
                        Filters.regex("content", searchKeyword, "i")
                    );
                } else {
                    count = inquiryCollection.countDocuments(
                        Filters.or(
                            Filters.regex("title", searchKeyword, "i"),
                            Filters.regex("content", searchKeyword, "i")
                        )
                    );
                }
            } else {
                count = inquiryCollection.countDocuments();
            }
            return (int) count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 게시글 수정
     */
    public boolean update(InquiryDTO inquiry) {
        try {
            System.out.println("=== 게시글 수정 ===");
            System.out.println("ID: " + inquiry.getId());
            System.out.println("Title: " + inquiry.getTitle());

            inquiryCollection.updateOne(
                Filters.eq("_id", new ObjectId(inquiry.getId())),
                Updates.combine(
                    Updates.set("title", inquiry.getTitle()),
                    Updates.set("content", inquiry.getContent())
                )
            );
            System.out.println("게시글 수정 성공!");
            return true;
        } catch (Exception e) {
            System.out.println("게시글 수정 오류:");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 게시글 삭제
     */
    public boolean delete(String id) {
        try {
            inquiryCollection.deleteOne(Filters.eq("_id", new ObjectId(id)));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 답변 추가
     */
    public boolean addReply(String inquiryId, InquiryDTO.ReplyDTO reply) {
        try {
            System.out.println("=== 답변 추가 ===");
            System.out.println("Inquiry ID: " + inquiryId);
            System.out.println("Reply Author: " + reply.getAuthor());

            Document replyDoc = new Document();
            replyDoc.append("_id", new ObjectId())
                    .append("content", reply.getContent())
                    .append("author", reply.getAuthor())
                    .append("authorName", reply.getAuthorName())
                    .append("createdAt", Date.from(reply.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant()));

            inquiryCollection.updateOne(
                Filters.eq("_id", new ObjectId(inquiryId)),
                Updates.combine(
                    Updates.push("replies", replyDoc),
                    Updates.set("status", "답변완료")
                )
            );
            System.out.println("답변 추가 성공!");
            return true;
        } catch (Exception e) {
            System.out.println("답변 추가 오류:");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 답변 삭제
     */
    public boolean deleteReply(String inquiryId, String replyId) {
        try {
            // 답변을 제거하고 상태를 다시 확인
            InquiryDTO inquiry = findByIdWithoutIncrement(inquiryId);
            if (inquiry != null && inquiry.getReplies() != null) {
                List<Document> updatedReplies = new ArrayList<>();
                for (InquiryDTO.ReplyDTO reply : inquiry.getReplies()) {
                    if (!reply.getId().equals(replyId)) {
                        Document replyDoc = new Document();
                        if (reply.getId() != null) {
                            replyDoc.append("_id", new ObjectId(reply.getId()));
                        }
                        replyDoc.append("content", reply.getContent())
                                .append("author", reply.getAuthor())
                                .append("authorName", reply.getAuthorName())
                                .append("createdAt", Date.from(reply.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant()));
                        updatedReplies.add(replyDoc);
                    }
                }

                // 답변이 없으면 상태를 "답변대기"로 변경
                String newStatus = updatedReplies.isEmpty() ? "답변대기" : "답변완료";

                inquiryCollection.updateOne(
                    Filters.eq("_id", new ObjectId(inquiryId)),
                    Updates.combine(
                        Updates.set("replies", updatedReplies),
                        Updates.set("status", newStatus)
                    )
                );
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 댓글 추가 (답변에 대한 댓글)
     */
    public boolean addComment(String inquiryId, String replyId, InquiryDTO.CommentDTO comment) {
        try {
            System.out.println("=== 댓글 추가 ===");
            System.out.println("Inquiry ID: " + inquiryId);
            System.out.println("Reply ID: " + replyId);
            System.out.println("Comment Author: " + comment.getAuthor());

            // 게시글 조회
            InquiryDTO inquiry = findByIdWithoutIncrement(inquiryId);
            if (inquiry == null || inquiry.getReplies() == null) {
                return false;
            }

            // 해당 답변 찾기
            InquiryDTO.ReplyDTO targetReply = null;
            for (InquiryDTO.ReplyDTO reply : inquiry.getReplies()) {
                if (reply.getId().equals(replyId)) {
                    targetReply = reply;
                    break;
                }
            }

            if (targetReply == null) {
                return false;
            }

            // 댓글 추가
            Document commentDoc = new Document();
            commentDoc.append("_id", new ObjectId())
                    .append("content", comment.getContent())
                    .append("author", comment.getAuthor())
                    .append("authorName", comment.getAuthorName())
                    .append("createdAt", Date.from(comment.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant()));

            // MongoDB 배열 업데이트를 위해 replies 배열을 다시 구성
            List<Document> updatedReplies = new ArrayList<>();
            for (InquiryDTO.ReplyDTO reply : inquiry.getReplies()) {
                Document replyDoc = new Document();
                if (reply.getId() != null) {
                    replyDoc.append("_id", new ObjectId(reply.getId()));
                }
                replyDoc.append("content", reply.getContent())
                        .append("author", reply.getAuthor())
                        .append("authorName", reply.getAuthorName())
                        .append("createdAt", Date.from(reply.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant()));

                // 해당 답변에 댓글 추가
                if (reply.getId().equals(replyId)) {
                    List<Document> commentDocs = new ArrayList<>();
                    if (reply.getComments() != null) {
                        for (InquiryDTO.CommentDTO existingComment : reply.getComments()) {
                            Document existingCommentDoc = new Document();
                            if (existingComment.getId() != null) {
                                existingCommentDoc.append("_id", new ObjectId(existingComment.getId()));
                            }
                            existingCommentDoc.append("content", existingComment.getContent())
                                    .append("author", existingComment.getAuthor())
                                    .append("authorName", existingComment.getAuthorName())
                                    .append("createdAt", Date.from(existingComment.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant()));
                            commentDocs.add(existingCommentDoc);
                        }
                    }
                    commentDocs.add(commentDoc);
                    replyDoc.append("comments", commentDocs);
                } else if (reply.getComments() != null && !reply.getComments().isEmpty()) {
                    // 다른 답변의 댓글도 유지
                    List<Document> commentDocs = new ArrayList<>();
                    for (InquiryDTO.CommentDTO existingComment : reply.getComments()) {
                        Document existingCommentDoc = new Document();
                        if (existingComment.getId() != null) {
                            existingCommentDoc.append("_id", new ObjectId(existingComment.getId()));
                        }
                        existingCommentDoc.append("content", existingComment.getContent())
                                .append("author", existingComment.getAuthor())
                                .append("authorName", existingComment.getAuthorName())
                                .append("createdAt", Date.from(existingComment.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant()));
                        commentDocs.add(existingCommentDoc);
                    }
                    replyDoc.append("comments", commentDocs);
                }

                updatedReplies.add(replyDoc);
            }

            // MongoDB 업데이트
            inquiryCollection.updateOne(
                Filters.eq("_id", new ObjectId(inquiryId)),
                Updates.set("replies", updatedReplies)
            );

            System.out.println("댓글 추가 성공!");
            return true;
        } catch (Exception e) {
            System.out.println("댓글 추가 오류:");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 댓글 삭제
     */
    public boolean deleteComment(String inquiryId, String replyId, String commentId) {
        try {
            // 게시글 조회
            InquiryDTO inquiry = findByIdWithoutIncrement(inquiryId);
            if (inquiry == null || inquiry.getReplies() == null) {
                return false;
            }

            // 해당 답변 찾기 및 댓글 제거
            List<Document> updatedReplies = new ArrayList<>();
            for (InquiryDTO.ReplyDTO reply : inquiry.getReplies()) {
                Document replyDoc = new Document();
                if (reply.getId() != null) {
                    replyDoc.append("_id", new ObjectId(reply.getId()));
                }
                replyDoc.append("content", reply.getContent())
                        .append("author", reply.getAuthor())
                        .append("authorName", reply.getAuthorName())
                        .append("createdAt", Date.from(reply.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant()));

                // 해당 답변의 댓글 목록 업데이트
                if (reply.getId().equals(replyId) && reply.getComments() != null) {
                    List<Document> commentDocs = new ArrayList<>();
                    for (InquiryDTO.CommentDTO comment : reply.getComments()) {
                        if (!comment.getId().equals(commentId)) {
                            Document commentDoc = new Document();
                            if (comment.getId() != null) {
                                commentDoc.append("_id", new ObjectId(comment.getId()));
                            }
                            commentDoc.append("content", comment.getContent())
                                    .append("author", comment.getAuthor())
                                    .append("authorName", comment.getAuthorName())
                                    .append("createdAt", Date.from(comment.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant()));
                            commentDocs.add(commentDoc);
                        }
                    }
                    if (!commentDocs.isEmpty()) {
                        replyDoc.append("comments", commentDocs);
                    }
                } else if (reply.getComments() != null && !reply.getComments().isEmpty()) {
                    // 다른 답변의 댓글 유지
                    List<Document> commentDocs = new ArrayList<>();
                    for (InquiryDTO.CommentDTO comment : reply.getComments()) {
                        Document commentDoc = new Document();
                        if (comment.getId() != null) {
                            commentDoc.append("_id", new ObjectId(comment.getId()));
                        }
                        commentDoc.append("content", comment.getContent())
                                .append("author", comment.getAuthor())
                                .append("authorName", comment.getAuthorName())
                                .append("createdAt", Date.from(comment.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant()));
                        commentDocs.add(commentDoc);
                    }
                    replyDoc.append("comments", commentDocs);
                }

                updatedReplies.add(replyDoc);
            }

            // MongoDB 업데이트
            inquiryCollection.updateOne(
                Filters.eq("_id", new ObjectId(inquiryId)),
                Updates.set("replies", updatedReplies)
            );

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 게시글 댓글 추가
     */
    public boolean addInquiryComment(String inquiryId, InquiryDTO.CommentDTO comment) {
        try {
            System.out.println("=== 게시글 댓글 추가 ===");
            System.out.println("Inquiry ID: " + inquiryId);
            System.out.println("Comment Author: " + comment.getAuthor());

            Document commentDoc = new Document();
            commentDoc.append("_id", new ObjectId())
                    .append("content", comment.getContent())
                    .append("author", comment.getAuthor())
                    .append("authorName", comment.getAuthorName())
                    .append("createdAt", Date.from(comment.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant()));

            inquiryCollection.updateOne(
                Filters.eq("_id", new ObjectId(inquiryId)),
                Updates.push("comments", commentDoc)
            );

            System.out.println("게시글 댓글 추가 성공!");
            return true;
        } catch (Exception e) {
            System.out.println("게시글 댓글 추가 오류:");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 게시글 댓글 삭제
     */
    public boolean deleteInquiryComment(String inquiryId, String commentId) {
        try {
            // 게시글 조회
            InquiryDTO inquiry = findByIdWithoutIncrement(inquiryId);
            if (inquiry == null || inquiry.getComments() == null) {
                return false;
            }

            // 댓글 목록에서 해당 댓글 제거
            List<Document> updatedComments = new ArrayList<>();
            for (InquiryDTO.CommentDTO comment : inquiry.getComments()) {
                if (!comment.getId().equals(commentId)) {
                    Document commentDoc = new Document();
                    if (comment.getId() != null) {
                        commentDoc.append("_id", new ObjectId(comment.getId()));
                    }
                    commentDoc.append("content", comment.getContent())
                            .append("author", comment.getAuthor())
                            .append("authorName", comment.getAuthorName())
                            .append("createdAt", Date.from(comment.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant()));
                    updatedComments.add(commentDoc);
                }
            }

            // MongoDB 업데이트
            inquiryCollection.updateOne(
                Filters.eq("_id", new ObjectId(inquiryId)),
                Updates.set("comments", updatedComments)
            );

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

