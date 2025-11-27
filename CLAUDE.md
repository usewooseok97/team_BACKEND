나는 몽고 db를 외부에서 api로 받은 데이터를 db에 저장한다음에 그걸가지고 java에서 보여주게 하고싶어 이걸 할수 있는 md를 만들어줘

외부 api

HttpRequest request = HttpRequest.newBuilder()

.uri(URI.create("https://exercisedb.p.rapidapi.com/exercises/targetList"))

.header("x-rapidapi-key", "1a077de20bmshdbdbe3f303aa16dp143e78jsn02e222a21c6a")

.header("x-rapidapi-host", "exercisedb.p.rapidapi.com")

.method("GET", HttpRequest.BodyPublishers.noBody())

.build();

HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

System.out.println(response.body());

db 스키마

Exercise schema

{

"id": "string",

"name": "string",

"bodyPart": "string",

"target": "string",

"equipment": "string",

"secondaryMuscles": ["string"],

"instructions": ["string"],

"description": "string",

"difficulty": "beginner | intermediate | advanced",

"category": "strength | cardio | mobility | balance | stretching | plyometrics | rehabilitation"

}

api키는 .env 파일에 있어

현재는 mongo compress와 atlas와 연결만 되어있고 java와 연결 안되어있음

현재 환경 java , servlet , jsp

구조는 mvc패턴으로

만들때 안의 구조를 모두 확인한후 시작해