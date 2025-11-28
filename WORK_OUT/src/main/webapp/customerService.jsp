<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>고객센터 - WORK OUT</title>
    <link rel="stylesheet" type="text/css" href="css/mainstyle.css">
    <style>
        .cs-container { max-width: 900px; margin: 0 auto; padding: 30px; }
        .cs-header { text-align: center; margin-bottom: 20px; }
        .cs-section { margin-bottom: 40px; }
        .cs-section h2 { margin-bottom: 10px; color: #333; }
        .faq-item { margin-bottom: 10px; }
        .faq-question { font-weight: bold; cursor: pointer; }
        .faq-answer { display: none; padding-left: 10px; }
        .faq-item.active .faq-answer { display: block; }
        .inquiry-form label { display: block; margin: 10px 0 5px; }
        .inquiry-form input, .inquiry-form textarea { width: 100%; padding: 8px; margin-bottom: 10px; }
        .inquiry-form button { padding: 10px 20px; background-color: #007bff; color: white; border: none; cursor: pointer; }
    </style>
</head>
<body>
<%@ include file="header.jsp" %>

<div class="cs-container">
    <div class="cs-header">
        <h1>고객센터</h1>
        <p>FAQ 혹은 문의하기를 사용해서 질문을 남겨주세요.</p>
    </div>

    <!-- FAQ Section -->
    <div class="cs-section">
        <h2>FAQ</h2>
        <div class="faq-item">
            <div class="faq-question">회원가입은 어떻게 하나요?</div>
            <div class="faq-answer">상단 메뉴에서 회원가입을 클릭하여 정보를 입력하면 가입 가능합니다.</div>
        </div>
        <div class="faq-item">
            <div class="faq-question">비밀번호를 잊어버렸어요.</div>
            <div class="faq-answer">로그인 페이지에서 비밀번호 찾기를 통해 재설정할 수 있습니다.</div>
        </div>
        <div class="faq-item">
            <div class="faq-question">운동 프로그램은 무료인가요?</div>
            <div class="faq-answer">기본 운동 프로그램은 무료로 제공되며, 일부 프리미엄 기능은 유료입니다.</div>
        </div>
    </div>

    <!-- Inquiry Form Section (EmailJS 적용) -->
    <div class="cs-section">
        <h2>문의하기</h2>
        <form id="inquiryForm" class="inquiry-form">
            <label for="name">이름</label>
            <input type="text" id="name" name="name" required>

            <label for="email">이메일</label>
            <input type="email" id="email" name="email" required>

            <label for="message">문의 내용</label>
            <textarea id="message" name="message" rows="5" required></textarea>

            <button type="submit">문의 보내기</button>
        </form>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/emailjs-com@3/dist/email.min.js"></script>
<script>
    emailjs.init('qdHIci-2csmpz9QPb'); //이준영 public key임
    document.querySelectorAll('.faq-question').forEach(q => {
        q.addEventListener('click', () => {
            const item = q.parentElement;
            item.classList.toggle('active');
        });
    });
    document.getElementById('inquiryForm').addEventListener('submit', function(event){
        event.preventDefault();

        emailjs.sendForm('service_849yl4e', 'template_xkaaqnl', this)
            .then(() => {
                alert('문의가 성공적으로 전송되었습니다!');
                this.reset();
            })
            .catch(err => {
                console.error('EmailJS 전송 실패:', err);
                alert('문의 전송에 실패했습니다. 다시 시도해주세요.');
            });
    });
</script>

<%@ include file="footer.jsp" %>
</body>
</html>
