<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Footer -->
<footer class="footer">
    <div class="footer-links">
        <a href="privacy.jsp">${lang == 'ko' ? '정책' : 'POLICY'}</a> |
        <a href="customerService.jsp">${lang == 'ko' ? '고객센터' : 'CUSTOMER SERVICE'}</a>
    </div>

    <!-- SNS & Email Links using Font Awesome -->
    <div class="footer-social">
        <a href="https://www.instagram.com/your_instagram" target="_blank">
            <i class="fab fa-instagram"></i>
        </a>
        <a href="mailto:anam0409@dongyang.ac.kr">
            <i class="fas fa-envelope"></i>
        </a>
        <a href="https://www.facebook.com/your_facebook" target="_blank">
            <i class="fab fa-facebook"></i>
        </a>
    </div>

    <div class="footer-copyright">
       ${lang == 'ko' ? '© 2025 동양미래대학교 컴퓨터소프트웨어공학과 강우석 이준영 조윤재' : '© 2025 DONGYANG MIRAE UNIVERSITY BACKEND TEAM-02 WOOSEOK KANG JOONYEONG LEE YOONJAE CHO.'}  <br>  ${lang == 'ko' ? '저작권 법에 의해 보호됩니다.' : 'ALL RIGHTS RESERVED'}
    </div>
</footer>

<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">

<style>
.footer-social {
    margin: 10px 0;
    text-align: center; 
}

.footer-social a {
    color: #555; 
    font-size: 24px;
    margin: 0 10px;
    transition: color 0.3s, transform 0.3s;
}

.footer-social a:hover {
    color: #ffffff; 
    transform: scale(1.2); 
}

</style>
