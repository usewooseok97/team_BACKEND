<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="header.jsp" %>

<style>
    .detail-container {
        max-width: 900px;
        margin: 50px auto;
        padding: 20px;
        background: white;
        border-radius: 12px;
        box-shadow: 0 4px 12px rgba(0,0,0,0.1);
    }

    .detail-header {
        border-bottom: 3px solid #667eea;
        padding-bottom: 20px;
        margin-bottom: 30px;
    }

    .detail-title {
        font-size: 32px;
        font-weight: bold;
        color: #333;
        text-transform: capitalize;
        margin-bottom: 15px;
    }

    .detail-badges {
        display: flex;
        gap: 10px;
        flex-wrap: wrap;
    }

    .badge {
        display: inline-block;
        padding: 8px 16px;
        border-radius: 6px;
        font-size: 14px;
        font-weight: bold;
    }

    .badge-bodypart {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        color: white;
    }

    .badge-target {
        background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
        color: white;
    }

    .badge-equipment {
        background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
        color: white;
    }

    .badge-difficulty {
        background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
        color: white;
    }

    .badge-category {
        background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
        color: white;
    }

    .detail-section {
        margin-bottom: 30px;
    }

    .section-title {
        font-size: 20px;
        font-weight: bold;
        color: #333;
        margin-bottom: 15px;
        padding-left: 10px;
        border-left: 4px solid #667eea;
    }

    .info-grid {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
        gap: 15px;
        margin-bottom: 20px;
    }

    .info-item {
        padding: 15px;
        background: #f5f5f5;
        border-radius: 8px;
    }

    .info-label {
        font-weight: bold;
        color: #666;
        font-size: 14px;
        margin-bottom: 5px;
    }

    .info-value {
        color: #333;
        font-size: 16px;
        text-transform: capitalize;
    }

    .instruction-list {
        list-style: none;
        padding: 0;
        counter-reset: instruction-counter;
    }

    .instruction-list li {
        counter-increment: instruction-counter;
        padding: 15px;
        margin-bottom: 10px;
        background: #f8f9fa;
        border-radius: 8px;
        position: relative;
        padding-left: 50px;
    }

    .instruction-list li::before {
        content: counter(instruction-counter);
        position: absolute;
        left: 15px;
        top: 15px;
        background: #667eea;
        color: white;
        width: 25px;
        height: 25px;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        font-weight: bold;
        font-size: 14px;
    }

    .muscle-list {
        display: flex;
        flex-wrap: wrap;
        gap: 10px;
    }

    .muscle-tag {
        background: #e3f2fd;
        color: #1976d2;
        padding: 8px 16px;
        border-radius: 20px;
        font-size: 14px;
    }

    .back-button {
        display: inline-block;
        background: #6c757d;
        color: white;
        padding: 12px 24px;
        border-radius: 6px;
        text-decoration: none;
        transition: background 0.2s;
        margin-top: 20px;
    }

    .back-button:hover {
        background: #5a6268;
    }

    .description-box {
        background: #fff3cd;
        border-left: 4px solid #ffc107;
        padding: 15px;
        border-radius: 4px;
        color: #856404;
    }
</style>

<div class="detail-container">
    <c:choose>
        <c:when test="${not empty exercise}">
            <div class="detail-header">
                <h1 class="detail-title">${exercise.name}</h1>
                <div class="detail-badges">
                    <span class="badge badge-bodypart">${exercise.bodyPart}</span>
                    <span class="badge badge-target">${exercise.target}</span>
                    <span class="badge badge-equipment">${exercise.equipment}</span>
                    <c:if test="${not empty exercise.difficulty}">
                        <span class="badge badge-difficulty">${exercise.difficulty}</span>
                    </c:if>
                    <c:if test="${not empty exercise.category}">
                        <span class="badge badge-category">${exercise.category}</span>
                    </c:if>
                </div>
            </div>

            <div class="detail-section">
                <div class="info-grid">
                    <div class="info-item">
                        <div class="info-label">운동 ID</div>
                        <div class="info-value">${exercise.id}</div>
                    </div>
                    <div class="info-item">
                        <div class="info-label">주요 근육</div>
                        <div class="info-value">${exercise.target}</div>
                    </div>
                    <div class="info-item">
                        <div class="info-label">신체 부위</div>
                        <div class="info-value">${exercise.bodyPart}</div>
                    </div>
                    <div class="info-item">
                        <div class="info-label">필요 장비</div>
                        <div class="info-value">${exercise.equipment}</div>
                    </div>
                </div>
            </div>

            <c:if test="${not empty exercise.description}">
                <div class="detail-section">
                    <h2 class="section-title">설명</h2>
                    <div class="description-box">
                        ${exercise.description}
                    </div>
                </div>
            </c:if>

            <c:if test="${not empty exercise.secondaryMuscles}">
                <div class="detail-section">
                    <h2 class="section-title">보조 근육</h2>
                    <div class="muscle-list">
                        <c:forEach var="muscle" items="${exercise.secondaryMuscles}">
                            <span class="muscle-tag">${muscle}</span>
                        </c:forEach>
                    </div>
                </div>
            </c:if>

            <c:if test="${not empty exercise.instructions}">
                <div class="detail-section">
                    <h2 class="section-title">운동 방법</h2>
                    <ol class="instruction-list">
                        <c:forEach var="instruction" items="${exercise.instructions}">
                            <li>${instruction}</li>
                        </c:forEach>
                    </ol>
                </div>
            </c:if>

            <a href="${pageContext.request.contextPath}/exercises" class="back-button">목록으로 돌아가기</a>
        </c:when>
        <c:otherwise>
            <div class="detail-header">
                <h1 class="detail-title">운동을 찾을 수 없습니다</h1>
            </div>
            <a href="${pageContext.request.contextPath}/exercises" class="back-button">목록으로 돌아가기</a>
        </c:otherwise>
    </c:choose>
</div>

<%@ include file="footer.jsp" %>
