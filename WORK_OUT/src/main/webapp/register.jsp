<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>회원가입 - WORK OUT</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
    <style>
        .form-container {
            max-width: 500px;
            margin: 100px auto;
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
        .form-label .required {
            color: red;
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
    </style>
</head>
<body>
    <!-- Navigation Bar -->
    <nav class="navbar">
        <a href="index.jsp" class="logo">🏋️</a>
        <div class="nav-right">
            <a href="login" class="nav-button">로그인</a>
        </div>
    </nav>

    <div class="form-container">
        <h2 class="form-title">회원가입</h2>

        <c:if test="${not empty error}">
            <div class="error-message">${error}</div>
        </c:if>

        <form action="register" method="post">
            <div class="form-group">
                <label class="form-label" for="username">아이디 <span class="required">*</span></label>
                <input type="text" id="username" name="username" class="form-input"
                       placeholder="아이디를 입력하세요" required>
            </div>

            <div class="form-group">
                <label class="form-label" for="password">비밀번호 <span class="required">*</span></label>
                <input type="password" id="password" name="password" class="form-input"
                       placeholder="비밀번호를 입력하세요" required>
            </div>

            <div class="form-group">
                <label class="form-label" for="passwordConfirm">비밀번호 확인 <span class="required">*</span></label>
                <input type="password" id="passwordConfirm" name="passwordConfirm" class="form-input"
                       placeholder="비밀번호를 다시 입력하세요" required>
            </div>

            <div class="form-group">
                <label class="form-label" for="name">이름 <span class="required">*</span></label>
                <input type="text" id="name" name="name" class="form-input"
                       placeholder="이름을 입력하세요" required>
            </div>

            <div class="form-group">
                <label class="form-label" for="email">이메일 <span class="required">*</span></label>
                <input type="email" id="email" name="email" class="form-input"
                       placeholder="이메일을 입력하세요" required>
            </div>

            <div class="form-group">
                <label class="form-label" for="phone">전화번호</label>
                <input type="tel" id="phone" name="phone" class="form-input"
                       placeholder="전화번호를 입력하세요 (선택)">
            </div>

            <button type="submit" class="form-button">회원가입</button>
        </form>

        <div class="form-link">
            이미 계정이 있으신가요? <a href="login">로그인</a>
        </div>
    </div>
</body>
</html>
