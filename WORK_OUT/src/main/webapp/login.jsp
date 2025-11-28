<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<%@ include file="header.jsp" %>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>로그인 - WORK OUT</title>
    <link rel="stylesheet" type="text/css" href="css/formstyle.css">
</head>
<body>

    <div class="form-container">
        <h2 class="form-title">LogIn</h2>

        <c:if test="${param.registered == 'true'}">
            <div class="success-message">YOU ARE SIGNED UP! PLEASE LOGIN.</div>
        </c:if>

        <c:if test="${not empty error}">
            <div class="error-message">${error}</div>
        </c:if>

        <form action="login" method="post">
            <div class="form-group">
                <label class="form-label" for="username">ID<span class="required">*</span></label>
                <input type="text" id="username" name="username" class="form-input"
                       placeholder="ENTER YOUR ID" required>
            </div>

            <div class="form-group">
                <label class="form-label" for="password">PASSWORD<span class="required">*</span></label>
                <input type="password" id="password" name="password" class="form-input"
                       placeholder="ENTER YOUR PASSWORD" required>
            </div>

            <button type="submit" class="form-button">LOGIN</button>
        </form>

        <div class="form-link">
            YOU DON'T HAVE AN ACCOUNT YET? <a href="register">SIGNUP</a>
        </div>
         <div class="form-link">
    FORGOT YOUR PASSWORD? <a href="findpassword.jsp">FIND PASSWORD</a>
</div>


        <!-- 테스트용 계정 정보 -->
        <div class="test-accounts">
            <strong>테스트 계정:</strong><br>
            관리자 - ID: admin / PW: admin123<br>
            일반유저 - ID: user1 / PW: user123
        </div>
    </div>
<%@ include file="footer.jsp" %>
</body>
</html>
