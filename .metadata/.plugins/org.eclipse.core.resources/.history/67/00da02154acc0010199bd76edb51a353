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
        <h1>CUSTOMER CENTRE</h1>
        <p>PLEASE READ THE FAQs BELOW OR CONTACT US USING THE FORM.</p>
    </div>

    <!-- FAQ Section -->
    <div class="cs-section">
        <h2>FAQ</h2>
        <div class="faq-item">
            <div class="faq-question">HOW DO I SIGN UP?</div>
            <div class="faq-answer">YOU CAN SIGN UP BY CLICKING 'SIGNUP' BUTTON ABOVE.</div>
        </div>
        <div class="faq-item">
            <div class="faq-question">I FORGOT MY PASSWORD.</div>
            <div class="faq-answer">YOU CAN FIND YOUR PASSWORD BY CLICKING 'FIND PASSWORD' BUTTON IN LOGIN PAGE.</div>
        </div>
        <div class="faq-item">
            <div class="faq-question">ARE THE EXERCISE PROGRAMS FREE OF CHARGE?</div>
            <div class="faq-answer">BASIC WORK-OUT PROGRAMS ARE FREE. HOWEVER, THERE ARE SOME EXCEPTIONAL PREMIUM PROGRAMS THAT YOU WOULD NEED TO PAY.</div>
        </div>
    </div>

    <!-- Inquiry Form Section (EmailJS 적용) -->
    <div class="cs-section">
        <h2>CONTACT US</h2>
        <form id="inquiryForm" class="inquiry-form">
            <label for="name">NAME</label>
            <input type="text" id="name" name="name" required>

            <label for="email">EMAIL</label>
            <input type="email" id="email" name="email" required>

            <label for="message">MESSAGE</label>
            <textarea id="message" name="message" rows="5" required></textarea>

            <button type="submit">SEND</button>
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
