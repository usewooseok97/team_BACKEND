<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>Find Password</title>
    <link rel="stylesheet" href="css/formstyle.css">
</head>
<body>
<%@ include file="header.jsp" %>


<div class="form-container">
    <h2 class="form-title">${lang == 'ko' ? '비밀번호 찾기' : 'FIND PASSWORD'}</h2>

    <form action="findpassword" method="post">
        <div class="form-group">
            <label class="form-label" for="username">${lang == 'ko' ? '아이디' : 'ID'}<span class="required">*</span></label>
            <input type="text" id="username" name="username" class="form-input"
                   placeholder="${lang == 'ko' ? '아이디를 입력하세요.' : 'Enter your ID'}" required>
        </div>

        <div class="form-group">
            <label class="form-label" for="email">${lang == 'ko' ? '이메일' : 'EMAIL'}<span class="required">*</span></label>
            <input type="email" id="email" name="email" class="form-input"
                   placeholder="${lang == 'ko' ? '이메일을 입력하세요.' : 'Enter your Email'}" required>
        </div>

        <button type="submit" class="form-button">${lang == 'ko' ? '비밀번호 찾기' : 'FIND PASSWORD'}</button>
    </form>

    <div class="form-link">
        ${lang == 'ko' ? '로그인으로 돌아가기' : 'GO BACK TO LOGIN'} <a href="login.jsp">${lang == 'ko' ? '로그인' : 'LOG IN'}</a>
    </div>
</div>

</body>
</html>
<%@ include file="footer.jsp" %>

