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
                    <c:if test="${not empty exercise.category}">
                        <span class="badge badge-category">${exercise.category}</span>
                    </c:if>
                    <c:if test="${not empty exercise.level}">
                        <span class="badge badge-difficulty">${exercise.level}</span>
                    </c:if>
                    <c:if test="${not empty exercise.equipment}">
                        <span class="badge badge-equipment">${exercise.equipment}</span>
                    </c:if>
                    <c:if test="${not empty exercise.mechanic}">
                        <span class="badge badge-bodypart">${exercise.mechanic}</span>
                    </c:if>
                    <c:if test="${not empty exercise.force}">
                        <span class="badge badge-target">${exercise.force}</span>
                    </c:if>
                </div>
            </div>

            <c:if test="${not empty exercise.images}">
                <div class="exercise-image-section">
                    <div class="exercise-detail-image-container">
                        <c:forEach var="imageUrl" items="${exercise.images}" varStatus="status">
                            <c:if test="${status.index < 2}">
                                <c:choose>
                                    <c:when test="${imageUrl.startsWith('http://') || imageUrl.startsWith('https://')}">
                                        <img src="${imageUrl}"
                                             alt="${exercise.name} - Image ${status.index + 1}"
                                             class="exercise-detail-image"
                                             loading="lazy"
                                             onerror="this.style.display='none'">
                                    </c:when>
                                    <c:when test="${imageUrl.startsWith('images/')}">
                                        <img src="${pageContext.request.contextPath}/${imageUrl}"
                                             alt="${exercise.name} - Image ${status.index + 1}"
                                             class="exercise-detail-image"
                                             loading="lazy"
                                             onerror="this.style.display='none'">
                                    </c:when>
                                    <c:otherwise>
                                        <img src="https://${imageUrl}"
                                             alt="${exercise.name} - Image ${status.index + 1}"
                                             class="exercise-detail-image"
                                             loading="lazy"
                                             onerror="this.style.display='none'">
                                    </c:otherwise>
                                </c:choose>
                            </c:if>
                        </c:forEach>
                    </div>
                </div>
            </c:if>

            <div class="detail-section">
                <div class="info-grid">
                    <div class="info-item">
                        <div class="info-label">${lang == 'ko' ? '운동 ID' : 'WORKOUT ID'}</div>
                        <div class="info-value">${exercise.id}</div>
                    </div>
                    <c:if test="${not empty exercise.category}">
                        <div class="info-item">
                            <div class="info-label">${lang == 'ko' ? '카테고리' : 'Category'}</div>
                            <div class="info-value">${exercise.category}</div>
                        </div>
                    </c:if>
                    <c:if test="${not empty exercise.equipment}">
                        <div class="info-item">
                            <div class="info-label">${lang == 'ko' ? '필요 장비' : 'Required Equipment'}</div>
                            <div class="info-value">${exercise.equipment}</div>
                        </div>
                    </c:if>
                    <c:if test="${not empty exercise.level}">
                        <div class="info-item">
                            <div class="info-label">${lang == 'ko' ? '난이도' : 'Difficulty Level'}</div>
                            <div class="info-value">${exercise.level}</div>
                        </div>
                    </c:if>
                    <c:if test="${not empty exercise.mechanic}">
                        <div class="info-item">
                            <div class="info-label">${lang == 'ko' ? '운동 방식' : 'Mechanic'}</div>
                            <div class="info-value">${exercise.mechanic}</div>
                        </div>
                    </c:if>
                    <c:if test="${not empty exercise.force}">
                        <div class="info-item">
                            <div class="info-label">${lang == 'ko' ? '힘의 방향' : 'Force'}</div>
                            <div class="info-value">${exercise.force}</div>
                        </div>
                    </c:if>
                </div>
            </div>

            <c:if test="${not empty exercise.primaryMuscles}">
                <div class="detail-section">
                    <h2 class="section-title">${lang == 'ko' ? '주요 근육' : 'Primary Muscles'}</h2>
                    <div class="muscle-list">
                        <c:forEach var="muscle" items="${exercise.primaryMuscles}">
                            <a href="${pageContext.request.contextPath}/exercises?action=search&q=${muscle}" class="muscle-tag">${muscle}</a>
                        </c:forEach>
                    </div>
                </div>
            </c:if>

            <c:if test="${not empty exercise.secondaryMuscles}">
                <div class="detail-section">
                    <h2 class="section-title">${lang == 'ko' ? '보조 근육' : 'Secondary Muscles'}</h2>
                    <div class="muscle-list">
                        <c:forEach var="muscle" items="${exercise.secondaryMuscles}">
                            <a href="${pageContext.request.contextPath}/exercises?action=search&q=${muscle}" class="muscle-tag">${muscle}</a>
                        </c:forEach>
                    </div>
                </div>
            </c:if>

            <c:if test="${not empty exercise.instructions}">
                <div class="detail-section">
                    <h2 class="section-title">${lang == 'ko' ? '운동 방법' : 'How To?'}</h2>
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
                    <h2 class="section-title">${lang == 'ko' ? '튜토리얼 영상' : 'Tutorial Videos'}</h2>
                    <p class="video-subtitle">
                        ${lang == 'ko' ? '"' : 'Learn how to perform "'}${videoSearchQuery}${lang == 'ko' ? '" 운동 방법을 배워보세요' : '"'}
                    </p>

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
                    <h2 class="section-title">${lang == 'ko' ? '추천 장비' : 'Recommended Equipment'}</h2>
                    <p class="product-subtitle">
                        ${lang == 'ko' ? '"' : 'Related products for "'}${searchQuery}${lang == 'ko' ? '" 관련 상품' : '"'}
                    </p>

                    <div class="amazon-products-grid">
                        <c:forEach var="product" items="${amazonProducts}">
                            <a href="${product.url}" target="_blank" rel="noopener noreferrer" class="product-card">
                                <div class="product-image-wrapper">
                                    <img src="${product.image}"
                                         alt="${product.name}"
                                         class="product-image"
                                         onerror="this.src='https://via.placeholder.com/200x200?text=No+Image'">
                                    <c:if test="${product.isBestSeller}">
                                        <span class="badge-bestseller">${lang == 'ko' ? '베스트셀러' : 'Best Seller'}</span>
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
                                                <span class="price-unavailable">
                                                    ${lang == 'ko' ? '가격 정보 없음' : 'Price not available'}
                                                </span>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>

                                    <div class="product-view-button">
                                        ${lang == 'ko' ? '아마존에서 보기 →' : 'View on Amazon →'}
                                    </div>
                                </div>
                            </a>
                        </c:forEach>
                    </div>

                    <p class="amazon-disclaimer">
                        ${lang == 'ko' ? '* 상품 정보는 아마존에서 제공됩니다. 가격과 재고는 변동될 수 있습니다.' 
                                       : '* Product information is provided by Amazon. Prices and availability may vary.'}
                    </p>
                </div>
            </c:if>

            <a href="${pageContext.request.contextPath}/exercises" class="back-button">
                ${lang == 'ko' ? '목록으로 돌아가기' : 'Go Back to the List'}
            </a>
        </c:when>
        <c:otherwise>
            <div class="detail-header">
                <h1 class="detail-title">${lang == 'ko' ? '운동을 찾을 수 없습니다' : 'Exercise Not Found'}</h1>
            </div>
            <a href="${pageContext.request.contextPath}/exercises" class="back-button">
                ${lang == 'ko' ? '목록으로 돌아가기' : 'Go Back to the List'}
            </a>
        </c:otherwise>
    </c:choose>
</div>

<%@ include file="footer.jsp" %>