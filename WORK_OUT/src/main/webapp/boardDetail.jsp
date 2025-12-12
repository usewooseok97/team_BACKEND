<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${lang == 'ko' ? '문의사항' : 'Inquiry Detail'} - FitBegin</title>
    <link rel="stylesheet" type="text/css" href="css/boardstyle.css">
</head>
<body>
<%@ include file="header.jsp" %>

<div class="board-container">
    <c:if test="${not empty error}">
        <div class="error-message">
            ${error}
        </div>
    </c:if>

    <c:if test="${not empty inquiry}">
        <!-- 게시글 상세보기 -->
        <div class="board-detail">
            <div class="detail-header">
                <h2 class="detail-title">${inquiry.title}</h2>
                <div class="detail-meta">
                    <span>Author: <strong>${inquiry.authorName}</strong></span>
                    <span>Date: ${inquiry.createdAt.toLocalDate()} ${inquiry.createdAt.toLocalTime().toString().substring(0, 5)}</span>
                    <span>Views: <strong>${inquiry.viewCount}</strong></span>
                    <span>
                        Status: 
                        <span class="status-badge ${inquiry.status == '답변완료' ? 'status-completed' : 'status-waiting'}">
                            ${inquiry.status == '답변완료' ? 'Answered' : 'Pending'}
                        </span>
                    </span>
                </div>
            </div>

            <div class="detail-content">
                ${inquiry.content}
            </div>

            <!-- 작성자 또는 관리자만 수정/삭제 가능 -->
            <c:if test="${not empty sessionScope.user && (sessionScope.user.username == inquiry.author || sessionScope.user.admin)}">
                <div class="detail-actions">
                    <a href="${pageContext.request.contextPath}/inquiry?action=edit&id=${inquiry.id}" class="detail-btn btn-primary">Edit</a>
                    <a href="${pageContext.request.contextPath}/inquiry?action=delete&id=${inquiry.id}" 
                       class="detail-btn btn-danger" 
                       onclick="return confirm('Are you sure you want to delete this inquiry?');">Delete</a>
                </div>
            </c:if>
        </div>

        <!-- 게시글 댓글 목록 -->
        <c:if test="${not empty inquiry.comments}">
            <div class="reply-section">
                <h3 style="margin-bottom: 20px; color: #2d2d3d;">Comments</h3>
                <div class="reply-list">
                    <c:forEach var="comment" items="${inquiry.comments}">
                        <div class="comment-item" style="background-color: #f8f9fa; padding: 15px; border-radius: 5px; margin-bottom: 10px; border-left: 3px solid #667eea;">
                            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 8px; flex-wrap: wrap; gap: 10px;">
                                <div>
                                    <span style="font-weight: 600; color: #667eea; font-size: 13px;">${comment.authorName}</span>
                                    <span style="color: #999; font-size: 12px; margin-left: 10px;">
                                        (${comment.createdAt.toLocalDate()} ${comment.createdAt.toLocalTime().toString().substring(0, 5)})
                                    </span>
                                </div>
                                <c:if test="${not empty sessionScope.user && (sessionScope.user.username == comment.author || sessionScope.user.admin)}">
                                    <a href="${pageContext.request.contextPath}/inquiry?action=deleteInquiryComment&inquiryId=${inquiry.id}&commentId=${comment.id}" 
                                       class="detail-btn btn-danger" 
                                       style="padding: 3px 8px; font-size: 11px;"
                                       onclick="return confirm('Are you sure you want to delete this comment?');">Delete</a>
                                </c:if>
                            </div>
                            <div style="color: #333; font-size: 13px; line-height: 1.6; white-space: pre-wrap; word-wrap: break-word;">${comment.content}</div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </c:if>

        <!-- 게시글 댓글 작성 폼 (로그인한 사용자 모두 가능) -->
        <c:if test="${not empty sessionScope.user}">
            <div class="reply-section">
                <h3 style="margin-bottom: 20px; color: #2d2d3d;">Write Comment</h3>
                <form action="${pageContext.request.contextPath}/inquiry" method="post" class="reply-form">
                    <input type="hidden" name="action" value="inquiryComment">
                    <input type="hidden" name="inquiryId" value="${inquiry.id}">
                    <textarea name="content" required placeholder="Write a comment on this inquiry..."></textarea>
                    <div style="text-align: right;">
                        <button type="submit" class="detail-btn btn-primary">Submit Comment</button>
                    </div>
                </form>
            </div>
        </c:if>

        <!-- 답변 목록 -->
        <c:if test="${not empty inquiry.replies}">
            <div class="reply-section">
                <h3 style="margin-bottom: 20px; color: #2d2d3d;">Replies</h3>
                <div class="reply-list">
                    <c:forEach var="reply" items="${inquiry.replies}">
                        <div class="reply-item">
                            <div class="reply-header">
                                <div>
                                    <span class="reply-author">${reply.authorName}</span>
                                    <span class="reply-date">
                                        (${reply.createdAt.toLocalDate()} ${reply.createdAt.toLocalTime().toString().substring(0, 5)})
                                    </span>
                                </div>
                                <c:if test="${not empty sessionScope.user && sessionScope.user.admin}">
                                    <a href="${pageContext.request.contextPath}/inquiry?action=deleteReply&inquiryId=${inquiry.id}&replyId=${reply.id}" 
                                       class="detail-btn btn-danger" 
                                       style="padding: 5px 10px; font-size: 12px;"
                                       onclick="return confirm('Are you sure you want to delete this reply?');">Delete</a>
                                </c:if>
                            </div>
                            <div class="reply-content">${reply.content}</div>

                            <!-- 댓글 목록 -->
                            <c:if test="${not empty reply.comments}">
                                <div class="comment-list" style="margin-top: 20px; padding-top: 20px; border-top: 1px solid #eee;">
                                    <h4 style="margin-bottom: 15px; color: #666; font-size: 14px;">Comments</h4>
                                    <c:forEach var="comment" items="${reply.comments}">
                                        <div class="comment-item" style="background-color: #f8f9fa; padding: 15px; border-radius: 5px; margin-bottom: 10px; border-left: 3px solid #667eea;">
                                            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 8px; flex-wrap: wrap; gap: 10px;">
                                                <div>
                                                    <span style="font-weight: 600; color: #667eea; font-size: 13px;">${comment.authorName}</span>
                                                    <span style="color: #999; font-size: 12px; margin-left: 10px;">
                                                        (${comment.createdAt.toLocalDate()} ${comment.createdAt.toLocalTime().toString().substring(0, 5)})
                                                    </span>
                                                </div>
                                                <c:if test="${not empty sessionScope.user && (sessionScope.user.username == comment.author || sessionScope.user.admin)}">
                                                    <a href="${pageContext.request.contextPath}/inquiry?action=deleteComment&inquiryId=${inquiry.id}&replyId=${reply.id}&commentId=${comment.id}" 
                                                       class="detail-btn btn-danger" 
                                                       style="padding: 3px 8px; font-size: 11px;"
                                                       onclick="return confirm('Are you sure you want to delete this comment?');">Delete</a>
                                                </c:if>
                                            </div>
                                            <div style="color: #333; font-size: 13px; line-height: 1.6; white-space: pre-wrap; word-wrap: break-word;">${comment.content}</div>
                                        </div>
                                    </c:forEach>
                                </div>
                            </c:if>

                            <!-- 댓글 작성 폼 (로그인한 사용자 모두 가능) -->
                            <c:if test="${not empty sessionScope.user}">
                                <div class="comment-form" style="margin-top: 15px; padding-top: 15px; border-top: 1px solid #eee;">
                                    <form action="${pageContext.request.contextPath}/inquiry" method="post" style="display: flex; gap: 10px; align-items: flex-start;">
                                        <input type="hidden" name="action" value="comment">
                                        <input type="hidden" name="inquiryId" value="${inquiry.id}">
                                        <input type="hidden" name="replyId" value="${reply.id}">
                                        <textarea name="content" required placeholder="Write a comment..." 
                                                  style="flex: 1; padding: 10px; border: 1px solid #ddd; border-radius: 5px; font-size: 13px; font-family: inherit; min-height: 80px; resize: vertical;"></textarea>
                                        <button type="submit" class="detail-btn btn-primary" style="padding: 10px 20px; font-size: 13px; white-space: nowrap;">Submit</button>
                                    </form>
                                </div>
                            </c:if>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </c:if>

        <!-- 답변 작성 폼 (관리자만) -->
        <c:if test="${not empty sessionScope.user && sessionScope.user.admin}">
            <div class="reply-section">
                <h3 style="margin-bottom: 20px; color: #2d2d3d;">Write Reply</h3>
                <form action="${pageContext.request.contextPath}/inquiry" method="post" class="reply-form">
                    <input type="hidden" name="action" value="reply">
                    <input type="hidden" name="inquiryId" value="${inquiry.id}">
                    <textarea name="content" required placeholder="Enter your reply..."></textarea>
                    <div style="text-align: right;">
                        <button type="submit" class="detail-btn btn-primary">Submit Reply</button>
                    </div>
                </form>
            </div>
        </c:if>
    </c:if>

    <!-- 목록으로 돌아가기 -->
    <div style="text-align: center; margin-top: 30px;">
        <a href="${pageContext.request.contextPath}/inquiry?action=list" class="detail-btn btn-secondary">Back to List</a>
    </div>
</div>

<%@ include file="footer.jsp" %>
</body>
</html>

