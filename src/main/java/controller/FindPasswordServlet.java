package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import dao.UserDAO;

@WebServlet("/findpassword")
public class FindPasswordServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public FindPasswordServlet() {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        // 한글 처리
        request.setCharacterEncoding("UTF-8");

        // 폼 데이터 가져오기
        String username = request.getParameter("username");
        String email = request.getParameter("email");

        // DAO 싱글톤 사용
        UserDAO dao = UserDAO.getInstance();

        // ID + EMAIL 존재 여부 확인
        boolean exists = dao.checkUserEmail(username, email);

        // 메시지 설정
        if (exists) {
            request.setAttribute("message", 
                "The account exists. Please contact admin to reset your password.");
        } else {
            request.setAttribute("message", 
                "No account matches this ID and Email. Please try again.");
        }

        // 결과 페이지로 이동
        request.getRequestDispatcher("findpassword_result.jsp")
               .forward(request, response);
    }
}
