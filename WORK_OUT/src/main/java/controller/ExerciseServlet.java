package controller;

import dto.NaverProductDTO;
import dto.ExerciseDTO;
import dto.ExerciseDetailDTO;
import dto.YouTubeVideoDTO;
import service.NaverShoppingService;
import service.ExerciseService;
import service.YouTubeVideoService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/exercises")
public class ExerciseServlet extends HttpServlet {
    private ExerciseService exerciseService;
    private NaverShoppingService naverShoppingService;
    private YouTubeVideoService youtubeVideoService;

    @Override
    public void init() throws ServletException {
        exerciseService = ExerciseService.getInstance();
        naverShoppingService = NaverShoppingService.getInstance();
        youtubeVideoService = YouTubeVideoService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");

        if (action == null) {
            action = "list";
        }

        switch (action) {
            case "list":
                listExercises(request, response);
                break;
            case "detail":
                showExerciseDetail(request, response);
                break;
            case "sync":
                syncExercises(request, response);
                break;
            case "syncFromDetails":
                syncExercisesFromDetails(request, response);
                break;
            case "syncImages":
                syncImagesFromExerciseDetails(request, response);
                break;
            case "filter":
                filterExercises(request, response);
                break;
            case "search":
                searchExercises(request, response);
                break;
            default:
                listExercises(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");

        if ("sync".equals(action)) {
            syncExercises(request, response);
        } else if ("syncFromDetails".equals(action)) {
            syncExercisesFromDetails(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/exercises");
        }
    }

    private void listExercises(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String language = getLanguage(request);

            // 페이지네이션 파라미터 처리
            int page = 1;
            int pageSize = 18;
            String pageParam = request.getParameter("page");
            if (pageParam != null && !pageParam.trim().isEmpty()) {
                try {
                    page = Integer.parseInt(pageParam);
                    if (page < 1) page = 1;
                } catch (NumberFormatException e) {
                    page = 1;
                }
            }

            // 전체 개수 조회
            long totalCount = exerciseService.getExerciseCount(language);
            int totalPages = (int) Math.ceil((double) totalCount / pageSize);

            // 페이지네이션된 운동 목록 조회
            List<ExerciseDTO> exercises = exerciseService.getAllExercises(language, page, pageSize);

            request.setAttribute("exercises", exercises);
            request.setAttribute("exerciseCount", exercises.size());
            request.setAttribute("totalCount", totalCount);
            request.setAttribute("currentPage", page);
            request.setAttribute("pageSize", pageSize);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("message", "총 " + totalCount + "개의 운동이 있습니다. (페이지 " + page + "/" + totalPages + ")");

            request.getRequestDispatcher("/exercises.jsp").forward(request, response);
        } catch (Exception e) {
            System.err.println("Error listing exercises: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "운동 목록을 불러오는 중 오류가 발생했습니다.");
            request.getRequestDispatcher("/exercises.jsp").forward(request, response);
        }
    }

    private void showExerciseDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String id = request.getParameter("id");

        if (id == null || id.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/exercises");
            return;
        }

        try {
            String language = getLanguage(request);  // 언어 가져오기
            ExerciseDetailDTO exerciseDetail = exerciseService.getExerciseDetailById(id, language);

            if (exerciseDetail != null) {
                request.setAttribute("exercise", exerciseDetail);

                // Fetch YouTube videos based on exercise data
                String videoSearchQuery = youtubeVideoService.determineSearchQuery(
                    exerciseDetail.getEquipment(),
                    exerciseDetail.getName()
                );

                if (!videoSearchQuery.isEmpty()) {
                    List<YouTubeVideoDTO> youtubeVideos =
                        youtubeVideoService.getVideos(videoSearchQuery, 3);
                    request.setAttribute("youtubeVideos", youtubeVideos);
                    request.setAttribute("videoSearchQuery", videoSearchQuery);
                }

                // Fetch Naver Shopping products based on exercise data
                String searchQuery = naverShoppingService.determineSearchQuery(
                    exerciseDetail.getEquipment(),
                    exerciseDetail.getName()
                );

                if (!searchQuery.isEmpty()) {
                    List<NaverProductDTO> naverProducts =
                        naverShoppingService.getProductsFromCombinedQuery(searchQuery, 5);
                    request.setAttribute("naverProducts", naverProducts);
                    request.setAttribute("searchQuery", searchQuery);
                }

                request.getRequestDispatcher("/exerciseDetail.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "해당 운동을 찾을 수 없습니다.");
                response.sendRedirect(request.getContextPath() + "/exercises");
            }
        } catch (Exception e) {
            System.err.println("Error showing exercise detail: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "운동 상세 정보를 불러오는 중 오류가 발생했습니다.");
            response.sendRedirect(request.getContextPath() + "/exercises");
        }
    }

    private void syncExercises(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String limitParam = request.getParameter("limit");
        int limit = 50;

        if (limitParam != null && !limitParam.trim().isEmpty()) {
            try {
                limit = Integer.parseInt(limitParam);
            } catch (NumberFormatException e) {
                limit = 50;
            }
        }

        try {
            boolean success = exerciseService.syncAllExercisesFromAPI(limit);

            if (success) {
                long count = exerciseService.getExerciseCount();
                request.setAttribute("message", "API에서 " + count + "개의 운동 데이터를 성공적으로 동기화했습니다.");
            } else {
                request.setAttribute("error", "운동 데이터 동기화에 실패했습니다.");
            }
        } catch (Exception e) {
            System.err.println("Error syncing exercises: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "운동 데이터 동기화 중 오류가 발생했습니다: " + e.getMessage());
        }

        listExercises(request, response);
    }

    private void filterExercises(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String filterType = request.getParameter("filterType");
        String filterValue = request.getParameter("filterValue");

        if (filterType == null || filterValue == null || filterValue.trim().isEmpty()) {
            listExercises(request, response);
            return;
        }

        try {
            String language = getLanguage(request);  // 언어 가져오기
            List<ExerciseDTO> exercises = null;

            switch (filterType) {
                case "primaryMuscle":
                    exercises = exerciseService.getExercisesByPrimaryMuscle(filterValue, language);
                    break;
                case "level":
                    exercises = exerciseService.getExercisesByLevel(filterValue, language);
                    break;
                default:
                    exercises = exerciseService.getAllExercises(language);
                    break;
            }

            request.setAttribute("exercises", exercises);
            request.setAttribute("exerciseCount", exercises.size());
            request.setAttribute("filterType", filterType);
            request.setAttribute("filterValue", filterValue);
            request.setAttribute("message", filterValue + "에 대한 " + exercises.size() + "개의 운동이 있습니다.");

            request.getRequestDispatcher("/exercises.jsp").forward(request, response);
        } catch (Exception e) {
            System.err.println("Error filtering exercises: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "운동 필터링 중 오류가 발생했습니다.");
            listExercises(request, response);
        }
    }

    private void syncExercisesFromDetails(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // 웹앱 실제 경로 가져오기 (이미지 다운로드용)
            String webappPath = getServletContext().getRealPath("/");

            boolean success = exerciseService.syncExercisesFromDetails(webappPath);

            if (success) {
                long count = exerciseService.getExerciseCount();
                request.setAttribute("message", "exerciseDetails에서 " + count + "개의 운동 데이터를 성공적으로 동기화했습니다. (이미지 다운로드 완료)");
            } else {
                request.setAttribute("error", "exerciseDetails가 비어있거나 동기화에 실패했습니다.");
            }
        } catch (Exception e) {
            System.err.println("Error syncing exercises from details: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "운동 데이터 동기화 중 오류가 발생했습니다: " + e.getMessage());
        }

        listExercises(request, response);
    }

    private void syncImagesFromExerciseDetails(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // 웹앱 실제 경로 가져오기 (이미지 다운로드용)
            String webappPath = getServletContext().getRealPath("/");

            boolean success = exerciseService.syncImagesFromExerciseDetails(webappPath);

            if (success) {
                request.setAttribute("message", "exerciseDetails의 이미지를 성공적으로 다운로드하여 images 테이블에 저장했습니다.");
            } else {
                request.setAttribute("error", "이미지 동기화에 실패했습니다.");
            }
        } catch (Exception e) {
            System.err.println("Error syncing images from exerciseDetails: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "이미지 동기화 중 오류가 발생했습니다: " + e.getMessage());
        }

        listExercises(request, response);
    }

    private void searchExercises(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String query = request.getParameter("q");

        if (query == null || query.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/exercises");
            return;
        }

        try {
            String currentLanguage = getLanguage(request);  // 현재 언어

            // 1단계: 현재 언어 DB에서 검색
            List<ExerciseDTO> results = exerciseService.searchByMultipleFields(query.trim(), currentLanguage);

            boolean languageChanged = false;

            // 2단계: 결과가 없으면 다른 언어 DB에서 재시도
            if (results.isEmpty()) {
                String alternativeLanguage = "ko".equals(currentLanguage) ? "en" : "ko";

                System.out.println("No results in " + currentLanguage + " DB. Trying " + alternativeLanguage + " DB...");

                results = exerciseService.searchByMultipleFields(query.trim(), alternativeLanguage);

                // 대체 언어에서 결과를 찾으면 언어 자동 전환
                if (!results.isEmpty()) {
                    currentLanguage = alternativeLanguage;
                    request.getSession().setAttribute("language", alternativeLanguage);
                    languageChanged = true;

                    System.out.println("Found results in " + alternativeLanguage + " DB. Language auto-switched.");
                }
            }

            // 3단계: 결과 처리
            if (results.isEmpty()) {
                // 양쪽 DB 모두 결과 없음
                request.setAttribute("searchQuery", query);
                request.setAttribute("error", "\"" + query + "\"에 대한 검색 결과가 없습니다.");
                request.getRequestDispatcher("/exercises.jsp").forward(request, response);

            } else if (results.size() == 1) {
                // 검색 결과가 1개 → 상세 페이지로 자동 이동
                ExerciseDTO exercise = results.get(0);
                ExerciseDetailDTO exerciseDetail = exerciseService.getExerciseDetailById(exercise.getId(), currentLanguage);

                if (exerciseDetail != null) {
                    request.setAttribute("exercise", exerciseDetail);

                    // 언어 자동 전환 알림
                    if (languageChanged) {
                        String langName = "ko".equals(currentLanguage) ? "한국어" : "영어";
                        request.setAttribute("message", "검색 결과를 찾기 위해 언어를 " + langName + "로 변경했습니다.");
                    }

                    // Fetch YouTube videos based on exercise data
                    String videoSearchQuery = youtubeVideoService.determineSearchQuery(
                        exerciseDetail.getEquipment(),
                        exerciseDetail.getName()
                    );

                    if (!videoSearchQuery.isEmpty()) {
                        List<YouTubeVideoDTO> youtubeVideos =
                            youtubeVideoService.getVideos(videoSearchQuery, 3);
                        request.setAttribute("youtubeVideos", youtubeVideos);
                        request.setAttribute("videoSearchQuery", videoSearchQuery);
                    }

                    // Fetch Naver Shopping products based on exercise data
                    String searchQuery = naverShoppingService.determineSearchQuery(
                        exerciseDetail.getEquipment(),
                        exerciseDetail.getName()
                    );

                    if (!searchQuery.isEmpty()) {
                        List<NaverProductDTO> naverProducts =
                            naverShoppingService.getProductsFromCombinedQuery(searchQuery, 5);
                        request.setAttribute("naverProducts", naverProducts);
                        request.setAttribute("searchQuery", searchQuery);
                    }

                    request.getRequestDispatcher("/exerciseDetail.jsp").forward(request, response);
                } else {
                    request.setAttribute("error", "운동 상세 정보를 찾을 수 없습니다.");
                    request.getRequestDispatcher("/exercises.jsp").forward(request, response);
                }

            } else {
                // 검색 결과가 여러 개 → 목록 표시 (페이지네이션 적용)
                // 페이지네이션 파라미터 처리
                int page = 1;
                int pageSize = 18;
                String pageParam = request.getParameter("page");
                if (pageParam != null && !pageParam.trim().isEmpty()) {
                    try {
                        page = Integer.parseInt(pageParam);
                        if (page < 1) page = 1;
                    } catch (NumberFormatException e) {
                        page = 1;
                    }
                }

                // 전체 검색 결과 개수
                long totalCount = exerciseService.getSearchResultCount(query.trim(), currentLanguage);
                int totalPages = (int) Math.ceil((double) totalCount / pageSize);

                // 페이지네이션된 검색 결과 조회
                List<ExerciseDTO> pagedResults = exerciseService.searchByMultipleFields(query.trim(), currentLanguage, page, pageSize);

                request.setAttribute("exercises", pagedResults);
                request.setAttribute("exerciseCount", pagedResults.size());
                request.setAttribute("totalCount", totalCount);
                request.setAttribute("currentPage", page);
                request.setAttribute("pageSize", pageSize);
                request.setAttribute("totalPages", totalPages);
                request.setAttribute("searchQuery", query);

                // 언어 자동 전환 알림
                if (languageChanged) {
                    String langName = "ko".equals(currentLanguage) ? "한국어" : "영어";
                    request.setAttribute("message",
                        "검색 결과를 찾기 위해 언어를 " + langName + "로 변경했습니다. \"" + query + "\"에 대한 " + totalCount + "개의 검색 결과 (페이지 " + page + "/" + totalPages + ")");
                } else {
                    request.setAttribute("message", "\"" + query + "\"에 대한 " + totalCount + "개의 검색 결과 (페이지 " + page + "/" + totalPages + ")");
                }

                request.getRequestDispatcher("/exercises.jsp").forward(request, response);
            }

        } catch (Exception e) {
            System.err.println("Error searching exercises: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "검색 중 오류가 발생했습니다.");
            request.getRequestDispatcher("/exercises.jsp").forward(request, response);
        }
    }

    /**
     * 세션에서 현재 언어 설정을 가져옵니다.
     * @param request HTTP 요청 객체
     * @return 언어 코드 (기본값: "en")
     */
    private String getLanguage(HttpServletRequest request) {
        String language = (String) request.getSession().getAttribute("language");
        return language != null ? language : "en";  // 기본값: 영어
    }
}
