package controller;

import dto.ExerciseDTO;
import dto.ExerciseDetailDTO;
import service.ExerciseService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/language")
public class LanguageServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private ExerciseService exerciseService;

    @Override
    public void init() throws ServletException {
        exerciseService = ExerciseService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String lang = request.getParameter("lang");
        HttpSession session = request.getSession();

        // ⭐ 중요: 새 언어로 변경하기 전에 이전 언어를 먼저 변수에 백업
        String oldLang = (String) session.getAttribute("language");
        if (oldLang == null) {
            oldLang = "en"; // 기본값
        }

        // 언어 설정 (기본값: en)
        if (lang != null && (lang.equals("en") || lang.equals("ko"))) {
            session.setAttribute("language", lang);
            System.out.println("[LANG CHANGE] " + oldLang + " → " + lang);
        }

        // 이전 페이지 URL 확인
        String referer = request.getHeader("Referer");

        // ⭐ 우선순위 1: 상세 페이지에서 언어 전환 → 같은 운동의 새 언어 버전 표시 (먼저 체크!)
        if (referer != null && referer.contains("action=detail")) {
            // URL에서 exercise ID 추출
            String exerciseId = extractIdFromUrl(referer);
            if (exerciseId != null) {
                try {
                    System.out.println("[DEBUG] Language change on detail page - ID: " + exerciseId + ", Old lang: " + oldLang + ", New lang: " + lang);

                    // 이전 언어로 운동 조회하여 실제 _id 확보 (ObjectId 포함)
                    ExerciseDetailDTO currentExercise = exerciseService.getExerciseDetailById(exerciseId, oldLang);

                    if (currentExercise != null) {
                        // 실제 DB의 _id 사용 (ObjectId를 toString()한 값)
                        String actualId = currentExercise.getId();
                        System.out.println("[SUCCESS] Found exercise with actual _id: " + actualId + ", Name: " + currentExercise.getName());

                        // 같은 _id로 새 언어의 detail 페이지로 리다이렉트
                        // _id가 공통이므로 새 언어 DB에서도 같은 _id로 조회 가능
                        response.sendRedirect(request.getContextPath() + "/exercises?action=detail&id=" + actualId);
                        return;
                    } else {
                        System.err.println("[ERROR] Exercise not found with ID: " + exerciseId + " in language: " + oldLang);
                    }
                } catch (Exception e) {
                    System.err.println("[ERROR] Failed to fetch exercise during language change: " + e.getMessage());
                    e.printStackTrace();
                }
            }
            // ID 추출 실패 또는 운동 조회 실패 시 목록으로
            response.sendRedirect(request.getContextPath() + "/exercises");
            return;
        }

        // ⭐ 우선순위 2: 운동 목록/검색/필터 페이지에서 언어 변경 → ID로 재조회하여 같은 결과를 새 언어로 표시
        @SuppressWarnings("unchecked")
        List<String> exerciseIds = (List<String>) session.getAttribute("currentExerciseIds");
        String currentAction = (String) session.getAttribute("currentAction");

        if (exerciseIds != null && !exerciseIds.isEmpty() && currentAction != null) {
            try {
                // 새 언어로 같은 운동들 조회
                List<ExerciseDTO> exercises = exerciseService.getExercisesByIds(exerciseIds, lang);

                // 결과를 request에 설정
                request.setAttribute("exercises", exercises);
                request.setAttribute("exerciseCount", exercises.size());

                // 액션 타입에 따라 추가 정보 설정
                if ("search".equals(currentAction)) {
                    String searchQuery = (String) session.getAttribute("searchQuery");
                    request.setAttribute("searchQuery", searchQuery);
                    request.setAttribute("message", "\"" + searchQuery + "\"에 대한 " + exercises.size() + "개의 검색 결과");
                } else if ("filter".equals(currentAction)) {
                    String filterType = (String) session.getAttribute("filterType");
                    String filterValue = (String) session.getAttribute("filterValue");
                    request.setAttribute("filterType", filterType);
                    request.setAttribute("filterValue", filterValue);
                    request.setAttribute("message", filterValue + "에 대한 " + exercises.size() + "개의 운동");
                } else if ("list".equals(currentAction)) {
                    request.setAttribute("message", "총 " + exercises.size() + "개의 운동");
                }

                // exercises.jsp로 포워드
                request.getRequestDispatcher("/exercises.jsp").forward(request, response);
                return;

            } catch (Exception e) {
                System.err.println("Error reloading exercises with new language: " + e.getMessage());
                e.printStackTrace();
                // 오류 발생 시 일반 리다이렉트로 fallback
            }
        }

        // 그 외에는 이전 페이지로 리다이렉트
        if (referer != null && !referer.isEmpty()) {
            response.sendRedirect(referer);
        }
        // Referer 없으면 홈으로
        else {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    /**
     * URL에서 exercise ID 추출
     * 예: "...exercises?action=detail&id=abc123" → "abc123"
     */
    private String extractIdFromUrl(String url) {
        if (url == null || url.isEmpty()) {
            return null;
        }

        // "id=" 파라미터 찾기
        int idIndex = url.indexOf("id=");
        if (idIndex == -1) {
            return null;
        }

        // "id=" 이후 값 추출
        String idPart = url.substring(idIndex + 3);

        // 다음 파라미터가 있으면 거기까지만 (& 기준으로 자르기)
        int ampIndex = idPart.indexOf("&");
        if (ampIndex != -1) {
            idPart = idPart.substring(0, ampIndex);
        }

        return idPart.trim().isEmpty() ? null : idPart.trim();
    }
}