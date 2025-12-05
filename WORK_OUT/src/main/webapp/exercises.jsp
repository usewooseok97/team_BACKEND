<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="header.jsp" %>
<link rel="stylesheet" type="text/css" href="css/exercisestyle.css">
<div class="exercise-container">
    <div class="exercise-header">
        <h1>EXERCISE LIST</h1>
    </div>

    <c:if test="${not empty message}">
        <div class="message">${message}</div>
    </c:if>

    <c:if test="${not empty error}">
        <div class="message error">${error}</div>
    </c:if>

    <c:choose>
        <c:when test="${not empty exercises}">
            <div class="exercise-grid">
                <c:forEach var="exercise" items="${exercises}">
                    <div class="exercise-card">
                        <c:if test="${not empty exercise.images && exercise.images.size() > 0}">
                            <div class="exercise-image">
                                <c:set var="imageUrl" value="${exercise.images[0]}" />
                                <c:choose>
                                    <c:when test="${imageUrl.startsWith('http://') || imageUrl.startsWith('https://')}">
                                        <!-- 외부 URL -->
                                        <img src="${imageUrl}"
                                             alt="${exercise.name}"
                                             style="width: 100%; height: 150px; object-fit: cover; border-radius: 8px 8px 0 0;"
                                             loading="lazy"
                                             onerror="this.style.display='none'">
                                    </c:when>
                                    <c:when test="${imageUrl.startsWith('images/')}">
                                        <!-- 로컬 이미지 경로 -->
                                        <img src="${pageContext.request.contextPath}/${imageUrl}"
                                             alt="${exercise.name}"
                                             style="width: 100%; height: 150px; object-fit: cover; border-radius: 8px 8px 0 0;"
                                             loading="lazy"
                                             onerror="this.style.display='none'">
                                    </c:when>
                                    <c:otherwise>
                                        <!-- 짧은 URL (https:// 추가) -->
                                        <img src="https://${imageUrl}"
                                             alt="${exercise.name}"
                                             style="width: 100%; height: 150px; object-fit: cover; border-radius: 8px 8px 0 0;"
                                             loading="lazy"
                                             onerror="this.style.display='none'">
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </c:if>
                        <div class="exercise-name">${exercise.name}</div>
                        <c:if test="${not empty exercise.primaryMuscles}">
                            <div class="exercise-info">
                                <strong>PRIMARY MUSCLES:</strong>
                                <c:forEach var="muscle" items="${exercise.primaryMuscles}" varStatus="status">
                                    <a href="${pageContext.request.contextPath}/exercises?action=search&q=${muscle}" class="badge badge-target">${muscle}</a><c:if test="${!status.last}">, </c:if>
                                </c:forEach>
                            </div>
                        </c:if>
                        <c:if test="${not empty exercise.secondaryMuscles}">
                            <div class="exercise-info">
                                <strong>SECONDARY MUSCLES:</strong>
                                <c:forEach var="muscle" items="${exercise.secondaryMuscles}" varStatus="status">
                                    <a href="${pageContext.request.contextPath}/exercises?action=search&q=${muscle}" class="badge badge-bodypart">${muscle}</a><c:if test="${!status.last}">, </c:if>
                                </c:forEach>
                            </div>
                        </c:if>
                        <c:if test="${not empty exercise.level}">
                            <div class="exercise-info">
                                <strong>LEVEL:</strong> <span class="badge badge-equipment">${exercise.level}</span>
                            </div>
                        </c:if>
                        <a href="${pageContext.request.contextPath}/exercises?action=detail&id=${exercise.id}" class="detail-button">
                            Details
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
