package controller;

import dao.ExerciseDetailDAO;
import dao.ImagesDAO;
import dto.ExerciseDetailDTO;
import dto.ImagesDTO;
import service.ImageDownloadService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * 이미지 마이그레이션 서블릿
 * exerciseDetails 컬렉션의 이미지 URL들을 다운로드하여 로컬에 저장하고
 * images 컬렉션에 로컬 경로를 저장합니다.
 *
 * 사용법: http://localhost:8080/WORK_OUT/imageMigration?action=migrate
 *
 * 주의: 이 작업은 시간이 오래 걸릴 수 있으므로 한 번만 실행하세요.
 */
@WebServlet("/imageMigration")
public class ImageMigrationServlet extends HttpServlet {
    private ExerciseDetailDAO exerciseDetailDAO;
    private ImagesDAO imagesDAO;
    private ImageDownloadService imageDownloadService;

    @Override
    public void init() throws ServletException {
        exerciseDetailDAO = ExerciseDetailDAO.getInstance();
        imagesDAO = ImagesDAO.getInstance();
        imageDownloadService = ImageDownloadService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");

        if (action == null) {
            showMenu(request, response);
            return;
        }

        switch (action) {
            case "migrate":
                migrateImages(request, response);
                break;
            case "status":
                showStatus(request, response);
                break;
            case "clear":
                clearImages(request, response);
                break;
            default:
                showMenu(request, response);
                break;
        }
    }

    /**
     * 메뉴 화면 표시
     */
    private void showMenu(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<title>이미지 마이그레이션 도구</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; margin: 40px; }");
        out.println("h1 { color: #333; }");
        out.println(".btn { display: inline-block; padding: 10px 20px; margin: 10px; ");
        out.println("       background: #007bff; color: white; text-decoration: none; ");
        out.println("       border-radius: 5px; }");
        out.println(".btn:hover { background: #0056b3; }");
        out.println(".warning { color: red; font-weight: bold; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>이미지 마이그레이션 도구</h1>");
        out.println("<p>이 도구는 exerciseDetails 컬렉션의 이미지 URL을 다운로드하여 로컬에 저장합니다.</p>");
        out.println("<p class='warning'>주의: 이미지 다운로드는 시간이 오래 걸릴 수 있습니다!</p>");
        out.println("<div>");
        out.println("<a href='imageMigration?action=status' class='btn'>현재 상태 확인</a>");
        out.println("<a href='imageMigration?action=migrate' class='btn'>이미지 마이그레이션 시작</a>");
        out.println("<a href='imageMigration?action=clear' class='btn' style='background: #dc3545;'>images 테이블 초기화</a>");
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }

    /**
     * 현재 상태 표시
     */
    private void showStatus(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();

        long exerciseCount = exerciseDetailDAO.count();
        int imagesCount = imagesDAO.findAll().size();

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<title>마이그레이션 상태</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; margin: 40px; }");
        out.println(".status { background: #f0f0f0; padding: 20px; border-radius: 5px; }");
        out.println(".btn { display: inline-block; padding: 10px 20px; margin: 10px; ");
        out.println("       background: #007bff; color: white; text-decoration: none; ");
        out.println("       border-radius: 5px; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>마이그레이션 상태</h1>");
        out.println("<div class='status'>");
        out.println("<p><strong>exerciseDetails 컬렉션:</strong> " + exerciseCount + "개</p>");
        out.println("<p><strong>images 컬렉션:</strong> " + imagesCount + "개</p>");
        out.println("<p><strong>마이그레이션 진행률:</strong> " +
                    (exerciseCount > 0 ? (imagesCount * 100 / exerciseCount) : 0) + "%</p>");
        out.println("</div>");
        out.println("<a href='imageMigration' class='btn'>돌아가기</a>");
        out.println("</body>");
        out.println("</html>");
    }

    /**
     * images 테이블 초기화
     */
    private void clearImages(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();

        boolean success = imagesDAO.deleteAll();

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<title>초기화 결과</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; margin: 40px; }");
        out.println(".btn { display: inline-block; padding: 10px 20px; margin: 10px; ");
        out.println("       background: #007bff; color: white; text-decoration: none; ");
        out.println("       border-radius: 5px; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>초기화 " + (success ? "성공" : "실패") + "</h1>");
        out.println("<p>images 컬렉션이 " + (success ? "초기화되었습니다." : "초기화에 실패했습니다.") + "</p>");
        out.println("<a href='imageMigration' class='btn'>돌아가기</a>");
        out.println("</body>");
        out.println("</html>");
    }

    /**
     * 이미지 마이그레이션 실행
     */
    private void migrateImages(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<title>이미지 마이그레이션 진행 중...</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; margin: 40px; }");
        out.println(".log { background: #f5f5f5; padding: 10px; border-left: 3px solid #007bff; margin: 5px 0; }");
        out.println(".success { color: green; }");
        out.println(".error { color: red; }");
        out.println(".info { color: blue; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>이미지 마이그레이션 진행 중...</h1>");
        out.flush();

        // 웹앱의 실제 경로 가져오기
        String webappPath = getServletContext().getRealPath("/");

        out.println("<div class='log info'>웹앱 경로: " + webappPath + "</div>");
        out.flush();

        // 모든 운동 상세 정보 조회
        List<ExerciseDetailDTO> exerciseDetails = exerciseDetailDAO.findAll();
        out.println("<div class='log info'>총 " + exerciseDetails.size() + "개의 운동 데이터를 찾았습니다.</div>");
        out.flush();

        int successCount = 0;
        int skipCount = 0;
        int errorCount = 0;

        // 각 운동에 대해 이미지 다운로드 및 저장
        for (ExerciseDetailDTO exercise : exerciseDetails) {
            String exerciseId = exercise.getId();
            String exerciseName = exercise.getName();
            List<String> imageUrls = exercise.getImages();

            out.println("<div class='log'><strong>[" + exerciseId + "] " + exerciseName + "</strong></div>");
            out.flush();

            // 이미 마이그레이션된 데이터인지 확인
            if (imagesDAO.existsById(exerciseId)) {
                out.println("<div class='log info'>  → 이미 마이그레이션되었습니다. 건너뜁니다.</div>");
                skipCount++;
                out.flush();
                continue;
            }

            // 이미지 URL이 없는 경우
            if (imageUrls == null || imageUrls.isEmpty()) {
                out.println("<div class='log info'>  → 이미지가 없습니다.</div>");

                // 빈 리스트로 저장
                ImagesDTO imagesDTO = new ImagesDTO(exerciseId, new ArrayList<>());
                imagesDAO.insert(imagesDTO);

                skipCount++;
                out.flush();
                continue;
            }

            // 이미지 다운로드 및 로컬 경로 수집
            List<String> localPaths = new ArrayList<>();

            for (int i = 0; i < imageUrls.size(); i++) {
                String imageUrl = imageUrls.get(i);
                out.println("<div class='log'>  이미지 " + (i + 1) + "/" + imageUrls.size() + " 다운로드 중: " + imageUrl + "</div>");
                out.flush();

                try {
                    String localPath = imageDownloadService.downloadAndSaveImage(
                        imageUrl, exerciseId, i, webappPath);

                    if (localPath != null) {
                        localPaths.add(localPath);
                        out.println("<div class='log success'>  → 성공: " + localPath + "</div>");
                    } else {
                        out.println("<div class='log error'>  → 다운로드 실패</div>");
                    }
                    out.flush();
                } catch (Exception e) {
                    out.println("<div class='log error'>  → 오류: " + e.getMessage() + "</div>");
                    out.flush();
                }
            }

            // images 컬렉션에 저장
            if (!localPaths.isEmpty() || imageUrls.size() > 0) {
                ImagesDTO imagesDTO = new ImagesDTO(exerciseId, localPaths);
                boolean inserted = imagesDAO.insert(imagesDTO);

                if (inserted) {
                    out.println("<div class='log success'>  ✓ DB에 저장 완료 (" + localPaths.size() + "개 이미지)</div>");
                    successCount++;
                } else {
                    out.println("<div class='log error'>  ✗ DB 저장 실패</div>");
                    errorCount++;
                }
                out.flush();
            }
        }

        out.println("<hr>");
        out.println("<h2>마이그레이션 완료!</h2>");
        out.println("<div class='log success'><strong>성공:</strong> " + successCount + "개</div>");
        out.println("<div class='log info'><strong>건너뜀:</strong> " + skipCount + "개</div>");
        out.println("<div class='log error'><strong>실패:</strong> " + errorCount + "개</div>");
        out.println("<p><a href='imageMigration'>돌아가기</a></p>");
        out.println("</body>");
        out.println("</html>");
    }
}
