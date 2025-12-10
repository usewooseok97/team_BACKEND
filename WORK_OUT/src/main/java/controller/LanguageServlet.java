package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/language")
public class LanguageServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;  // 이 줄 추가!
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String lang = request.getParameter("lang");
        HttpSession session = request.getSession();

        // 언어 설정 (기본값: en)
        if (lang != null && (lang.equals("en") || lang.equals("ko"))) {
            session.setAttribute("language", lang);
        }

        // 이전 페이지 URL 확인
        String referer = request.getHeader("Referer");

        // 상세 페이지에서 언어 전환 시 목록 페이지로 리다이렉트
        // (다른 언어 DB에 해당 운동이 없을 수 있으므로)
        if (referer != null && referer.contains("action=detail")) {
            response.sendRedirect(request.getContextPath() + "/exercises");
        }
        // 그 외에는 이전 페이지로 리다이렉트
        else if (referer != null && !referer.isEmpty()) {
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
}