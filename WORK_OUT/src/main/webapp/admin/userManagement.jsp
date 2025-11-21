<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>회원 관리 - WORK OUT</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .admin-container {
            max-width: 1200px;
            margin: 80px auto;
            padding: 40px;
        }
        .admin-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 30px;
        }
        .admin-title {
            font-size: 32px;
            font-weight: bold;
            color: #333;
        }
        .admin-info {
            color: #666;
        }
        .stats-container {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 20px;
            margin-bottom: 30px;
        }
        .stat-card {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 20px;
            border-radius: 10px;
            text-align: center;
        }
        .stat-label {
            font-size: 14px;
            opacity: 0.9;
            margin-bottom: 10px;
        }
        .stat-value {
            font-size: 32px;
            font-weight: bold;
        }
        .table-container {
            background: white;
            border-radius: 10px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            overflow: hidden;
        }
        table {
            width: 100%;
            border-collapse: collapse;
        }
        th {
            background: #f5f5f5;
            padding: 15px;
            text-align: left;
            font-weight: 600;
            color: #555;
            border-bottom: 2px solid #ddd;
        }
        td {
            padding: 15px;
            border-bottom: 1px solid #eee;
            color: #333;
        }
        tr:hover {
            background: #f9f9f9;
        }
        .role-badge {
            display: inline-block;
            padding: 4px 12px;
            border-radius: 12px;
            font-size: 12px;
            font-weight: 600;
        }
        .role-superadmin {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
        }
        .role-admin {
            background: #ffebee;
            color: #c62828;
        }
        .role-user {
            background: #e3f2fd;
            color: #1565c0;
        }
        .action-button {
            padding: 6px 12px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 13px;
            margin-right: 5px;
        }
        .delete-button {
            background: #f44336;
            color: white;
        }
        .delete-button:hover {
            background: #d32f2f;
        }
        .role-button {
            background: #2196F3;
            color: white;
        }
        .role-button:hover {
            background: #1976D2;
        }
        .success-message {
            background: #e8f5e9;
            color: #2e7d32;
            padding: 12px;
            border-radius: 5px;
            margin-bottom: 20px;
            text-align: center;
        }
        .error-message {
            background: #ffebee;
            color: #c62828;
            padding: 12px;
            border-radius: 5px;
            margin-bottom: 20px;
            text-align: center;
        }
        .nav-button-back {
            padding: 10px 20px;
            background: white;
            color: #667eea;
            border: 2px solid #667eea;
            border-radius: 5px;
            text-decoration: none;
            font-weight: 600;
        }
        .nav-button-back:hover {
            background: #667eea;
            color: white;
        }
    </style>
</head>
<body>
    <!-- Navigation Bar -->
    <nav class="navbar">
        <a href="${pageContext.request.contextPath}/index.jsp" class="logo">🏋️</a>
        <div class="nav-right">
            <span style="color: white; margin-right: 20px;">관리자: ${sessionScope.user.name}</span>
            <a href="${pageContext.request.contextPath}/login?action=logout" class="nav-button login">로그아웃</a>
        </div>
    </nav>

    <div class="admin-container">
        <div class="admin-header">
            <div>
                <h1 class="admin-title">회원 관리</h1>
                <p class="admin-info">전체 회원 정보를 관리합니다</p>
            </div>
            <a href="${pageContext.request.contextPath}/index.jsp" class="nav-button-back">홈으로</a>
        </div>

        <!-- 통계 카드 -->
        <div class="stats-container">
            <div class="stat-card">
                <div class="stat-label">총 회원 수</div>
                <div class="stat-value">${userCount}</div>
            </div>
        </div>

        <!-- 메시지 표시 -->
        <c:if test="${param.deleted == 'true'}">
            <div class="success-message">회원이 삭제되었습니다.</div>
        </c:if>
        <c:if test="${param.updated == 'true'}">
            <div class="success-message">회원 정보가 수정되었습니다.</div>
        </c:if>
        <c:if test="${param.error == 'self'}">
            <div class="error-message">본인 계정은 삭제할 수 없습니다.</div>
        </c:if>
        <c:if test="${param.error == 'superadmin'}">
            <div class="error-message">최고 관리자는 삭제하거나 권한을 변경할 수 없습니다.</div>
        </c:if>
        <c:if test="${param.error == 'delete'}">
            <div class="error-message">회원 삭제에 실패했습니다.</div>
        </c:if>
        <c:if test="${param.error == 'notfound'}">
            <div class="error-message">회원을 찾을 수 없습니다.</div>
        </c:if>

        <!-- 회원 목록 테이블 -->
        <div class="table-container">
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>아이디</th>
                        <th>이름</th>
                        <th>이메일</th>
                        <th>전화번호</th>
                        <th>권한</th>
                        <th>가입일</th>
                        <th>관리</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="user" items="${users}">
                        <tr>
                            <td>${user.id}</td>
                            <td>${user.username}</td>
                            <td>${user.name}</td>
                            <td>${user.email}</td>
                            <td>${user.phone}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${user.role == 'SUPER_ADMIN'}">
                                        <span class="role-badge role-superadmin">최고관리자</span>
                                    </c:when>
                                    <c:when test="${user.role == 'ADMIN'}">
                                        <span class="role-badge role-admin">관리자</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="role-badge role-user">일반회원</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>${user.regDate}</td>
                            <td>
                                <!-- 최고 관리자는 버튼 숨김 -->
                                <c:choose>
                                    <c:when test="${user.role == 'SUPER_ADMIN'}">
                                        <span style="color: #667eea; font-weight: 600;">보호됨</span>
                                    </c:when>
                                    <c:when test="${user.username == sessionScope.user.username}">
                                        <span style="color: #999;">본인</span>
                                    </c:when>
                                    <c:otherwise>
                                        <!-- 권한 변경 버튼 -->
                                        <form action="${pageContext.request.contextPath}/admin/users" method="post" style="display:inline;">
                                            <input type="hidden" name="action" value="updateRole">
                                            <input type="hidden" name="username" value="${user.username}">
                                            <input type="hidden" name="role" value="${user.role == 'ADMIN' ? 'USER' : 'ADMIN'}">
                                            <button type="submit" class="action-button role-button">
                                                ${user.role == 'ADMIN' ? '일반회원으로' : '관리자로'}
                                            </button>
                                        </form>

                                        <!-- 삭제 버튼 -->
                                        <form action="${pageContext.request.contextPath}/admin/users" method="post" style="display:inline;"
                                              onsubmit="return confirm('정말 삭제하시겠습니까?');">
                                            <input type="hidden" name="action" value="delete">
                                            <input type="hidden" name="username" value="${user.username}">
                                            <button type="submit" class="action-button delete-button">삭제</button>
                                        </form>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>
