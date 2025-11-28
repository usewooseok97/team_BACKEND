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
    <h2 class="form-title">SignUp</h2>

    <c:if test="${not empty error}">
        <div class="error-message">${error}</div>
    </c:if>

    <form action="register" method="post">
        <div class="form-group">
            <label class="form-label" for="username">ID <span class="required">*</span></label>
            <input type="text" id="username" name="username" class="form-input" placeholder="Enter Your ID" required>
        </div>

        <div class="form-group">
            <label class="form-label" for="password">PASSWORD <span class="required">*</span></label>
            <input type="password" id="password" name="password" class="form-input" placeholder="Enter Your Password" required>
        </div>

        <div class="form-group">
            <label class="form-label" for="passwordConfirm">PASSWORD CONFIRMATION <span class="required">*</span></label>
            <input type="password" id="passwordConfirm" name="passwordConfirm" class="form-input" placeholder="Enter Your Password Again" required>
        </div>

        <div class="form-group">
            <label class="form-label" for="name">NAME <span class="required">*</span></label>
            <input type="text" id="name" name="name" class="form-input" placeholder="Enter Your Name" required>
        </div>

        <div class="form-group">
            <label class="form-label" for="email">EMAIL<span class="required">*</span></label>
            <input type="email" id="email" name="email" class="form-input" placeholder="Enter Your Email">
        </div>

        <div class="form-group">
            <label class="form-label" for="phone">PHONE</label>
            <input type="tel" id="phone" name="phone" class="form-input" placeholder="Ex)+1(506)-223-4413">
        </div>

        <div class="form-group">
            <label class="form-label">GENDER</label>
            <select name="gender" class="form-input">
                <option value="">SELECT</option>
                <option value="male">MALE</option>
                <option value="female">FEMALE</option>
            </select>
        </div>

        <div class="form-group">
            <label class="form-label" for="birthdate">DATE OF BIRTH</label>
            <input type="date" id="birthdate" name="birthdate" class="form-input">
        </div>

        <div class="form-group">
            <label class="form-label">WORKOUT EXPERIENCE</label>
            <select name="experience" class="form-input">
                <option value="">SELECT</option>
                <option value="beginner">BEGINNER</option>
                <option value="intermediate">INTERMEDIATE</option>
                <option value="advanced">ADVANCED</option>
            </select>
        </div>

        <div class="form-group">
            <label class="form-label">INTERESTED</label>
            <div class="checkbox-group">
                <label><input type="checkbox" name="interest" value="upper"> UPPER</label>
                <label><input type="checkbox" name="interest" value="lower"> LOWER</label>
                <label><input type="checkbox" name="interest" value="core"> CORE</label>
                <label><input type="checkbox" name="interest" value="fullbody"> FULL BODY</label>
            </div>
        </div>

        <button type="submit" class="form-button">SIGNUP</button>
    </form>

    <div class="form-link">
        DO YOU ALREADY HAVE AN ACCOUNT? <a href="login">LOGIN</a>
    </div>
</div>

<%@ include file="footer.jsp" %>
</body>
</html>
