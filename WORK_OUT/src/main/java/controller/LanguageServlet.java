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
        
        // 이전 페이지로 리다이렉트
        String referer = request.getHeader("Referer");
        if (referer != null && !referer.isEmpty()) {
            response.sendRedirect(referer);
        } else {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
}