<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>검색 - WORK OUT</title>
    <link rel="stylesheet" type="text/css" href="css/searchstyle.css">

</head>
<%@ include file="header.jsp" %>
<body>
    <div class="coming-soon">
        <h1>🔍 SEARCH</h1>
        <c:if test="${not empty param.q}">
            <p><span class="search-query">"${param.q}"</span> 에 대한 검색 결과</p>
        </c:if>
        <p>SEARCH IN PREPARATION. PLEASE STAY TUNED.</p>
    </div>
    <%@ include file="footer.jsp" %>
</body>
</html>
