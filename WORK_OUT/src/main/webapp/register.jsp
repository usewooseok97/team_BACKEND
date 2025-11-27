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
            <input type="text" id="username" name="username" class="form-input" placeholder="아이디를 입력하세요" required>
        </div>

        <div class="form-group">
            <label class="form-label" for="password">비밀번호 <span class="required">*</span></label>
            <input type="password" id="password" name="password" class="form-input" placeholder="비밀번호를 입력하세요" required>
        </div>

        <div class="form-group">
            <label class="form-label" for="passwordConfirm">비밀번호 확인 <span class="required">*</span></label>
            <input type="password" id="passwordConfirm" name="passwordConfirm" class="form-input" placeholder="비밀번호를 다시 입력하세요" required>
        </div>

        <div class="form-group">
            <label class="form-label" for="name">이름 <span class="required">*</span></label>
            <input type="text" id="name" name="name" class="form-input" placeholder="이름을 입력하세요" required>
        </div>

        <div class="form-group">
            <label class="form-label" for="email">이메일</label>
            <input type="email" id="email" name="email" class="form-input" placeholder="이메일을 입력하세요">
        </div>

        <div class="form-group">
            <label class="form-label" for="phone">전화번호</label>
            <input type="tel" id="phone" name="phone" class="form-input" placeholder="예: 010-1234-5678">
        </div>

        <div class="form-group">
            <label class="form-label">성별</label>
            <select name="gender" class="form-input">
                <option value="">선택</option>
                <option value="male">남성</option>
                <option value="female">여성</option>
            </select>
        </div>

        <div class="form-group">
            <label class="form-label" for="birthdate">생년월일</label>
            <input type="date" id="birthdate" name="birthdate" class="form-input">
        </div>

        <div class="form-group">
            <label class="form-label">운동 경험</label>
            <select name="experience" class="form-input">
                <option value="">선택</option>
                <option value="beginner">초급</option>
                <option value="intermediate">중급</option>
                <option value="advanced">고급</option>
            </select>
        </div>

        <div class="form-group">
            <label class="form-label">관심 부위</label>
            <div class="checkbox-group">
                <label><input type="checkbox" name="interest" value="upper"> 상체</label>
                <label><input type="checkbox" name="interest" value="lower"> 하체</label>
                <label><input type="checkbox" name="interest" value="core"> 코어</label>
                <label><input type="checkbox" name="interest" value="fullbody"> 전신</label>
            </div>
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
