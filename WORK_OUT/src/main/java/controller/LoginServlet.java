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
 * 로그인/로그아웃 처리 Servlet (MVC 모델2 - Controller)
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        userDAO = UserDAO.getInstance();
    }

    /**
     * GET: 로그인 폼 페이지로 이동
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 로그아웃 처리
        String action = request.getParameter("action");
        if ("logout".equals(action)) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        // 로그인 페이지로 이동
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    /**
     * POST: 로그인 처리
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 요청 인코딩 설정
        request.setCharacterEncoding("UTF-8");

        // 폼 데이터 받기
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // 유효성 검사
        if (username == null || username.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {

            request.setAttribute("error", "아이디와 비밀번호를 입력해주세요.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        // 로그인 시도
        UserDTO user = userDAO.login(username, password);

        if (user != null) {
            // 로그인 성공: 세션에 사용자 정보 저장
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            session.setAttribute("username", user.getUsername());
            session.setAttribute("role", user.getRole());
            session.setMaxInactiveInterval(1800); // 30분

            // 관리자면 관리자 페이지로, 일반 사용자면 메인 페이지로
            if (user.isAdmin()) {
                response.sendRedirect(request.getContextPath() + "/admin/users");
            } else {
                response.sendRedirect(request.getContextPath() + "/index.jsp");
            }
        } else {
            // 로그인 실패
            request.setAttribute("error", "아이디 또는 비밀번호가 올바르지 않습니다.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
}
