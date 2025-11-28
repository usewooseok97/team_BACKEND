<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="header.jsp" %>

<style>
    .exercise-container {
        max-width: 1200px;
        margin: 50px auto;
        padding: 20px;
    }

    .exercise-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 30px;
    }

    .exercise-header h1 {
        color: #333;
    }

    .sync-button {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        color: white;
        padding: 12px 24px;
        border: none;
        border-radius: 6px;
        cursor: pointer;
        font-size: 16px;
        transition: transform 0.2s;
    }

    .sync-button:hover {
        transform: scale(1.05);
    }

    .filter-section {
        background: #f5f5f5;
        padding: 20px;
        border-radius: 8px;
        margin-bottom: 30px;
    }

    .filter-form {
        display: flex;
        gap: 15px;
        align-items: center;
    }

    .filter-form select,
    .filter-form input {
        padding: 10px;
        border: 1px solid #ddd;
        border-radius: 4px;
        font-size: 14px;
    }

    .filter-form button {
        background: #4CAF50;
        color: white;
        padding: 10px 20px;
        border: none;
        border-radius: 4px;
        cursor: pointer;
    }

    .message {
        padding: 15px;
        margin-bottom: 20px;
        border-radius: 6px;
        background: #d4edda;
        color: #155724;
        border: 1px solid #c3e6cb;
    }

    .error {
        background: #f8d7da;
        color: #721c24;
        border: 1px solid #f5c6cb;
    }

    .exercise-grid {
        display: grid;
        grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
        gap: 20px;
    }

    .exercise-card {
        background: white;
        border-radius: 8px;
        padding: 20px;
        box-shadow: 0 2px 8px rgba(0,0,0,0.1);
        transition: transform 0.2s, box-shadow 0.2s;
    }

    .exercise-card:hover {
        transform: translateY(-5px);
        box-shadow: 0 4px 12px rgba(0,0,0,0.15);
    }

    .exercise-name {
        font-size: 18px;
        font-weight: bold;
        color: #333;
        margin-bottom: 10px;
        text-transform: capitalize;
    }

    .exercise-info {
        margin: 8px 0;
        color: #666;
        font-size: 14px;
    }

    .exercise-info strong {
        color: #333;
    }

    .badge {
        display: inline-block;
        padding: 4px 8px;
        border-radius: 4px;
        font-size: 12px;
        font-weight: bold;
        margin-right: 5px;
    }

    .badge-bodypart {
        background: #e3f2fd;
        color: #1976d2;
    }

    .badge-target {
        background: #fff3e0;
        color: #e65100;
    }

    .badge-equipment {
        background: #f3e5f5;
        color: #7b1fa2;
    }

    .detail-button {
        background: #2196F3;
        color: white;
        padding: 8px 16px;
        border: none;
        border-radius: 4px;
        cursor: pointer;
        text-decoration: none;
        display: inline-block;
        margin-top: 10px;
        transition: background 0.2s;
    }

    .detail-button:hover {
        background: #1976D2;
    }

    .no-exercises {
        text-align: center;
        padding: 50px;
        color: #999;
    }
</style>

<div class="exercise-container">
    <div class="exercise-header">
        <h1>EXERCISE LIST</h1>
        <form action="${pageContext.request.contextPath}/exercises" method="post" style="display: inline;">
            <input type="hidden" name="action" value="sync">
            <input type="number" name="limit" placeholder="NUMBERS" style="width: 150px; padding: 10px; margin-right: 10px; border: 1px solid #ddd; border-radius: 4px;">
            <button type="submit" class="sync-button">SYNCHRONIZE API DATA</button>
        </form>
    </div>

    <c:if test="${not empty message}">
        <div class="message">${message}</div>
    </c:if>

    <c:if test="${not empty error}">
        <div class="message error">${error}</div>
    </c:if>

    <div class="filter-section">
        <form action="${pageContext.request.contextPath}/exercises" method="get" class="filter-form">
            <input type="hidden" name="action" value="filter">
            <select name="filterType">
                <option value="target">TARGET MUSCLES</option>
                <option value="bodyPart">BODY PARTS</option>
                <option value="equipment">EQUIPMENTS</option>
            </select>
            <input type="text" name="filterValue" placeholder="Search.." required>
            <button type="submit">FILTER</button>
            <a href="${pageContext.request.contextPath}/exercises" style="margin-left: 10px; color: #666;">SEE ALL</a>
        </form>
    </div>

    <c:choose>
        <c:when test="${not empty exercises}">
            <div class="exercise-grid">
                <c:forEach var="exercise" items="${exercises}">
                    <div class="exercise-card">
                        <div class="exercise-name">${exercise.name}</div>
                        <div class="exercise-info">
                            <span class="badge badge-bodypart">${exercise.bodyPart}</span>
                            <span class="badge badge-target">${exercise.target}</span>
                        </div>
                        <div class="exercise-info">
                            <span class="badge badge-equipment">${exercise.equipment}</span>
                        </div>
                        <c:if test="${not empty exercise.difficulty}">
                            <div class="exercise-info">
                                <strong>DIFFICULTY:</strong> ${exercise.difficulty}
                            </div>
                        </c:if>
                        <a href="${pageContext.request.contextPath}/exercises?action=detail&id=${exercise.id}" class="detail-button">
                            상세보기
                        </a>
                    </div>
                </c:forEach>
            </div>
        </c:when>
        <c:otherwise>
            <div class="no-exercises">
                <h2>THERE ARE NO WORK-OUT DATA AVAILABLE.</h2>
                <p>PLEASE SYNCHRONIZE API DATA ABOVE.</p>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<%@ include file="footer.jsp" %>
