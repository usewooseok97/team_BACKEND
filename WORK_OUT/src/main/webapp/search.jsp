<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>검색 - WORK OUT</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
    <style>
        .coming-soon {
            text-align: center;
            padding: 200px 20px;
        }
        .coming-soon h1 {
            font-size: 48px;
            color: #333;
            margin-bottom: 20px;
        }
        .coming-soon p {
            font-size: 18px;
            color: #666;
        }
        .search-query {
            color: #667eea;
            font-weight: bold;
        }
    </style>
</head>
<body>
    <!-- Navigation Bar -->
    <nav class="navbar">
        <a href="index.jsp" class="logo">🏋️</a>
        <div class="nav-right">
            <a href="index.jsp" class="nav-button">홈으로</a>
        </div>
    </nav>

    <div class="coming-soon">
        <h1>🔍 검색</h1>
        <c:if test="${not empty param.q}">
            <p><span class="search-query">"${param.q}"</span> 에 대한 검색 결과</p>
        </c:if>
        <p>검색 기능은 준비 중입니다.</p>
    </div>
</body>
</html>
