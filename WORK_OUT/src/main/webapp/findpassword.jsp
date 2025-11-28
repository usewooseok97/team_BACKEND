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
    <h2 class="form-title">FIND PASSWORD</h2>

    <form action="findpassword" method="post">
        <div class="form-group">
            <label class="form-label" for="username">ID<span class="required">*</span></label>
            <input type="text" id="username" name="username" class="form-input"
                   placeholder="ENTER YOUR ID" required>
        </div>

        <div class="form-group">
            <label class="form-label" for="email">EMAIL<span class="required">*</span></label>
            <input type="email" id="email" name="email" class="form-input"
                   placeholder="ENTER YOUR EMAIL" required>
        </div>

        <button type="submit" class="form-button">FIND PASSWORD</button>
    </form>

    <div class="form-link">
        GO BACK TO LOGIN? <a href="login.jsp">LOGIN</a>
    </div>
</div>

</body>
</html>
<%@ include file="footer.jsp" %>

