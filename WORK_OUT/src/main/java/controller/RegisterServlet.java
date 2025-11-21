package controller;

import dao.UserDAO;
import dto.UserDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * 회원가입 처리 Servlet (MVC 모델2 - Controller)
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        userDAO = UserDAO.getInstance();
    }

    /**
     * GET: 회원가입 폼 페이지로 이동
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/register.jsp").forward(request, response);
    }

    /**
     * POST: 회원가입 처리
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 요청 인코딩 설정
        request.setCharacterEncoding("UTF-8");

        // 폼 데이터 받기
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String passwordConfirm = request.getParameter("passwordConfirm");
        String email = request.getParameter("email");
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");

        // 유효성 검사
        if (username == null || username.trim().isEmpty() ||
            password == null || password.trim().isEmpty() ||
            email == null || email.trim().isEmpty() ||
            name == null || name.trim().isEmpty()) {

            request.setAttribute("error", "모든 필수 항목을 입력해주세요.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        // 비밀번호 확인
        if (!password.equals(passwordConfirm)) {
            request.setAttribute("error", "비밀번호가 일치하지 않습니다.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        // Username 중복 확인
        if (userDAO.isUsernameExists(username)) {
            request.setAttribute("error", "이미 사용 중인 아이디입니다.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        // Email 중복 확인
        if (userDAO.isEmailExists(email)) {
            request.setAttribute("error", "이미 사용 중인 이메일입니다.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        // UserDTO 생성
        UserDTO user = new UserDTO();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setName(name);
        user.setPhone(phone);
        user.setRole("USER"); // 기본값은 일반 사용자

        // 회원가입 처리
        boolean success = userDAO.register(user);

        if (success) {
            // 성공 시 로그인 페이지로 리다이렉트
            response.sendRedirect(request.getContextPath() + "/login?registered=true");
        } else {
            // 실패 시 에러 메시지와 함께 다시 회원가입 페이지로
            request.setAttribute("error", "회원가입에 실패했습니다. 다시 시도해주세요.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        }
    }
}
