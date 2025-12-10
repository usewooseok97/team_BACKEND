<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Customer Service - WORK OUT</title>
    <link rel="stylesheet" type="text/css" href="css/boardstyle.css">
</head>
<body>
<%@ include file="header.jsp" %>

<div class="board-container">
    <div class="board-header">
        <h1>CUSTOMER SERVICE</h1>
        <p>Please check the inquiries below or create a new inquiry.</p>
    </div>

    <c:if test="${not empty error}">
        <div class="error-message">
            ${error}
        </div>
    </c:if>

    <!-- 검색 및 작성 버튼 -->
    <div class="board-actions">
        <form action="${pageContext.request.contextPath}/inquiry" method="get" class="search-form">
            <input type="hidden" name="action" value="list">
            <select name="searchType">
                <option value="all" ${searchType == 'all' ? 'selected' : ''}>All</option>
                <option value="title" ${searchType == 'title' ? 'selected' : ''}>Title</option>
                <option value="content" ${searchType == 'content' ? 'selected' : ''}>Content</option>
            </select>
            <input type="text" name="searchKeyword" value="${searchKeyword != null ? searchKeyword : ''}" placeholder="Search...">
            <button type="submit">Search</button>
        </form>
        <c:if test="${not empty sessionScope.user}">
            <a href="${pageContext.request.contextPath}/inquiry?action=write" class="write-btn">Write Inquiry</a>
        </c:if>
    </div>

    <!-- 게시글 목록 -->
    <c:choose>
        <c:when test="${not empty inquiries}">
            <table class="board-table">
                <thead>
                    <tr>
                        <th style="width: 8%;">No</th>
                        <th style="width: 40%;">Title</th>
                        <th style="width: 12%;">Author</th>
                        <th style="width: 12%;">Date</th>
                        <th style="width: 8%;">Views</th>
                        <th style="width: 10%;">Status</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="inquiry" items="${inquiries}" varStatus="status">
                        <tr onclick="location.href='${pageContext.request.contextPath}/inquiry?action=detail&id=${inquiry.id}'">
                            <td>${totalCount - ((currentPage - 1) * pageSize) - status.index}</td>
                            <td>
                                <a href="${pageContext.request.contextPath}/inquiry?action=detail&id=${inquiry.id}" class="board-title-link">
                                    ${inquiry.title}
                                </a>
                            </td>
                            <td>${inquiry.authorName}</td>
                            <td>
                                ${inquiry.createdAt.toLocalDate()}
                            </td>
                            <td>${inquiry.viewCount}</td>
                            <td>
                                <span class="status-badge ${inquiry.status == '답변완료' ? 'status-completed' : 'status-waiting'}">
                                    ${inquiry.status == '답변완료' ? 'Answered' : 'Pending'}
                                </span>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>

            <!-- 페이징 -->
            <div class="pagination">
                <c:if test="${currentPage > 1}">
                    <a href="${pageContext.request.contextPath}/inquiry?action=list&page=${currentPage - 1}&searchType=${searchType}&searchKeyword=${searchKeyword}">Previous</a>
                </c:if>
                
                <c:forEach var="i" begin="1" end="${totalPages}">
                    <c:choose>
                        <c:when test="${i == currentPage}">
                            <span class="current">${i}</span>
                        </c:when>
                        <c:otherwise>
                            <a href="${pageContext.request.contextPath}/inquiry?action=list&page=${i}&searchType=${searchType}&searchKeyword=${searchKeyword}">${i}</a>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
                
                <c:if test="${currentPage < totalPages}">
                    <a href="${pageContext.request.contextPath}/inquiry?action=list&page=${currentPage + 1}&searchType=${searchType}&searchKeyword=${searchKeyword}">Next</a>
                </c:if>
            </div>
        </c:when>
        <c:otherwise>
            <div class="empty-message">
                <p>No inquiries found.</p>
                <c:if test="${not empty sessionScope.user}">
                    <a href="${pageContext.request.contextPath}/inquiry?action=write" class="write-btn">Write First Inquiry</a>
                </c:if>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<%@ include file="footer.jsp" %>
</body>
</html>

