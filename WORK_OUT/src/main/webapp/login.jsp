<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<%@ include file="header.jsp" %>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${lang == 'ko' ? '로그인' : 'Login'} - WORK OUT</title>
    <link rel="stylesheet" type="text/css" href="css/formstyle.css">
</head>
<body>

    <div class="form-container">
        <h2 class="form-title">${lang == 'ko' ? '로그인' : 'LogIn'}</h2>

        <c:if test="${param.registered == 'true'}">
            <div class="success-message">
                ${lang == 'ko' ? '회원가입이 완료되었습니다! 로그인해주세요.' : 'YOU ARE SIGNED UP! PLEASE LOGIN.'}
            </div>
        </c:if>

        <c:if test="${not empty error}">
            <div class="error-message">${error}</div>
        </c:if>

        <form action="login" method="post">
            <div class="form-group">
                <label class="form-label" for="username">
                    ${lang == 'ko' ? '아이디' : 'ID'}<span class="required">*</span>
                </label>
                <input type="text" id="username" name="username" class="form-input"
                       placeholder="${lang == 'ko' ? '아이디를 입력하세요' : 'ENTER YOUR ID'}" required>
            </div>

            <div class="form-group">
                <label class="form-label" for="password">
                    ${lang == 'ko' ? '비밀번호' : 'PASSWORD'}<span class="required">*</span>
                </label>
                <input type="password" id="password" name="password" class="form-input"
                       placeholder="${lang == 'ko' ? '비밀번호를 입력하세요' : 'ENTER YOUR PASSWORD'}" required>
            </div>

            <button type="submit" class="form-button">
                ${lang == 'ko' ? '로그인' : 'LOGIN'}
            </button>
        </form>

        <div class="form-link">
            ${lang == 'ko' ? '계정이 없으신가요?' : 'YOU DON\'T HAVE AN ACCOUNT YET?'} 
            <a href="register">${lang == 'ko' ? '회원가입' : 'SIGNUP'}</a>
        </div>
        <div class="form-link">
            ${lang == 'ko' ? '비밀번호를 잊으셨나요?' : 'FORGOT YOUR PASSWORD?'} 
            <a href="findpassword.jsp">${lang == 'ko' ? '비밀번호 찾기' : 'FIND PASSWORD'}</a>
        </div>

        <!-- 테스트용 계정 정보 -->
        <div class="test-accounts">
            <strong>${lang == 'ko' ? '테스트 계정:' : 'Test Accounts:'}</strong><br>
            ${lang == 'ko' ? '관리자' : 'Admin'} - ID: admin / PW: admin123<br>
            ${lang == 'ko' ? '일반유저' : 'User'} - ID: user1 / PW: user123
        </div>
    </div>
<%@ include file="footer.jsp" %>
</body>
</html>