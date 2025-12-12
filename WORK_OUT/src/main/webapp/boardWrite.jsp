<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${inquiry != null ? 'Edit Inquiry' : 'Write Inquiry'} - FitBegin</title>
    <link rel="stylesheet" type="text/css" href="css/boardstyle.css">
</head>
<body>
<%@ include file="header.jsp" %>

<div class="board-container">
    <div class="board-header">
        <h1>${inquiry != null ? 'EDIT INQUIRY' : 'WRITE INQUIRY'}</h1>
        <p>${inquiry != null ? 'Please edit your inquiry below.' : 'Please fill in the form below to create a new inquiry.'}</p>
    </div>

    <c:if test="${not empty error}">
        <div class="error-message">
            ${error}
        </div>
    </c:if>

    <div class="board-form">
        <form action="${pageContext.request.contextPath}/inquiry" method="post">
            <input type="hidden" name="action" value="write">
            <c:if test="${inquiry != null}">
                <input type="hidden" name="id" value="${inquiry.id}">
            </c:if>

            <div class="form-group">
                <label for="title">Title *</label>
                <input type="text" id="title" name="title" value="${inquiry != null ? inquiry.title : ''}" required maxlength="200" placeholder="Enter inquiry title">
            </div>

            <div class="form-group">
                <label for="content">Content *</label>
                <textarea id="content" name="content" required placeholder="Enter inquiry content">${inquiry != null ? inquiry.content : ''}</textarea>
            </div>

            <div class="form-actions">
                <a href="${pageContext.request.contextPath}/inquiry?action=list" class="detail-btn btn-secondary">Cancel</a>
                <button type="submit" class="detail-btn btn-primary">${inquiry != null ? 'Update' : 'Submit'}</button>
            </div>
        </form>
    </div>
</div>

<%@ include file="footer.jsp" %>
</body>
</html>

