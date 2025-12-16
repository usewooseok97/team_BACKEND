<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${lang == 'ko' ? '회원가입' : 'Sign Up'} - FitBegin</title>
    <link rel="stylesheet" type="text/css" href="css/formstyle.css">
</head>
<body>
<%@ include file="header.jsp" %>

<div class="form-container">
    <h2 class="form-title">${lang == 'ko' ? '회원가입' : 'SIGN UP'}</h2>

    <c:if test="${not empty error}">
        <div class="error-message">${error}</div>
    </c:if>

    <form action="register" method="post">
        <div class="form-group">
            <label class="form-label" for="username">
                ${lang == 'ko' ? '아이디' : 'ID'} <span class="required">*</span>
            </label>
            <input type="text" id="username" name="username" class="form-input" 
                   placeholder="${lang == 'ko' ? '아이디를 입력하세요' : 'Enter Your ID'}" required>
        </div>

        <div class="form-group">
            <label class="form-label" for="password">
                ${lang == 'ko' ? '비밀번호' : 'PASSWORD'} <span class="required">*</span>
            </label>
            <input type="password" id="password" name="password" class="form-input" 
                   placeholder="${lang == 'ko' ? '비밀번호를 입력하세요' : 'Enter Your Password'}" required>
        </div>

        <div class="form-group">
            <label class="form-label" for="passwordConfirm">
                ${lang == 'ko' ? '비밀번호 확인' : 'PASSWORD CONFIRMATION'} <span class="required">*</span>
            </label>
            <input type="password" id="passwordConfirm" name="passwordConfirm" class="form-input" 
                   placeholder="${lang == 'ko' ? '비밀번호를 다시 입력하세요' : 'Enter Your Password Again'}" required>
        </div>

        <div class="form-group">
            <label class="form-label" for="name">
                ${lang == 'ko' ? '이름' : 'NAME'} <span class="required">*</span>
            </label>
            <input type="text" id="name" name="name" class="form-input" 
                   placeholder="${lang == 'ko' ? '이름을 입력하세요' : 'Enter Your Name'}" required>
        </div>

        <button type="submit" class="form-button">${lang == 'ko' ? '회원가입' : 'SIGNUP'}</button>
    </form>

    <div class="form-link">
        ${lang == 'ko' ? '이미 계정이 있으신가요?' : 'DO YOU ALREADY HAVE AN ACCOUNT?'} 
        <a href="login">${lang == 'ko' ? '로그인' : 'LOGIN'}</a>
    </div>
</div>

<%@ include file="footer.jsp" %>
</body>
</html>