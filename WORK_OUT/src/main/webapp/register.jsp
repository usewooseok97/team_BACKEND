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

        <div class="form-group">
            <label class="form-label" for="email">
                ${lang == 'ko' ? '이메일' : 'EMAIL'}<span class="required">*</span>
            </label>
            <input type="email" id="email" name="email" class="form-input" 
                   placeholder="${lang == 'ko' ? '이메일을 입력하세요' : 'Enter Your Email'}">
        </div>

        <div class="form-group">
            <label class="form-label" for="phone">${lang == 'ko' ? '전화번호' : 'PHONE'}</label>
            <input type="tel" id="phone" name="phone" class="form-input" 
                   placeholder="${lang == 'ko' ? '예) 010-1234-5678' : 'Ex)+1(506)-223-4413'}">
        </div>

        <div class="form-group">
            <label class="form-label">${lang == 'ko' ? '성별' : 'GENDER'}</label>
            <select name="gender" class="form-input">
                <option value="">${lang == 'ko' ? '선택' : 'SELECT'}</option>
                <option value="male">${lang == 'ko' ? '남성' : 'MALE'}</option>
                <option value="female">${lang == 'ko' ? '여성' : 'FEMALE'}</option>
            </select>
        </div>

        <div class="form-group">
            <label class="form-label" for="birthdate">${lang == 'ko' ? '생년월일' : 'DATE OF BIRTH'}</label>
            <input type="date" id="birthdate" name="birthdate" class="form-input">
        </div>

        <div class="form-group">
            <label class="form-label">${lang == 'ko' ? '운동 경력' : 'WORKOUT EXPERIENCE'}</label>
            <select name="experience" class="form-input">
                <option value="">${lang == 'ko' ? '선택' : 'SELECT'}</option>
                <option value="beginner">${lang == 'ko' ? '초급' : 'BEGINNER'}</option>
                <option value="intermediate">${lang == 'ko' ? '중급' : 'INTERMEDIATE'}</option>
                <option value="advanced">${lang == 'ko' ? '고급' : 'ADVANCED'}</option>
            </select>
        </div>

        <div class="form-group">
            <label class="form-label">${lang == 'ko' ? '관심 부위' : 'INTERESTED'}</label>
            <div class="checkbox-group">
                <label><input type="checkbox" name="interest" value="upper"> ${lang == 'ko' ? '상체' : 'UPPER'}</label>
                <label><input type="checkbox" name="interest" value="lower"> ${lang == 'ko' ? '하체' : 'LOWER'}</label>
                <label><input type="checkbox" name="interest" value="core"> ${lang == 'ko' ? '코어' : 'CORE'}</label>
                <label><input type="checkbox" name="interest" value="fullbody"> ${lang == 'ko' ? '전신' : 'FULL BODY'}</label>
            </div>
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