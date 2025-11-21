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
import java.util.List;

/**
 * 관리자 회원관리 Servlet (MVC 모델2 - Controller)
 */
@WebServlet("/admin/users")
public class AdminUserServlet extends HttpServlet {
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        userDAO = UserDAO.getInstance();
    }

    /**
     * 관리자 권한 확인
     */
    private boolean isAdmin(HttpSession session) {
        if (session == null) {
            return false;
        }
        UserDTO user = (UserDTO) session.getAttribute("user");
        return user != null && user.isAdmin();
    }

    /**
     * GET: 회원 목록 조회
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

        // 관리자 권한 확인
        if (!isAdmin(session)) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        // 모든 회원 조회
        List<UserDTO> users = userDAO.findAll();

        // request에 데이터 저장
        request.setAttribute("users", users);
        request.setAttribute("userCount", users.size());

        // 회원관리 페이지로 이동
        request.getRequestDispatcher("/admin/userManagement.jsp").forward(request, response);
    }

    /**
     * POST: 회원 삭제 처리
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

        // 관리자 권한 확인
        if (!isAdmin(session)) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        // 요청 인코딩 설정
        request.setCharacterEncoding("UTF-8");

        // 액션 타입 확인
        String action = request.getParameter("action");

        if ("delete".equals(action)) {
            // 회원 삭제
            String username = request.getParameter("username");

            if (username == null || username.trim().isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/admin/users?error=invalid");
                return;
            }

            // 본인 삭제 방지
            UserDTO currentUser = (UserDTO) session.getAttribute("user");
            if (currentUser.getUsername().equals(username)) {
                response.sendRedirect(request.getContextPath() + "/admin/users?error=self");
                return;
            }

            // 최고 관리자 삭제 방지
            UserDTO targetUser = userDAO.findByUsername(username);
            if (targetUser != null && targetUser.isSuperAdmin()) {
                response.sendRedirect(request.getContextPath() + "/admin/users?error=superadmin");
                return;
            }

            // 삭제 처리
            boolean success = userDAO.delete(username);

            if (success) {
                response.sendRedirect(request.getContextPath() + "/admin/users?deleted=true");
            } else {
                response.sendRedirect(request.getContextPath() + "/admin/users?error=delete");
            }
        } else if ("updateRole".equals(action)) {
            // 권한 변경
            String username = request.getParameter("username");
            String newRole = request.getParameter("role");

            if (username == null || newRole == null) {
                response.sendRedirect(request.getContextPath() + "/admin/users?error=invalid");
                return;
            }

            UserDTO user = userDAO.findByUsername(username);
            if (user != null) {
                // 최고 관리자 권한 변경 방지
                if (user.isSuperAdmin()) {
                    response.sendRedirect(request.getContextPath() + "/admin/users?error=superadmin");
                    return;
                }

                user.setRole(newRole);
                userDAO.update(user);
                response.sendRedirect(request.getContextPath() + "/admin/users?updated=true");
            } else {
                response.sendRedirect(request.getContextPath() + "/admin/users?error=notfound");
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/users");
        }
    }
}
