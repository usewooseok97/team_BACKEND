<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원 관리 - WORK OUT</title>
<link rel="stylesheet" type="text/css"
      href="${pageContext.request.contextPath}/admin/css/adminstyle.css">
</head>

<body>
    <nav class="navbar">
        <a href="${pageContext.request.contextPath}/index.jsp" class="logo">
            <img src="${pageContext.request.contextPath}/asset/backend_logo.png" alt="FitBegin Logo">
        </a>
        
        <div class="nav-right">
            <span style="color: white; margin-right: 20px;">관리자: ${sessionScope.user.name}</span>
            <a href="${pageContext.request.contextPath}/login?action=logout" class="nav-button login">
                로그아웃
            </a>
        </div>
    </nav>
</body>
</html>
