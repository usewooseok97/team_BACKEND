# Exercise API 연동 설정 가이드

## 개요
ExerciseDB API에서 운동 데이터를 받아와 MongoDB에 저장하고, Java (Servlet/JSP) MVC 패턴으로 보여주는 시스템입니다.

## 생성된 파일 목록

### Java 클래스
1. **DTO (Data Transfer Object)**
   - `src/main/java/dto/ExerciseDTO.java` - 운동 데이터 객체

2. **DAO (Data Access Object)**
   - `src/main/java/dao/ExerciseDAO.java` - MongoDB CRUD 작업

3. **Service**
   - `src/main/java/service/ExerciseService.java` - API 호출 및 비즈니스 로직

4. **Controller**
   - `src/main/java/controller/ExerciseServlet.java` - 요청 처리 컨트롤러

### JSP 파일
1. `src/main/webapp/exercises.jsp` - 운동 목록 페이지
2. `src/main/webapp/exerciseDetail.jsp` - 운동 상세 페이지

### 설정 파일
1. `WORK_OUT/.env` - API 키 및 MongoDB 설정
2. `src/main/java/mongoutil/MongoConn.java` - MongoDB 연결 (업데이트됨)

## 필수 라이브러리 설치

### 1. JSON 라이브러리 추가
ExerciseService에서 JSON 파싱을 위해 필요합니다.

**org.json 라이브러리 다운로드:**
```bash
# Maven 사용 시 pom.xml에 추가
<dependency>
    <groupId>org.json</groupId>
    <artifactId>json</artifactId>
    <version>20231013</version>
</dependency>
```

**수동 다운로드:**
1. https://search.maven.org/artifact/org.json/json 방문
2. 최신 버전의 jar 파일 다운로드
3. `WORK_OUT/src/main/webapp/WEB-INF/lib/` 디렉토리에 복사

### 2. 이미 설치된 라이브러리
- MongoDB Driver Sync (5.2.1) ✓
- Jakarta Servlet JSP JSTL ✓

## MongoDB 설정

### 1. Docker Compose로 MongoDB 시작
```bash
cd C:/BACKEND_kang
docker-compose up -d
```

### 2. MongoDB 연결 정보
- Host: localhost
- Port: 27017
- Username: admin
- Password: admin1234
- Database: workout_db
- Collection: exercises

## 헤더 메뉴에 운동 링크 추가

`src/main/webapp/header.jsp` 파일을 수정하여 운동 페이지 링크를 추가하세요:

```jsp
<a href="store.jsp" class="nav-button">store</a>
<a href="${pageContext.request.contextPath}/exercises" class="nav-button">운동</a>
```

## 사용 방법

### 1. 서버 시작
1. MongoDB Docker 컨테이너 시작
2. Tomcat 서버 시작

### 2. 초기 데이터 동기화
1. 브라우저에서 `http://localhost:8080/WORK_OUT/exercises` 접속
2. "API에서 데이터 동기화" 버튼 클릭
3. 개수 입력란에 원하는 데이터 개수 입력 (기본값: 50)
4. 데이터가 MongoDB에 저장됨

### 3. 주요 기능

#### 운동 목록 보기
- URL: `/exercises` 또는 `/exercises?action=list`
- 모든 운동 목록을 카드 형태로 표시

#### 운동 상세 보기
- URL: `/exercises?action=detail&id={운동ID}`
- 운동의 상세 정보, 보조 근육, 운동 방법 등 표시

#### 운동 필터링
- 타겟 근육별 필터: `/exercises?action=filter&filterType=target&filterValue=biceps`
- 신체 부위별 필터: `/exercises?action=filter&filterType=bodyPart&filterValue=upper%20arms`
- 장비별 필터: `/exercises?action=filter&filterType=equipment&filterValue=barbell`

#### API 동기화
- POST `/exercises?action=sync&limit=100`
- 외부 API에서 최신 데이터를 가져와 기존 데이터를 대체

## API 엔드포인트

### ExerciseDB API 사용 엔드포인트
1. **타겟 목록**: `GET https://exercisedb.p.rapidapi.com/exercises/targetList`
2. **타겟별 운동**: `GET https://exercisedb.p.rapidapi.com/exercises/target/{target}`
3. **전체 운동**: `GET https://exercisedb.p.rapidapi.com/exercises?limit={limit}`

## 데이터 스키마

```javascript
{
  "id": "string",                    // 운동 고유 ID
  "name": "string",                  // 운동 이름
  "bodyPart": "string",              // 신체 부위 (예: upper arms)
  "target": "string",                // 주요 타겟 근육 (예: biceps)
  "equipment": "string",             // 필요 장비 (예: barbell)
  "secondaryMuscles": ["string"],    // 보조 근육 목록
  "instructions": ["string"],        // 운동 방법 단계별 설명
  "description": "string",           // 운동 설명
  "difficulty": "beginner | intermediate | advanced",
  "category": "strength | cardio | mobility | balance | stretching | plyometrics | rehabilitation"
}
```

## 주요 클래스 설명

### ExerciseService
```java
// 주요 메서드
- fetchTargetList(): 타겟 근육 목록 조회
- fetchAllExercises(limit): API에서 운동 데이터 조회
- syncExercisesFromAPI(limit): API 데이터를 DB에 동기화
- getAllExercises(): DB에서 모든 운동 조회
- getExercisesByTarget(target): 타겟별 운동 조회
```

### ExerciseDAO
```java
// 주요 메서드
- insert(exercise): 단일 운동 저장
- insertMany(exercises): 여러 운동 일괄 저장
- findById(id): ID로 운동 조회
- findAll(): 모든 운동 조회
- findByTarget(target): 타겟별 조회
- findByBodyPart(bodyPart): 신체부위별 조회
- findByEquipment(equipment): 장비별 조회
- delete(id): 운동 삭제
- deleteAll(): 모든 운동 삭제
```

### ExerciseServlet
```java
// 주요 액션
- list: 운동 목록 조회
- detail: 운동 상세 조회
- sync: API 데이터 동기화
- filter: 필터링 조회
```

## 문제 해결

### JSON 라이브러리 오류
```
Error: org.json.JSONArray not found
```
**해결:** org.json 라이브러리를 WEB-INF/lib에 추가

### MongoDB 연결 오류
```
Error: MongoDB Connection Failed
```
**해결:**
1. Docker Compose로 MongoDB 컨테이너 시작 확인
2. .env 파일의 연결 정보 확인

### API 호출 오류
```
Error: 401 Unauthorized
```
**해결:** .env 파일의 API 키 확인

## 향후 개선 사항

1. **페이징 처리**: 많은 데이터를 효율적으로 표시
2. **검색 기능**: 운동 이름으로 검색
3. **즐겨찾기**: 사용자별 즐겨찾기 운동 저장
4. **운동 루틴**: 여러 운동을 묶어서 루틴 생성
5. **이미지 표시**: API에서 제공하는 운동 이미지 표시
6. **캐싱**: 자주 조회되는 데이터 캐싱으로 성능 향상

## 테스트

### 1. MongoDB 연결 테스트
서버 로그에서 다음 메시지 확인:
```
MongoDB Connected Successfully to workout_db
ExerciseDAO initialized successfully
```

### 2. API 호출 테스트
브라우저에서 동기화 후 로그 확인:
```
Fetched 50 exercises from API
Successfully synced 50 exercises to DB
```

### 3. JSP 페이지 테스트
- 운동 목록 페이지가 정상적으로 로드되는지 확인
- 운동 카드 클릭 시 상세 페이지로 이동 확인
- 필터 기능 동작 확인

## 참고 자료
- ExerciseDB API 문서: https://rapidapi.com/justin-WFnsXH_t6/api/exercisedb
- MongoDB Java Driver: https://mongodb.github.io/mongo-java-driver/
- Jakarta Servlet: https://jakarta.ee/specifications/servlet/
