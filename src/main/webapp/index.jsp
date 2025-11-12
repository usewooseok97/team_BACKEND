<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>홈</title>
</head>
<body>

<%@ include file="header.jsp" %>


<%

  if(lo != null){
	  //로그인 상태
%>
		<form action="logout.do" method="post">
			<input type="submit" value="로그아웃"/>
		</form>
<%	  
  } else {
	  //로그아웃 상태
%>
<form action="login.do" method="get">
	아이디 : <input type="text" name="id"><br>
	암호 : <input type="text" name="pw"><br>
	<input type="submit"><input type="reset">
</form>
<% 
  }
%>

<hr>
<h1>저희 웹사이트에 오신 걸 환영</h1>

</body>
</html>