# WORK OUT - 운동 플랫폼

운동 부위별 운동 정보, 스포츠 종류, 운동 기구 정보 및 할인 상품을 제공하는 웹 애플리케이션입니다.

## 프로젝트 구조

```
WORK_OUT/
├── src/
│   └── main/
│       ├── java/
│       │   ├── data/              # 데이터 제공 계층
│       │   │   └── CategoryData.java
│       │   ├── dto/               # 데이터 전송 객체
│       │   │   └── ProductDTO.java
│       │   └── model/             # 도메인 모델
│       │       └── CategoryItem.java
│       └── webapp/
│           ├── css/
│           │   └── style.css      # 전체 스타일시트
│           ├── WEB-INF/
│           │   └── lib/           # JSTL 라이브러리
│           └── index.jsp          # 메인 페이지
└── README.md
```

## 코드 구조 및 작동 원리

### 1. 아키텍처 패턴

이 프로젝트는 **3-Layer Architecture**를 따릅니다:

```
[Presentation Layer] → [Business Logic Layer] → [Data Layer]
     (JSP)                    (DTO/Model)          (CategoryData)
```

### 2. 계층별 설명

#### Data Layer (데이터 계층)
- **[CategoryData.java](WORK_OUT/src/main/java/data/CategoryData.java)**: 정적 데이터 제공 클래스
  - `getBodyParts()`: 신체 부위별 운동 데이터 (Triceps, Chest, Biceps 등)
  - `getSports()`: 스포츠 종류 데이터 (Swimming, Soccer, Basketball 등)
  - `getMachines()`: 운동 기구 데이터 (Lat Pulldown, Chest Press 등)
  - `getProducts()`: 할인 상품 데이터
  - `filterByCategory()`: 카테고리별 필터링 메소드

#### Model Layer (모델 계층)
- **[CategoryItem.java](WORK_OUT/src/main/java/model/CategoryItem.java)**: 카테고리 아이템 모델
  - 속성: `name` (이름), `icon` (이모지), `category` (분류)
  - 운동 부위, 스포츠, 기구를 표현하는 공통 모델

- **[ProductDTO.java](WORK_OUT/src/main/java/dto/ProductDTO.java)**: 상품 데이터 전송 객체
  - 속성: `brand`, `name`, `originalPrice`, `price`, `image`
  - `getDiscountRate()`: 할인율 자동 계산 메소드

#### Presentation Layer (프레젠테이션 계층)
- **[index.jsp](WORK_OUT/src/main/webapp/index.jsp)**: 메인 페이지 뷰
  - JSTL을 사용한 동적 렌더링
  - `CategoryData`에서 데이터를 가져와 `pageContext`에 저장
  - JSTL의 `<c:forEach>`와 `<c:if>`를 사용하여 데이터 출력

### 3. 데이터 흐름

```
1. 사용자가 index.jsp 접속
   ↓
2. JSP 스크립틀릿(<% %>)이 CategoryData 메소드 호출
   ↓
3. CategoryData가 List<CategoryItem>, List<ProductDTO> 반환
   ↓
4. pageContext.setAttribute()로 데이터를 JSP 컨텍스트에 저장
   ↓
5. JSTL 태그가 데이터를 읽어 HTML로 렌더링
   ↓
6. CSS로 스타일링된 최종 페이지가 브라우저에 표시
```

### 4. 주요 기술 스택

- **Backend**: Java (JSP, JSTL 3.0)
- **Frontend**: HTML5, CSS3
- **Server**: Apache Tomcat
- **Architecture**: MVC Pattern (Model-View-Controller)

## 웹 페이지 기능

### 주요 기능

#### 1. 네비게이션 바
- 로고 및 홈 링크
- 검색 기능 (`search.jsp`로 연결)
- 언어 선택 (EN/KR) - 라디오 버튼 방식
- 상단 메뉴: Store, Register, Login 링크

#### 2. Hero Section
- 대형 "WORK OUT" 타이틀
- 중앙 검색 바 (검색 아이콘 포함)
- 그라데이션 배경 효과

#### 3. BODY 섹션
신체 부위별 운동 정보를 제공합니다.

**탭 구조**:
- **ALL**: 모든 신체 부위 (13개)
- **UPPER**: 상체 운동 (Triceps, Chest, Biceps, Abs, Back, Shoulders, Trapezius, Forearms)
- **LOWER**: 하체 운동 (Hamstrings, Quads, Glutes, Adductors, Abductors)

**구현 방식**:
- 라디오 버튼과 CSS의 `:checked` 선택자를 사용한 탭 전환
- JavaScript 없이 순수 CSS로 구현
- 그리드 레이아웃으로 아이콘과 이름 표시

#### 4. SPORTS 섹션
다양한 스포츠 종류를 카테고리별로 분류합니다.

**탭 구조**:
- **ALL**: 모든 스포츠 (13개)
- **WATER**: 수상 스포츠 (Swimming)
- **GROUND**: 야외 스포츠 (Soccer, Basketball, Tennis, Golf, Football, Volleyball)
- **HOME**: 실내 스포츠 (Darts, Boxing, Table Tennis, Pool)
- **ETC**: 기타 스포츠 (Fencing, Archery)

#### 5. MACHINE 섹션
운동 기구 및 머신 정보를 제공합니다.

**탭 구조**:
- **ALL**: 모든 기구 (13개)
- **UPPER**: 상체 기구 (Lat Pulldown, Chest Press, Bicep Curl, Ab Machine, Row Machine, Shoulder Press)
- **LOWER**: 하체 기구 (Leg Press, Leg Extension, Glute Machine, Leg Curl)
- **ETC**: 기타 기구 (Cable Machine, Smith Machine, Treadmill)

#### 6. Today's Discount 섹션
할인 상품 정보를 카드 형식으로 표시합니다.

**표시 정보**:
- 브랜드명 (GORNATION, Rogue 등)
- 상품명
- 정가 (취소선 표시)
- 할인가 (빨간색 강조)
- 북마크 아이콘

**상품 예시**:
- GORNATION Dip Belt: $100 → $88
- Rogue Monster Bands: $25 → $22
- GORNATION Premium Pull Up Station: $122 → $102
- Rogue Dumbbells: $50 → $45

#### 7. Footer
- 정책 링크 (Privacy Policy, Terms of Service, Medical Disclaimer, Cookie Policy)
- 의료 면책 조항
- 저작권 정보 (© 2023 2조 강우석 이준영 조윤재)

## index.jsp 현재 구현 내용

현재 [index.jsp](WORK_OUT/src/main/webapp/index.jsp)는 다음과 같이 구성되어 있습니다:

### 데이터 로딩 방식
```jsp
<%
    // CategoryData를 사용하여 데이터 가져오기
    pageContext.setAttribute("bodyParts", CategoryData.getBodyParts());
    pageContext.setAttribute("sports", CategoryData.getSports());
    pageContext.setAttribute("machines", CategoryData.getMachines());
    pageContext.setAttribute("products", CategoryData.getProducts());
%>
```

### 동적 렌더링 방식
JSTL 태그를 사용한 반복 및 조건 처리:
```jsp
<!-- 모든 아이템 출력 -->
<c:forEach var="item" items="${bodyParts}">
    <div class="category-item">
        <div class="category-icon">${item.icon}</div>
        <div class="category-name">${item.name}</div>
    </div>
</c:forEach>

<!-- 카테고리별 필터링 -->
<c:if test="${item.category == 'upper'}">
    <!-- UPPER 카테고리만 출력 -->
</c:if>
```

### 탭 전환 메커니즘
라디오 버튼과 CSS를 활용한 상태 관리:
```html
<!-- 라디오 버튼 (숨김 처리) -->
<input type="radio" id="body-all" name="body-tab" class="tab-radio" checked>
<input type="radio" id="body-upper" name="body-tab" class="tab-radio">
<input type="radio" id="body-lower" name="body-tab" class="tab-radio">

<!-- 탭 레이블 -->
<label for="body-all" class="category-tab">ALL</label>
<label for="body-upper" class="category-tab">UPPER</label>
<label for="body-lower" class="category-tab">LOWER</label>

<!-- 각 탭에 해당하는 그리드 -->
<div class="category-grid" id="bodyGrid-all">...</div>
<div class="category-grid" id="bodyGrid-upper">...</div>
<div class="category-grid" id="bodyGrid-lower">...</div>
```

CSS에서 `:checked` 선택자로 표시할 그리드를 제어합니다.

## 특징

1. **JavaScript 없는 구현**: 모든 인터랙션을 CSS와 HTML만으로 구현
2. **반응형 그리드**: `repeat(auto-fill, minmax())`를 사용한 유연한 레이아웃
3. **컴포넌트 재사용**: `CategoryItem` 모델을 다양한 섹션에서 재사용
4. **타입 안정성**: DTO와 Model을 통한 명확한 데이터 구조
5. **서버 사이드 렌더링**: JSP를 통한 초기 렌더링으로 빠른 페이지 로드

## 실행 방법

1. Apache Tomcat 서버 실행
2. 프로젝트를 Tomcat에 배포
3. 브라우저에서 `http://localhost:8080/WORK_OUT/` 접속

## 향후 개선 방향

- 데이터베이스 연동 (현재는 하드코딩된 데이터 사용)
- 검색 기능 구현 (`search.jsp`)
- 회원가입/로그인 기능 구현
- 상품 상세 페이지
- 장바구니 및 결제 시스템
- 다국어 지원 (EN/KR 실제 적용)
