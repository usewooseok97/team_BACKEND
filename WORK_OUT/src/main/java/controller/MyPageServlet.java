package controller;

import dao.UserDAO;
import dto.UserDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * 마이페이지 (정보수정) 처리 Servlet (MVC 모델2 - Controller)
 */
@WebServlet("/mypage")
public class MyPageServlet extends HttpServlet {
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        userDAO = UserDAO.getInstance();
    }

    /**
     * GET: 마이페이지 조회
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 로그인 확인
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // 세션에서 사용자 정보 가져오기
        UserDTO user = (UserDTO) session.getAttribute("user");

        // 최신 정보 다시 조회 (다른 곳에서 수정됐을 수 있음)
        UserDTO currentUser = userDAO.findByUsername(user.getUsername());
        if (currentUser == null) {
            // 사용자가 삭제됐을 경우
            session.invalidate();
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // 최신 정보를 request에 저장
        request.setAttribute("user", currentUser);

        // 마이페이지로 이동
        request.getRequestDispatcher("/mypage.jsp").forward(request, response);
    }

    /**
     * POST: 정보 수정 처리
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 로그인 확인
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // 요청 인코딩 설정
        request.setCharacterEncoding("UTF-8");

        // 세션에서 현재 사용자 가져오기
        UserDTO currentUser = (UserDTO) session.getAttribute("user");
        String currentUsername = currentUser.getUsername();

        // 폼 데이터 받기
        String password = request.getParameter("password");
        String passwordConfirm = request.getParameter("passwordConfirm");
        String email = request.getParameter("email");
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");

        // 유효성 검사
        if (email == null || email.trim().isEmpty() ||
            name == null || name.trim().isEmpty()) {

            request.setAttribute("error", "모든 필수 항목을 입력해주세요.");
            request.setAttribute("user", currentUser);
            request.getRequestDispatcher("/mypage.jsp").forward(request, response);
            return;
        }

        // 비밀번호 변경 시 확인
        if (password != null && !password.trim().isEmpty()) {
            if (!password.equals(passwordConfirm)) {
                request.setAttribute("error", "비밀번호가 일치하지 않습니다.");
                request.setAttribute("user", currentUser);
                request.getRequestDispatcher("/mypage.jsp").forward(request, response);
                return;
            }
            currentUser.setPassword(password);
        }

        // Email 중복 확인 (본인 이메일 제외)
        if (!email.equals(currentUser.getEmail()) && userDAO.isEmailExists(email)) {
            request.setAttribute("error", "이미 사용 중인 이메일입니다.");
            request.setAttribute("user", currentUser);
            request.getRequestDispatcher("/mypage.jsp").forward(request, response);
            return;
        }

        // 정보 업데이트
        currentUser.setEmail(email);
        currentUser.setName(name);
        currentUser.setPhone(phone);

        // DB 업데이트
        boolean success = userDAO.update(currentUser);

        if (success) {
            // 세션 정보도 업데이트
            session.setAttribute("user", currentUser);

            // 성공 메시지와 함께 마이페이지로
            request.setAttribute("success", "정보가 수정되었습니다.");
            request.setAttribute("user", currentUser);
            request.getRequestDispatcher("/mypage.jsp").forward(request, response);
        } else {
            // 실패 시 에러 메시지
            request.setAttribute("error", "정보 수정에 실패했습니다.");
            request.setAttribute("user", currentUser);
            request.getRequestDispatcher("/mypage.jsp").forward(request, response);
        }
    }
}
