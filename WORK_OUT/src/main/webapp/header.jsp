<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    // 세션에서 언어 설정 가져오기 (기본값: en)
    String currentLang = (String) session.getAttribute("language");
    if (currentLang == null) {
        currentLang = "en";
        session.setAttribute("language", "en");
    }
    request.setAttribute("lang", currentLang);
%>
<!DOCTYPE html>
<html lang="${lang}">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>FitBegin</title>
	<link rel="icon" href="${pageContext.request.contextPath}/asset/backend_logo.ico">
    
    <link rel="stylesheet" type="text/css" href="css/mainstyle.css">
</head>
<body>
    <!-- Navigation Bar -->
   <nav class="navbar">
   
<a href="index.jsp" class="logo">
    <img src="./asset/backend_logo.png" alt="FitBegin Logo" style="height: 50px">
</a>
    <div class="search-container">
        <form action="${pageContext.request.contextPath}/exercises" method="get">
            <input type="hidden" name="action" value="search">
            <input type="text" name="q" class="search-input" placeholder="${lang == 'ko' ? '검색..' : 'Search..'}">
        </form>
    </div>
    <div class="nav-right">
    <div class="lang-selector">
    <form action="${pageContext.request.contextPath}/language" method="get">
        <input type="radio" id="lang-en" name="lang" value="en" class="lang-radio" 
            <c:if test="${lang != 'ko'}">checked</c:if>
            onchange="this.form.submit()">
        <label for="lang-en" class="lang-label">EN</label>
        /
        <input type="radio" id="lang-kr" name="lang" value="ko" class="lang-radio" 
            <c:if test="${lang == 'ko'}">checked</c:if>
            onchange="this.form.submit()">
        <label for="lang-kr" class="lang-label">KR</label>
    </form>
</div>
    
    
        <a href="${pageContext.request.contextPath}/store" class="nav-button">${lang == 'ko' ? '스토어' : 'STORE'}</a>
        <a href="${pageContext.request.contextPath}/exercises" class="nav-button">${lang == 'ko' ? '운동' : 'EXERCISES'}</a>
        <c:choose>
            <c:when test="${not empty sessionScope.user}">
                <c:if test="${sessionScope.user.admin}">
                    <a href="${pageContext.request.contextPath}/admin/users" class="nav-button">${lang == 'ko' ? '회원관리' : 'usermanage'}</a>
                </c:if>
                <a href="${pageContext.request.contextPath}/mypage" class="nav-button">${lang == 'ko' ? '마이페이지' : 'mypage'}</a>
                <span class="nav-button" style="color: white;">${sessionScope.user.name}${lang == 'ko' ? '님' : ''}</span>
                <a href="${pageContext.request.contextPath}/login?action=logout" class="nav-button login">${lang == 'ko' ? '로그아웃' : 'log out'}</a>
            </c:when>
            <c:otherwise>
                <a href="${pageContext.request.contextPath}/register" class="nav-button">${lang == 'ko' ? '회원가입' : 'SIGN UP'}</a>
                <a href="${pageContext.request.contextPath}/login" class="nav-button login">${lang == 'ko' ? '로그인' : 'LOG IN'}</a>
            </c:otherwise>
        </c:choose>
    </div>
</nav>
