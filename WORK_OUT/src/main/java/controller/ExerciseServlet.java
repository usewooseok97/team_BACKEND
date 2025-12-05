package controller;

import dto.AmazonProductDTO;
import dto.ExerciseDTO;
import dto.ExerciseDetailDTO;
import dto.YouTubeVideoDTO;
import service.AmazonProductService;
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
    private AmazonProductService amazonProductService;
    private YouTubeVideoService youtubeVideoService;

    @Override
    public void init() throws ServletException {
        exerciseService = ExerciseService.getInstance();
        amazonProductService = AmazonProductService.getInstance();
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
            List<ExerciseDTO> exercises = exerciseService.getAllExercises();
            long count = exerciseService.getExerciseCount();

            request.setAttribute("exercises", exercises);
            request.setAttribute("exerciseCount", count);
            request.setAttribute("message", "총 " + count + "개의 운동이 있습니다.");

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
            ExerciseDetailDTO exerciseDetail = exerciseService.getExerciseDetailById(id);

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

                // Fetch Amazon products based on exercise data
                String searchQuery = amazonProductService.determineSearchQuery(
                    exerciseDetail.getEquipment(),
                    exerciseDetail.getName()
                );

                if (!searchQuery.isEmpty()) {
                    List<AmazonProductDTO> amazonProducts =
                        amazonProductService.getProductsFromCombinedQuery(searchQuery, 5);
                    request.setAttribute("amazonProducts", amazonProducts);
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
            List<ExerciseDTO> exercises = null;

            switch (filterType) {
                case "primaryMuscle":
                    exercises = exerciseService.getExercisesByPrimaryMuscle(filterValue);
                    break;
                case "level":
                    exercises = exerciseService.getExercisesByLevel(filterValue);
                    break;
                default:
                    exercises = exerciseService.getAllExercises();
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
            // name, bodyPart, target 모두 검색
            List<ExerciseDTO> results = exerciseService.searchByMultipleFields(query.trim());

            if (results.isEmpty()) {
                request.setAttribute("searchQuery", query);
                request.setAttribute("error", "\"" + query + "\"에 대한 검색 결과가 없습니다.");
                request.getRequestDispatcher("/exercises.jsp").forward(request, response);
            } else if (results.size() == 1) {
                // 검색 결과가 1개면 바로 detail 페이지로
                ExerciseDTO exercise = results.get(0);
                ExerciseDetailDTO exerciseDetail = exerciseService.getExerciseDetailById(exercise.getId());

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

                    // Fetch Amazon products based on exercise data
                    String searchQuery = amazonProductService.determineSearchQuery(
                        exerciseDetail.getEquipment(),
                        exerciseDetail.getName()
                    );

                    if (!searchQuery.isEmpty()) {
                        List<AmazonProductDTO> amazonProducts =
                            amazonProductService.getProductsFromCombinedQuery(searchQuery, 5);
                        request.setAttribute("amazonProducts", amazonProducts);
                        request.setAttribute("searchQuery", searchQuery);
                    }

                    request.getRequestDispatcher("/exerciseDetail.jsp").forward(request, response);
                } else {
                    request.setAttribute("error", "운동 상세 정보를 찾을 수 없습니다.");
                    request.getRequestDispatcher("/exercises.jsp").forward(request, response);
                }
            } else {
                request.setAttribute("exercises", results);
                request.setAttribute("exerciseCount", results.size());
                request.setAttribute("message", "\"" + query + "\"에 대한 " + results.size() + "개의 검색 결과");
                request.getRequestDispatcher("/exercises.jsp").forward(request, response);
            }
        } catch (Exception e) {
            System.err.println("Error searching exercises: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "검색 중 오류가 발생했습니다.");
            listExercises(request, response);
        }
    }
}
