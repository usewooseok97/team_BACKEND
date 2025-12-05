<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>WORK OUT - 운동 플랫폼</title>
    <link rel="stylesheet" type="text/css" href="css/mainstyle.css">
</head>
<body>
    <!-- Navigation Bar -->
   <nav class="navbar">
    <a href="index.jsp" class="logo">🏋️</a>
    <div class="search-container">
        <form action="${pageContext.request.contextPath}/exercises" method="get">
            <input type="hidden" name="action" value="search">
            <input type="text" name="q" class="search-input" placeholder="Search..">
        </form>
    </div>
    <div class="nav-right">
        <a href="store.jsp" class="nav-button">Store</a>
        <a href="${pageContext.request.contextPath}/exercises" class="nav-button">Exercises</a>
        <c:choose>
            <c:when test="${not empty sessionScope.user}">
                <c:if test="${sessionScope.user.admin}">
                    <a href="${pageContext.request.contextPath}/admin/users" class="nav-button">회원관리</a>
                </c:if>
                <a href="${pageContext.request.contextPath}/mypage" class="nav-button">마이페이지</a>
                <span class="nav-button" style="color: white;">${sessionScope.user.name}님</span>
                <a href="${pageContext.request.contextPath}/login?action=logout" class="nav-button login">로그아웃</a>
            </c:when>
            <c:otherwise>
                <a href="${pageContext.request.contextPath}/register" class="nav-button">SIGNUP</a>
                <a href="${pageContext.request.contextPath}/login" class="nav-button login">LOGIN</a>
            </c:otherwise>
        </c:choose>
    </div>
</nav>
