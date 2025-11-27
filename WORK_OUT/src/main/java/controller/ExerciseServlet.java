package controller;

import dto.ExerciseDTO;
import service.ExerciseService;

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

    @Override
    public void init() throws ServletException {
        exerciseService = ExerciseService.getInstance();
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
            case "filter":
                filterExercises(request, response);
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
            ExerciseDTO exercise = exerciseService.getExerciseById(id);

            if (exercise != null) {
                request.setAttribute("exercise", exercise);
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
            boolean success = exerciseService.syncExercisesFromAPI(limit);

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
                case "target":
                    exercises = exerciseService.getExercisesByTarget(filterValue);
                    break;
                case "bodyPart":
                    exercises = exerciseService.getExercisesByBodyPart(filterValue);
                    break;
                case "equipment":
                    exercises = exerciseService.getExercisesByEquipment(filterValue);
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
}
