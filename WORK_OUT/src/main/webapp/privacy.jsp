<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="header.jsp" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${lang == 'ko' ? '정책' : 'Policy'}</title>
    <link rel="stylesheet" href="css/policystyle.css">
</head>
<body>
    <!-- Privacy Policy -->
    <div class="policy-container">
        <h1 class="policy-title">${lang == 'ko' ? '개인정보 처리방침' : 'Privacy Policy'}</h1>

        <div class="policy-content">
            <p>
                ${lang == 'ko' 
                    ? '본 개인정보 처리방침은 사용자의 개인정보를 어떻게 수집, 이용, 보호하는지 설명합니다.' 
                    : 'This Privacy Policy explains how we collect, use, and protect your personal information.'}
            </p>

            <h2>${lang == 'ko' ? '1. 정보 수집' : '1. Information Collection'}</h2>
            <p>
                ${lang == 'ko' 
                    ? '저희는 이름, 이메일 주소, 서비스 이용 기록, 쿠키와 같은 개인정보를 수집할 수 있습니다.' 
                    : 'We may collect personal information such as name, email address, service usage records, and cookies.'}
            </p>

            <h2>${lang == 'ko' ? '2. 개인정보 이용 목적' : '2. Purpose of Use'}</h2>
            <p>
                ${lang == 'ko' 
                    ? '수집된 정보는 서비스 품질 향상, 고객 지원 제공, 사용자 경험 개선에 사용됩니다.' 
                    : 'Collected information is used to improve service quality, provide customer support, and enhance user experience.'}
            </p>

            <h2>${lang == 'ko' ? '3. 정보 보호 정책' : '3. Information Protection'}</h2>
            <p>
                ${lang == 'ko' 
                    ? '저희는 사용자의 개인정보를 보호하기 위해 다양한 보안 조치를 적용하고 있습니다.' 
                    : 'We apply various security measures to protect user personal information.'}
            </p>

            <h2>${lang == 'ko' ? '4. 문의처' : '4. Contact'}</h2>
            <p>
                ${lang == 'ko' 
                    ? '개인정보 처리방침에 대한 문의 사항이 있으면 언제든지 연락해 주세요.' 
                    : 'If you have any questions about our Privacy Policy, please contact us anytime.'}
            </p>
        </div>
    </div>

    <!-- Terms of Service -->
    <div class="policy-container">
        <h1 class="policy-title">${lang == 'ko' ? '서비스 이용약관' : 'Terms of Service'}</h1>

        <div class="policy-content">
            <h2>${lang == 'ko' ? '1. 약관 동의' : '1. Acceptance of Terms'}</h2>
            <p>
                ${lang == 'ko' 
                    ? '본 서비스를 이용함으로써 귀하는 본 이용약관에 동의하는 것입니다.' 
                    : 'By using our service, you agree to these Terms of Service.'}
            </p>

            <h2>${lang == 'ko' ? '2. 사용자 책임' : '2. User Responsibilities'}</h2>
            <p>
                ${lang == 'ko' 
                    ? '사용자는 모든 관련 법규를 준수해야 하며 금지된 활동을 피해야 합니다.' 
                    : 'Users must comply with all applicable laws and avoid prohibited activities.'}
            </p>

            <h2>${lang == 'ko' ? '3. 서비스 가용성' : '3. Service Availability'}</h2>
            <p>
                ${lang == 'ko' 
                    ? '저희는 서비스의 중단 없는 제공을 보장하지 않으며, 기능을 수정하거나 중단할 수 있습니다.' 
                    : 'We do not guarantee uninterrupted service and may modify or discontinue features.'}
            </p>

            <h2>${lang == 'ko' ? '4. 책임의 제한' : '4. Limitation of Liability'}</h2>
            <p>
                ${lang == 'ko' 
                    ? '저희는 서비스 이용으로 인해 발생한 손해에 대해 책임을 지지 않습니다.' 
                    : 'We are not responsible for any damages arising from use of our service.'}
            </p>
        </div>
    </div>

    <!-- Medical Disclaimer -->
    <div class="policy-container">
        <h1 class="policy-title">${lang == 'ko' ? '의료 면책 조항' : 'Medical Disclaimer'}</h1>

        <div class="policy-content">
            <p>
                ${lang == 'ko' 
                    ? '본 사이트에서 제공하는 정보는 교육 목적으로만 제공됩니다.' 
                    : 'The information provided on this site is for educational purposes only.'}
            </p>

            <h2>${lang == 'ko' ? '전문적 조언 아님' : 'Not Professional Advice'}</h2>
            <p>
                ${lang == 'ko' 
                    ? '이 콘텐츠는 자격을 갖춘 의료 전문가와의 상담을 대체하지 않습니다.' 
                    : 'This content does not replace consultation with qualified healthcare professionals.'}
            </p>

            <h2>${lang == 'ko' ? '건강 위험' : 'Health Risks'}</h2>
            <p>
                ${lang == 'ko' 
                    ? '식이요법, 운동 또는 생활 습관을 변경하기 전에 항상 의사와 상담하세요.' 
                    : 'Always consult a doctor before beginning any diet, exercise, or lifestyle change.'}
            </p>

            <h2>${lang == 'ko' ? '의료 관계 없음' : 'No Medical Relationship'}</h2>
            <p>
                ${lang == 'ko' 
                    ? '본 사이트 이용이 의사-환자 관계를 성립시키지 않습니다.' 
                    : 'Using this site does not establish a doctor–patient relationship.'}
            </p>
        </div>
    </div>

    <!-- Cookie Policy -->
    <div class="policy-container">
        <h1 class="policy-title">${lang == 'ko' ? '쿠키 정책' : 'Cookie Policy'}</h1>

        <div class="policy-content">
            <h2>${lang == 'ko' ? '1. 쿠키란?' : '1. What Are Cookies?'}</h2>
            <p>
                ${lang == 'ko' 
                    ? '쿠키는 사용자 경험을 향상시키기 위해 기기에 저장되는 작은 텍스트 파일입니다.' 
                    : 'Cookies are small text files stored on your device to enhance user experience.'}
            </p>

            <h2>${lang == 'ko' ? '2. 쿠키 사용 방법' : '2. How We Use Cookies'}</h2>
            <p>
                ${lang == 'ko' 
                    ? '저희는 분석, 개인화 및 필수 기능을 위해 쿠키를 사용합니다.' 
                    : 'We use cookies for analytics, personalization, and essential functionality.'}
            </p>

            <h2>${lang == 'ko' ? '3. 쿠키 관리' : '3. Managing Cookies'}</h2>
            <p>
                ${lang == 'ko' 
                    ? '브라우저 설정을 통해 쿠키를 비활성화할 수 있습니다.' 
                    : 'You can disable cookies through your browser settings.'}
            </p>

            <h2>${lang == 'ko' ? '4. 문의' : '4. Contact'}</h2>
            <p>
                ${lang == 'ko' 
                    ? '쿠키 정책에 대해 질문이 있으시면 연락해 주세요.' 
                    : 'If you have questions about our Cookie Policy, please contact us.'}
            </p>
        </div>
    </div>
</body>
</html>
<%@ include file="footer.jsp" %>