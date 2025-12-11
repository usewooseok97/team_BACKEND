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
    <title>WORK OUT - 운동 플랫폼</title>
    <link rel="stylesheet" type="text/css" href="css/mainstyle.css">
    <style>
        /* 언어 토글 버튼 스타일 */
        .language-toggle {
            position: fixed;
            top: 80px;
            right: 20px;
            z-index: 1000;
        }

        .lang-btn {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            border: none;
            border-radius: 25px;
            padding: 10px 20px;
            color: white;
            font-weight: bold;
            cursor: pointer;
            text-decoration: none;
            display: inline-flex;
            align-items: center;
            gap: 10px;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
            transition: all 0.3s ease;
        }

        .lang-btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 6px 20px rgba(0, 0, 0, 0.3);
        }

        .lang-icon {
            font-size: 18px;
        }
    </style>
</head>
<body>
    <!-- Navigation Bar -->
   <nav class="navbar">
   
    <a href="index.jsp" class="logo">🏋️</a>
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
                    <a href="${pageContext.request.contextPath}/admin/users" class="nav-button">회원관리</a>
                </c:if>
                <a href="${pageContext.request.contextPath}/mypage" class="nav-button">마이페이지</a>
                <span class="nav-button" style="color: white;">${sessionScope.user.name}님</span>
                <a href="${pageContext.request.contextPath}/login?action=logout" class="nav-button login">로그아웃</a>
            </c:when>
            <c:otherwise>
                <a href="${pageContext.request.contextPath}/register" class="nav-button">${lang == 'ko' ? '회원가입' : 'SIGN UP'}</a>
                <a href="${pageContext.request.contextPath}/login" class="nav-button login">${lang == 'ko' ? '로그인' : 'LOG IN'}</a>
            </c:otherwise>
        </c:choose>
    </div>
</nav>

<!--
    언어 토글 버튼

    동작 방식:
    1. 현재 언어가 한국어(ko)이면 → 영어(en) 버튼 표시
    2. 현재 언어가 영어(en)이면 → 한국어(ko) 버튼 표시
    3. 클릭 시 LanguageServlet (/language?lang=XX)로 요청
    4. LanguageServlet이 세션의 language 속성 변경
    5. 리다이렉트 규칙:
       - 상세 페이지(action=detail)에서 전환 → exercises 목록으로
       - 그 외 페이지에서 전환 → 같은 페이지 유지
       - Referer 없으면 → index.jsp로

    언어에 따른 DB 조회:
    - 영어(en): exercises, exerciseDetails 컬렉션 사용
    - 한국어(ko): k_exercises, k_exercisesDetails 컬렉션 사용
    - images 컬렉션: en일 때 'id' 필드, ko일 때 'kid' 필드로 조회

    버튼 스타일 변경:
    - .lang-btn 클래스: Line 28-42에 정의
    - .lang-icon: Line 49-51에 정의
    - position: fixed, top: 80px, right: 20px (Line 22-25)
-->
