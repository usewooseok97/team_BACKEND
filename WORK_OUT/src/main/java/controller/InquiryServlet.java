package controller;

import dao.InquiryDAO;
import dto.InquiryDTO;
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
 * 고객센터 문의 게시판 요청 처리 Servlet
 */
@WebServlet("/inquiry")
public class InquiryServlet extends HttpServlet {
    private InquiryDAO inquiryDAO;

    @Override
    public void init() throws ServletException {
        inquiryDAO = InquiryDAO.getInstance();
    }

    /**
     * GET: 게시글 목록, 상세보기, 작성 폼 등
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        try {
            switch (action) {
                case "list":
                    handleList(request, response);
                    break;
                case "detail":
                    handleDetail(request, response);
                    break;
                case "write":
                    handleWriteForm(request, response);
                    break;
                case "edit":
                    handleEditForm(request, response);
                    break;
                case "delete":
                    handleDelete(request, response);
                    break;
                default:
                    handleList(request, response);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "요청 처리 중 오류가 발생했습니다.");
            request.getRequestDispatcher("/boardList.jsp").forward(request, response);
        }
    }

    /**
     * POST: 게시글 작성, 수정, 답변 등
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 요청 인코딩 설정
        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        if (action == null) {
            action = "write";
        }

        try {
            switch (action) {
                case "write":
                    handleWrite(request, response);
                    break;
                case "edit":
                    handleEdit(request, response);
                    break;
                case "reply":
                    handleReply(request, response);
                    break;
                case "deleteReply":
                    handleDeleteReply(request, response);
                    break;
                case "comment":
                    handleComment(request, response);
                    break;
                case "deleteComment":
                    handleDeleteComment(request, response);
                    break;
                case "inquiryComment":
                    handleInquiryComment(request, response);
                    break;
                case "deleteInquiryComment":
                    handleDeleteInquiryComment(request, response);
                    break;
                default:
                    response.sendRedirect(request.getContextPath() + "/inquiry?action=list");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "요청 처리 중 오류가 발생했습니다.");
            request.getRequestDispatcher("/boardList.jsp").forward(request, response);
        }
    }

    /**
     * 게시글 목록 조회
     */
    private void handleList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 페이징 파라미터
        int page = 1;
        try {
            String pageParam = request.getParameter("page");
            if (pageParam != null && !pageParam.isEmpty()) {
                page = Integer.parseInt(pageParam);
            }
        } catch (NumberFormatException e) {
            page = 1;
        }

        int pageSize = 10;

        // 검색 파라미터
        String searchType = request.getParameter("searchType");
        String searchKeyword = request.getParameter("searchKeyword");
        if (searchKeyword != null) {
            searchKeyword = searchKeyword.trim();
        }

        // 게시글 목록 조회
        List<InquiryDTO> inquiries = inquiryDAO.findAll(page, pageSize, searchType, searchKeyword);
        int totalCount = inquiryDAO.getTotalCount(searchType, searchKeyword);
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);

        // 요청 속성 설정
        request.setAttribute("inquiries", inquiries);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("totalCount", totalCount);
        request.setAttribute("pageSize", pageSize);
        request.setAttribute("searchType", searchType);
        request.setAttribute("searchKeyword", searchKeyword);

        request.getRequestDispatcher("/boardList.jsp").forward(request, response);
    }

    /**
     * 게시글 상세보기
     */
    private void handleDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String id = request.getParameter("id");
        if (id == null || id.isEmpty()) {
            request.setAttribute("error", "게시글 ID가 필요합니다.");
            request.getRequestDispatcher("/boardList.jsp").forward(request, response);
            return;
        }

        InquiryDTO inquiry = inquiryDAO.findById(id);
        if (inquiry == null) {
            request.setAttribute("error", "게시글을 찾을 수 없습니다.");
            request.getRequestDispatcher("/boardList.jsp").forward(request, response);
            return;
        }

        request.setAttribute("inquiry", inquiry);
        request.getRequestDispatcher("/boardDetail.jsp").forward(request, response);
    }

    /**
     * 게시글 작성 폼
     */
    private void handleWriteForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 로그인 확인
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            request.setAttribute("error", "로그인이 필요합니다.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        request.getRequestDispatcher("/boardWrite.jsp").forward(request, response);
    }

    /**
     * 게시글 수정 폼
     */
    private void handleEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 로그인 확인
        HttpSession session = request.getSession(false);
        UserDTO user = (UserDTO) session.getAttribute("user");
        if (user == null) {
            request.setAttribute("error", "로그인이 필요합니다.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        String id = request.getParameter("id");
        if (id == null || id.isEmpty()) {
            request.setAttribute("error", "게시글 ID가 필요합니다.");
            request.getRequestDispatcher("/boardList.jsp").forward(request, response);
            return;
        }

        InquiryDTO inquiry = inquiryDAO.findByIdWithoutIncrement(id);
        if (inquiry == null) {
            request.setAttribute("error", "게시글을 찾을 수 없습니다.");
            request.getRequestDispatcher("/boardList.jsp").forward(request, response);
            return;
        }

        // 작성자 또는 관리자만 수정 가능
        if (!inquiry.getAuthor().equals(user.getUsername()) && !user.isAdmin()) {
            request.setAttribute("error", "수정 권한이 없습니다.");
            request.getRequestDispatcher("/boardDetail.jsp?id=" + id).forward(request, response);
            return;
        }

        request.setAttribute("inquiry", inquiry);
        request.getRequestDispatcher("/boardWrite.jsp").forward(request, response);
    }

    /**
     * 게시글 작성 처리
     */
    private void handleWrite(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 로그인 확인
        HttpSession session = request.getSession(false);
        UserDTO user = (UserDTO) session.getAttribute("user");
        if (user == null) {
            request.setAttribute("error", "로그인이 필요합니다.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        String title = request.getParameter("title");
        String content = request.getParameter("content");
        String id = request.getParameter("id"); // 수정 모드인 경우

        // 유효성 검사
        if (title == null || title.trim().isEmpty() ||
            content == null || content.trim().isEmpty()) {
            request.setAttribute("error", "제목과 내용을 입력해주세요.");
            request.getRequestDispatcher("/boardWrite.jsp").forward(request, response);
            return;
        }

        if (id != null && !id.isEmpty()) {
            // 수정 모드
            InquiryDTO inquiry = inquiryDAO.findByIdWithoutIncrement(id);
            if (inquiry == null) {
                request.setAttribute("error", "게시글을 찾을 수 없습니다.");
                request.getRequestDispatcher("/boardList.jsp").forward(request, response);
                return;
            }

            // 작성자 또는 관리자만 수정 가능
            if (!inquiry.getAuthor().equals(user.getUsername()) && !user.isAdmin()) {
                request.setAttribute("error", "수정 권한이 없습니다.");
                request.getRequestDispatcher("/boardList.jsp").forward(request, response);
                return;
            }

            inquiry.setTitle(title.trim());
            inquiry.setContent(content.trim());

            if (inquiryDAO.update(inquiry)) {
                response.sendRedirect(request.getContextPath() + "/inquiry?action=detail&id=" + id);
            } else {
                request.setAttribute("error", "게시글 수정에 실패했습니다.");
                request.setAttribute("inquiry", inquiry);
                request.getRequestDispatcher("/boardWrite.jsp").forward(request, response);
            }
        } else {
            // 작성 모드
            InquiryDTO inquiry = new InquiryDTO();
            inquiry.setTitle(title.trim());
            inquiry.setContent(content.trim());
            inquiry.setAuthor(user.getUsername());
            inquiry.setAuthorName(user.getName());

            if (inquiryDAO.create(inquiry)) {
                response.sendRedirect(request.getContextPath() + "/inquiry?action=list");
            } else {
                request.setAttribute("error", "게시글 작성에 실패했습니다.");
                request.getRequestDispatcher("/boardWrite.jsp").forward(request, response);
            }
        }
    }

    /**
     * 게시글 수정 처리 (POST)
     */
    private void handleEdit(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        handleWrite(request, response); // handleWrite에서 수정 모드 처리
    }

    /**
     * 게시글 삭제
     */
    private void handleDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 로그인 확인
        HttpSession session = request.getSession(false);
        UserDTO user = (UserDTO) session.getAttribute("user");
        if (user == null) {
            request.setAttribute("error", "로그인이 필요합니다.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        String id = request.getParameter("id");
        if (id == null || id.isEmpty()) {
            request.setAttribute("error", "게시글 ID가 필요합니다.");
            request.getRequestDispatcher("/boardList.jsp").forward(request, response);
            return;
        }

        InquiryDTO inquiry = inquiryDAO.findByIdWithoutIncrement(id);
        if (inquiry == null) {
            request.setAttribute("error", "게시글을 찾을 수 없습니다.");
            request.getRequestDispatcher("/boardList.jsp").forward(request, response);
            return;
        }

        // 작성자 또는 관리자만 삭제 가능
        if (!inquiry.getAuthor().equals(user.getUsername()) && !user.isAdmin()) {
            request.setAttribute("error", "삭제 권한이 없습니다.");
            request.getRequestDispatcher("/boardList.jsp").forward(request, response);
            return;
        }

        if (inquiryDAO.delete(id)) {
            response.sendRedirect(request.getContextPath() + "/inquiry?action=list");
        } else {
            request.setAttribute("error", "게시글 삭제에 실패했습니다.");
            request.getRequestDispatcher("/boardList.jsp").forward(request, response);
        }
    }

    /**
     * 답변 작성
     */
    private void handleReply(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 로그인 확인
        HttpSession session = request.getSession(false);
        UserDTO user = (UserDTO) session.getAttribute("user");
        if (user == null) {
            request.setAttribute("error", "로그인이 필요합니다.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        // 관리자만 답변 가능
        if (!user.isAdmin()) {
            request.setAttribute("error", "답변 권한이 없습니다.");
            request.getRequestDispatcher("/boardList.jsp").forward(request, response);
            return;
        }

        String inquiryId = request.getParameter("inquiryId");
        String content = request.getParameter("content");

        if (inquiryId == null || inquiryId.isEmpty() ||
            content == null || content.trim().isEmpty()) {
            request.setAttribute("error", "답변 내용을 입력해주세요.");
            response.sendRedirect(request.getContextPath() + "/inquiry?action=detail&id=" + inquiryId);
            return;
        }

        InquiryDTO.ReplyDTO reply = new InquiryDTO.ReplyDTO();
        reply.setContent(content.trim());
        reply.setAuthor(user.getUsername());
        reply.setAuthorName(user.getName());

        if (inquiryDAO.addReply(inquiryId, reply)) {
            response.sendRedirect(request.getContextPath() + "/inquiry?action=detail&id=" + inquiryId);
        } else {
            request.setAttribute("error", "답변 작성에 실패했습니다.");
            response.sendRedirect(request.getContextPath() + "/inquiry?action=detail&id=" + inquiryId);
        }
    }

    /**
     * 답변 삭제
     */
    private void handleDeleteReply(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 로그인 확인
        HttpSession session = request.getSession(false);
        UserDTO user = (UserDTO) session.getAttribute("user");
        if (user == null) {
            request.setAttribute("error", "로그인이 필요합니다.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        // 관리자만 답변 삭제 가능
        if (!user.isAdmin()) {
            request.setAttribute("error", "답변 삭제 권한이 없습니다.");
            request.getRequestDispatcher("/boardList.jsp").forward(request, response);
            return;
        }

        String inquiryId = request.getParameter("inquiryId");
        String replyId = request.getParameter("replyId");

        if (inquiryId == null || inquiryId.isEmpty() ||
            replyId == null || replyId.isEmpty()) {
            request.setAttribute("error", "필수 파라미터가 없습니다.");
            request.getRequestDispatcher("/boardList.jsp").forward(request, response);
            return;
        }

        if (inquiryDAO.deleteReply(inquiryId, replyId)) {
            response.sendRedirect(request.getContextPath() + "/inquiry?action=detail&id=" + inquiryId);
        } else {
            request.setAttribute("error", "답변 삭제에 실패했습니다.");
            response.sendRedirect(request.getContextPath() + "/inquiry?action=detail&id=" + inquiryId);
        }
    }

    /**
     * 댓글 작성 (고객 또는 관리자 모두 가능)
     */
    private void handleComment(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 로그인 확인
        HttpSession session = request.getSession(false);
        UserDTO user = (UserDTO) session.getAttribute("user");
        if (user == null) {
            request.setAttribute("error", "로그인이 필요합니다.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        String inquiryId = request.getParameter("inquiryId");
        String replyId = request.getParameter("replyId");
        String content = request.getParameter("content");

        if (inquiryId == null || inquiryId.isEmpty() ||
            replyId == null || replyId.isEmpty() ||
            content == null || content.trim().isEmpty()) {
            request.setAttribute("error", "댓글 내용을 입력해주세요.");
            response.sendRedirect(request.getContextPath() + "/inquiry?action=detail&id=" + inquiryId);
            return;
        }

        // 답변이 존재하는지 확인
        InquiryDTO inquiry = inquiryDAO.findByIdWithoutIncrement(inquiryId);
        if (inquiry == null || inquiry.getReplies() == null) {
            request.setAttribute("error", "게시글 또는 답변을 찾을 수 없습니다.");
            response.sendRedirect(request.getContextPath() + "/inquiry?action=list");
            return;
        }

        // 해당 답변이 존재하는지 확인
        boolean replyExists = false;
        for (InquiryDTO.ReplyDTO reply : inquiry.getReplies()) {
            if (reply.getId().equals(replyId)) {
                replyExists = true;
                break;
            }
        }

        if (!replyExists) {
            request.setAttribute("error", "답변을 찾을 수 없습니다.");
            response.sendRedirect(request.getContextPath() + "/inquiry?action=detail&id=" + inquiryId);
            return;
        }

        InquiryDTO.CommentDTO comment = new InquiryDTO.CommentDTO();
        comment.setContent(content.trim());
        comment.setAuthor(user.getUsername());
        comment.setAuthorName(user.getName());

        if (inquiryDAO.addComment(inquiryId, replyId, comment)) {
            response.sendRedirect(request.getContextPath() + "/inquiry?action=detail&id=" + inquiryId);
        } else {
            request.setAttribute("error", "댓글 작성에 실패했습니다.");
            response.sendRedirect(request.getContextPath() + "/inquiry?action=detail&id=" + inquiryId);
        }
    }

    /**
     * 댓글 삭제 (작성자 또는 관리자만 가능)
     */
    private void handleDeleteComment(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 로그인 확인
        HttpSession session = request.getSession(false);
        UserDTO user = (UserDTO) session.getAttribute("user");
        if (user == null) {
            request.setAttribute("error", "로그인이 필요합니다.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        String inquiryId = request.getParameter("inquiryId");
        String replyId = request.getParameter("replyId");
        String commentId = request.getParameter("commentId");

        if (inquiryId == null || inquiryId.isEmpty() ||
            replyId == null || replyId.isEmpty() ||
            commentId == null || commentId.isEmpty()) {
            request.setAttribute("error", "필수 파라미터가 없습니다.");
            request.getRequestDispatcher("/boardList.jsp").forward(request, response);
            return;
        }

        // 댓글 작성자 확인
        InquiryDTO inquiry = inquiryDAO.findByIdWithoutIncrement(inquiryId);
        if (inquiry == null || inquiry.getReplies() == null) {
            request.setAttribute("error", "게시글을 찾을 수 없습니다.");
            request.getRequestDispatcher("/boardList.jsp").forward(request, response);
            return;
        }

        // 해당 댓글 찾기 및 권한 확인
        boolean hasPermission = false;
        for (InquiryDTO.ReplyDTO reply : inquiry.getReplies()) {
            if (reply.getId().equals(replyId) && reply.getComments() != null) {
                for (InquiryDTO.CommentDTO comment : reply.getComments()) {
                    if (comment.getId().equals(commentId)) {
                        // 작성자 또는 관리자만 삭제 가능
                        if (comment.getAuthor().equals(user.getUsername()) || user.isAdmin()) {
                            hasPermission = true;
                        }
                        break;
                    }
                }
                break;
            }
        }

        if (!hasPermission) {
            request.setAttribute("error", "댓글 삭제 권한이 없습니다.");
            response.sendRedirect(request.getContextPath() + "/inquiry?action=detail&id=" + inquiryId);
            return;
        }

        if (inquiryDAO.deleteComment(inquiryId, replyId, commentId)) {
            response.sendRedirect(request.getContextPath() + "/inquiry?action=detail&id=" + inquiryId);
        } else {
            request.setAttribute("error", "댓글 삭제에 실패했습니다.");
            response.sendRedirect(request.getContextPath() + "/inquiry?action=detail&id=" + inquiryId);
        }
    }

    /**
     * 게시글 댓글 작성 (고객 또는 관리자 모두 가능)
     */
    private void handleInquiryComment(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 로그인 확인
        HttpSession session = request.getSession(false);
        UserDTO user = (UserDTO) session.getAttribute("user");
        if (user == null) {
            request.setAttribute("error", "로그인이 필요합니다.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        String inquiryId = request.getParameter("inquiryId");
        String content = request.getParameter("content");

        if (inquiryId == null || inquiryId.isEmpty() ||
            content == null || content.trim().isEmpty()) {
            request.setAttribute("error", "댓글 내용을 입력해주세요.");
            response.sendRedirect(request.getContextPath() + "/inquiry?action=detail&id=" + inquiryId);
            return;
        }

        // 게시글이 존재하는지 확인
        InquiryDTO inquiry = inquiryDAO.findByIdWithoutIncrement(inquiryId);
        if (inquiry == null) {
            request.setAttribute("error", "게시글을 찾을 수 없습니다.");
            response.sendRedirect(request.getContextPath() + "/inquiry?action=list");
            return;
        }

        InquiryDTO.CommentDTO comment = new InquiryDTO.CommentDTO();
        comment.setContent(content.trim());
        comment.setAuthor(user.getUsername());
        comment.setAuthorName(user.getName());

        if (inquiryDAO.addInquiryComment(inquiryId, comment)) {
            response.sendRedirect(request.getContextPath() + "/inquiry?action=detail&id=" + inquiryId);
        } else {
            request.setAttribute("error", "댓글 작성에 실패했습니다.");
            response.sendRedirect(request.getContextPath() + "/inquiry?action=detail&id=" + inquiryId);
        }
    }

    /**
     * 게시글 댓글 삭제 (작성자 또는 관리자만 가능)
     */
    private void handleDeleteInquiryComment(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 로그인 확인
        HttpSession session = request.getSession(false);
        UserDTO user = (UserDTO) session.getAttribute("user");
        if (user == null) {
            request.setAttribute("error", "로그인이 필요합니다.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        String inquiryId = request.getParameter("inquiryId");
        String commentId = request.getParameter("commentId");

        if (inquiryId == null || inquiryId.isEmpty() ||
            commentId == null || commentId.isEmpty()) {
            request.setAttribute("error", "필수 파라미터가 없습니다.");
            request.getRequestDispatcher("/boardList.jsp").forward(request, response);
            return;
        }

        // 댓글 작성자 확인
        InquiryDTO inquiry = inquiryDAO.findByIdWithoutIncrement(inquiryId);
        if (inquiry == null || inquiry.getComments() == null) {
            request.setAttribute("error", "게시글 또는 댓글을 찾을 수 없습니다.");
            request.getRequestDispatcher("/boardList.jsp").forward(request, response);
            return;
        }

        // 해당 댓글 찾기 및 권한 확인
        boolean hasPermission = false;
        for (InquiryDTO.CommentDTO comment : inquiry.getComments()) {
            if (comment.getId().equals(commentId)) {
                // 작성자 또는 관리자만 삭제 가능
                if (comment.getAuthor().equals(user.getUsername()) || user.isAdmin()) {
                    hasPermission = true;
                }
                break;
            }
        }

        if (!hasPermission) {
            request.setAttribute("error", "댓글 삭제 권한이 없습니다.");
            response.sendRedirect(request.getContextPath() + "/inquiry?action=detail&id=" + inquiryId);
            return;
        }

        if (inquiryDAO.deleteInquiryComment(inquiryId, commentId)) {
            response.sendRedirect(request.getContextPath() + "/inquiry?action=detail&id=" + inquiryId);
        } else {
            request.setAttribute("error", "댓글 삭제에 실패했습니다.");
            response.sendRedirect(request.getContextPath() + "/inquiry?action=detail&id=" + inquiryId);
        }
    }
}

