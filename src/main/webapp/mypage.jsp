<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${lang == 'ko' ? '마이페이지' : 'MyPage'} - WORK OUT</title>
    <link rel="stylesheet" type="text/css" href="css/mypagestyle.css">
</head>
<%@ include file="header.jsp" %>
<body>

    <div class="form-container">
        <h2 class="form-title">${lang == 'ko' ? '마이페이지' : 'MyPage'}</h2>
        <p class="form-subtitle">${lang == 'ko' ? '내 프로필 수정' : 'Edit My Profile'}</p>

        <!-- 기본 정보 표시 -->
        <div class="info-box">
            <div class="info-row">
                <span class="info-label">${lang == 'ko' ? '아이디:' : 'ID:'}</span>
                <span class="info-value">${user.username}</span>
            </div>
            <div class="info-row">
                <span class="info-label">${lang == 'ko' ? '역할:' : 'Role:'}</span>
                <span class="info-value">
                    <c:choose>
                        <c:when test="${user.role == 'ADMIN'}">
                            ${lang == 'ko' ? '관리자' : 'Admin'}
                        </c:when>
                        <c:otherwise>
                            ${lang == 'ko' ? '일반 회원' : 'Regular'}
                        </c:otherwise>
                    </c:choose>
                </span>
            </div>
            <div class="info-row">
                <span class="info-label">${lang == 'ko' ? '가입일:' : 'Joined Date:'}</span>
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
                <label class="form-label" for="username">${lang == 'ko' ? '아이디' : 'ID'}</label>
                <input type="text" id="username" name="username" class="form-input"
                       value="${user.username}" disabled>
                <p class="help-text">${lang == 'ko' ? '아이디는 변경할 수 없습니다.' : 'ID Can\'t Be Changed.'}</p>
            </div>

            <div class="form-group">
                <label class="form-label" for="password">${lang == 'ko' ? '새 비밀번호' : 'New Password'}</label>
                <input type="password" id="password" name="password" class="form-input"
                       placeholder="">
                <p class="help-text">${lang == 'ko' ? '새 비밀번호를 입력하세요.' : 'Please Enter your New Password.'}</p>
            </div>

            <div class="form-group">
                <label class="form-label" for="passwordConfirm">${lang == 'ko' ? '비밀번호 확인' : 'Password Confirmation'}</label>
                <input type="password" id="passwordConfirm" name="passwordConfirm" class="form-input"
                       placeholder="">
            </div>

            <div class="form-group">
                <label class="form-label" for="name">${lang == 'ko' ? '이름' : 'Name'} <span class="required">*</span></label>
                <input type="text" id="name" name="name" class="form-input"
                       value="${user.name}" required>
            </div>

            <button type="submit" class="form-button">${lang == 'ko' ? '확인' : 'Confirm'}</button>
        </form>

        <div class="form-link">
            <a href="index.jsp">← ${lang == 'ko' ? '홈으로 돌아가기' : 'Back to Home'}</a>
        </div>
    </div>
    <%@ include file="footer.jsp" %>
</body>
</html>