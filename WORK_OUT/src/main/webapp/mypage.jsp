<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>MyPage - WORK OUT</title>
    <link rel="stylesheet" type="text/css" href="css/mypagestyle.css">

</head>
 <%@ include file="header.jsp" %>
<body>

    <div class="form-container">
        <h2 class="form-title">MyPage</h2>
        <p class="form-subtitle">Edit My Profile</p>

        <!-- 기본 정보 표시 -->
        <div class="info-box">
            <div class="info-row">
                <span class="info-label">ID:</span>
                <span class="info-value">${user.username}</span>
            </div>
            <div class="info-row">
                <span class="info-label">Role:</span>
                <span class="info-value">
                    <c:choose>
                        <c:when test="${user.role == 'ADMIN'}">Admin</c:when>
                        <c:otherwise>Regular</c:otherwise>
                    </c:choose>
                </span>
            </div>
            <div class="info-row">
                <span class="info-label">Joined Date:</span>
                <span class="info-value">${user.regDate}</span>
            </div>
        </div>

        <c:if test="${not empty success}">
            <div class="success-message">${success}</div>
        </c:if>

        <c:if test="${not empty error}">
            <div class="error-message">${error}</div>
        </c:if>

        <form action="mypage" method="post">
            <div class="form-group">
                <label class="form-label" for="username">ID</label>
                <input type="text" id="username" name="username" class="form-input"
                       value="${user.username}" disabled>
                <p class="help-text">ID Can't Be Changed.</p>
            </div>

            <div class="form-group">
                <label class="form-label" for="password">New Password</label>
                <input type="password" id="password" name="password" class="form-input"
                       placeholder="">
                <p class="help-text">Please Enter your New Password.</p>
            </div>

            <div class="form-group">
                <label class="form-label" for="passwordConfirm">Password Confirmation</label>
                <input type="password" id="passwordConfirm" name="passwordConfirm" class="form-input"
                       placeholder="">
            </div>

            <div class="form-group">
                <label class="form-label" for="name">Name <span class="required">*</span></label>
                <input type="text" id="name" name="name" class="form-input"
                       value="${user.name}" required>
            </div>

            <button type="submit" class="form-button">Confirm</button>
        </form>

        <div class="form-link">
            <a href="index.jsp">← Back to Home</a>
        </div>
    </div>
    <%@ include file="footer.jsp" %>
</body>
</html>
