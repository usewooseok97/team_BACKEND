<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>회원가입 - WORK OUT</title>
    <link rel="stylesheet" type="text/css" href="css/formstyle.css">
</head>
<body>
<%@ include file="header.jsp" %>

    <div class="form-container">
        <h2 class="form-title">회원가입</h2>

        <c:if test="${not empty error}">
            <div class="error-message">${error}</div>
        </c:if>

        <form action="register" method="post">
            <div class="form-group">
                <label class="form-label" for="username">아이디 <span class="required">*</span></label>
                <input type="text" id="username" name="username" class="form-input"
                       placeholder="아이디를 입력하세요" required>
            </div>

            <div class="form-group">
                <label class="form-label" for="password">비밀번호 <span class="required">*</span></label>
                <input type="password" id="password" name="password" class="form-input"
                       placeholder="비밀번호를 입력하세요" required>
            </div>

            <div class="form-group">
                <label class="form-label" for="passwordConfirm">비밀번호 확인 <span class="required">*</span></label>
                <input type="password" id="passwordConfirm" name="passwordConfirm" class="form-input"
                       placeholder="비밀번호를 다시 입력하세요" required>
            </div>

            <div class="form-group">
                <label class="form-label" for="name">이름 <span class="required">*</span></label>
                <input type="text" id="name" name="name" class="form-input"
                       placeholder="이름을 입력하세요" required>
            </div>

            <button type="submit" class="form-button">회원가입</button>
        </form>

        <div class="form-link">
            이미 계정이 있으신가요? <a href="login">로그인</a>
        </div>
    </div>
    <%@ include file="footer.jsp" %>
</body>
</html>
