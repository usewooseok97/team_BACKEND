<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="header.jsp" %>

<link rel="stylesheet" type="text/css" href="css/exerciseDetailstyle.css">
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

            <c:if test="${not empty exercise.gifUrl}">
                <div class="exercise-image-section">
                    <div class="exercise-detail-image-container">
                        <img src="${exercise.gifUrl}"
                             alt="${exercise.name}"
                             class="exercise-detail-image"
                             onerror="this.parentElement.parentElement.style.display='none'">
                    </div>
                </div>
            </c:if>

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

            <!-- YouTube Tutorial Videos Section -->
            <c:if test="${not empty youtubeVideos}">
                <div class="detail-section">
                    <h2 class="section-title">Tutorial Videos</h2>
                    <p class="video-subtitle">Learn how to perform "${videoSearchQuery}"</p>

                    <div class="youtube-videos-grid">
                        <c:forEach var="video" items="${youtubeVideos}">
                            <a href="https://youtube.com/watch?v=${video.videoId}"
                               target="_blank"
                               rel="noopener noreferrer"
                               class="video-card">

                                <div class="video-thumbnail-wrapper">
                                    <img src="${video.thumbnailUrl}"
                                         alt="${video.title}"
                                         class="video-thumbnail"
                                         onerror="this.src='https://via.placeholder.com/360x202?text=Video'">
                                    <div class="video-play-overlay">
                                        <span class="play-icon">▶</span>
                                    </div>
                                </div>

                                <div class="video-info">
                                    <h3 class="video-title">${video.title}</h3>
                                </div>
                            </a>
                        </c:forEach>
                    </div>
                </div>
            </c:if>

            <!-- Amazon Products Section -->

            <c:if test="${not empty amazonProducts}">
                <div class="detail-section">
                    <h2 class="section-title">Recommended Equipment</h2>
                    <p class="product-subtitle">Related products for "${searchQuery}"</p>

                    <div class="amazon-products-grid">
                        <c:forEach var="product" items="${amazonProducts}">
                            <a href="${product.url}" target="_blank" rel="noopener noreferrer" class="product-card">
                                <div class="product-image-wrapper">
                                    <img src="${product.image}"
                                         alt="${product.name}"
                                         class="product-image"
                                         onerror="this.src='https://via.placeholder.com/200x200?text=No+Image'">
                                    <c:if test="${product.isBestSeller}">
                                        <span class="badge-bestseller">Best Seller</span>
                                    </c:if>
                                    <c:if test="${product.hasPrime}">
                                        <span class="badge-prime">Prime</span>
                                    </c:if>
                                </div>

                                <div class="product-info">
                                    <h3 class="product-name">${product.name}</h3>

                                    <div class="product-rating">
                                        <c:if test="${product.stars > 0}">
                                            <span class="stars">★ ${product.stars}</span>
                                        </c:if>
                                    </div>

                                    <div class="product-price">
                                        <c:choose>
                                            <c:when test="${not empty product.priceString}">
                                                <span class="price-value">${product.priceString}</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="price-unavailable">Price not available</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>

                                    <div class="product-view-button">
                                        View on Amazon →
                                    </div>
                                </div>
                            </a>
                        </c:forEach>
                    </div>

                    <p class="amazon-disclaimer">
                        * Product information is provided by Amazon. Prices and availability may vary.
                    </p>
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
