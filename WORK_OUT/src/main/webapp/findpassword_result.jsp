<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="header.jsp" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Password Find Result</title>
    <link rel="stylesheet" href="css/formstyle.css">
    <style>
        .form-container {
            max-width: 500px;
            margin: 100px auto;
            padding: 30px;
            background: #fff;
            border-radius: 10px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.1);
            text-align: center;
        }
        .form-title {
            font-size: 24px;
            margin-bottom: 20px;
            color: #333;
        }
        .message {
            font-size: 16px;
            margin-bottom: 30px;
            color: #555;
        }
        .back-button {
            display: inline-block;
            padding: 10px 25px;
            background-color: #e63946;
            color: white;
            border-radius: 20px;
            text-decoration: none;
            transition: opacity 0.3s;
        }
        .back-button:hover {
            opacity: 0.8;
        }
    </style>
</head>
<body>
    <div class="form-container">
        <h2 class="form-title">Password Find Result</h2>
        <p class="message">${message}</p>
        <a class="back-button" href="login.jsp">BACK TO LOGIN</a>
    </div>
</body>
</html>
