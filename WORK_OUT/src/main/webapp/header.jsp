<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>WORK OUT - 운동 플랫폼</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
    <!-- Navigation Bar -->
    <nav class="navbar">
        <a href="#" class="logo">🏋️</a>
        <div class="search-container">
            <form action="search.jsp" method="get">
                <input type="text" name="q" class="search-input" placeholder="검색...">
            </form>
        </div>
        <div class="nav-right">
            <div class="lang-selector">
                <input type="radio" id="lang-en" name="language" class="lang-radio" checked>
                <label for="lang-en" class="lang-label">EN</label>
                /
                <input type="radio" id="lang-kr" name="language" class="lang-radio">
                <label for="lang-kr" class="lang-label">KR</label>
            </div>
            <a href="store.jsp" class="nav-button">store</a>

            <c:choose>
                <c:when test="${not empty sessionScope.user}">
                    <!-- 로그인 상태 -->
                    <c:if test="${sessionScope.user.role == 'ADMIN'}">
                        <a href="${pageContext.request.contextPath}/admin/users" class="nav-button">회원관리</a>
                    </c:if>
                    <a href="${pageContext.request.contextPath}/mypage" class="nav-button">마이페이지</a>
                    <span class="nav-button" style="color: white;">${sessionScope.user.name}님</span>
                    <a href="${pageContext.request.contextPath}/login?action=logout" class="nav-button login">로그아웃</a>
                </c:when>
                <c:otherwise>
                    <!-- 비로그인 상태 -->
                    <a href="${pageContext.request.contextPath}/register" class="nav-button">회원가입</a>
                    <a href="${pageContext.request.contextPath}/login" class="nav-button login">로그인</a>
                </c:otherwise>
            </c:choose>
        </div>
    </nav>

    <!-- Hero Section -->
    <section class="hero">
        <h1 class="hero-title">WORK<br>OUT</h1>
        <div class="hero-search">
            <form action="search.jsp" method="get">
                <input type="text" name="q" placeholder="search...">
            </form>
        </div>
    </section>
