<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>로그인 - WORK OUT</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
    <style>
        .form-container {
            max-width: 450px;
            margin: 120px auto;
            padding: 40px;
            background: white;
            border-radius: 10px;
            box-shadow: 0 4px 6px rgba(0,0,0,0.1);
        }
        .form-title {
            font-size: 28px;
            font-weight: bold;
            text-align: center;
            margin-bottom: 30px;
            color: #333;
        }
        .form-group {
            margin-bottom: 20px;
        }
        .form-label {
            display: block;
            margin-bottom: 8px;
            font-weight: 600;
            color: #555;
        }
        .form-input {
            width: 100%;
            padding: 12px;
            border: 1px solid #ddd;
            border-radius: 5px;
            font-size: 14px;
            box-sizing: border-box;
        }
        .form-input:focus {
            outline: none;
            border-color: #4CAF50;
        }
        .form-button {
            width: 100%;
            padding: 14px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border: none;
            border-radius: 5px;
            font-size: 16px;
            font-weight: bold;
            cursor: pointer;
            margin-top: 10px;
        }
        .form-button:hover {
            opacity: 0.9;
        }
        .error-message {
            background: #ffebee;
            color: #c62828;
            padding: 12px;
            border-radius: 5px;
            margin-bottom: 20px;
            text-align: center;
        }
        .success-message {
            background: #e8f5e9;
            color: #2e7d32;
            padding: 12px;
            border-radius: 5px;
            margin-bottom: 20px;
            text-align: center;
        }
        .form-link {
            text-align: center;
            margin-top: 20px;
            color: #666;
        }
        .form-link a {
            color: #667eea;
            text-decoration: none;
            font-weight: 600;
        }
        .form-link a:hover {
            text-decoration: underline;
        }
        .test-accounts {
            background: #f5f5f5;
            padding: 15px;
            border-radius: 5px;
            margin-top: 20px;
            font-size: 12px;
            color: #666;
        }
        .test-accounts strong {
            color: #333;
        }
    </style>
</head>
<body>
    <!-- Navigation Bar -->
    <nav class="navbar">
        <a href="index.jsp" class="logo">🏋️</a>
        <div class="nav-right">
            <a href="register" class="nav-button">회원가입</a>
        </div>
    </nav>

    <div class="form-container">
        <h2 class="form-title">로그인</h2>

        <c:if test="${param.registered == 'true'}">
            <div class="success-message">회원가입이 완료되었습니다. 로그인해주세요.</div>
        </c:if>

        <c:if test="${not empty error}">
            <div class="error-message">${error}</div>
        </c:if>

        <form action="login" method="post">
            <div class="form-group">
                <label class="form-label" for="username">아이디</label>
                <input type="text" id="username" name="username" class="form-input"
                       placeholder="아이디를 입력하세요" required>
            </div>

            <div class="form-group">
                <label class="form-label" for="password">비밀번호</label>
                <input type="password" id="password" name="password" class="form-input"
                       placeholder="비밀번호를 입력하세요" required>
            </div>

            <button type="submit" class="form-button">로그인</button>
        </form>

        <div class="form-link">
            계정이 없으신가요? <a href="register">회원가입</a>
        </div>

        <!-- 테스트용 계정 정보 -->
        <div class="test-accounts">
            <strong>테스트 계정:</strong><br>
            관리자 - ID: admin / PW: admin123<br>
            일반유저 - ID: user1 / PW: user123
        </div>
    </div>
</body>
</html>
