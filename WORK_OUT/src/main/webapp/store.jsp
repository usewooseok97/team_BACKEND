<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="${lang == 'en' ? 'en' : 'ko'}">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${lang == 'en' ? 'Store - FitBegin' : '스토어 - FitBegin'}</title>
    <link rel="stylesheet" type="text/css" href="css/storestyle.css">
</head>
<body>
    <%@ include file="header.jsp" %>

    <div class="store-container">
        <!-- Page Title -->
        <div class="store-header">
            <h1 class="store-title">
                ${lang == 'en' ? 'Equipment Store' : '운동 기구 스토어'}
            </h1>
            <p class="store-subtitle">
                ${lang == 'en' ? 'Find the perfect equipment for your workout' : '운동에 필요한 기구를 찾아보세요'}
            </p>
        </div>

        <!-- Category Navigation -->
        <div class="category-nav">
            <c:forEach var="category" items="${allCategories}">
                <a href="${pageContext.request.contextPath}/store?category=${category.id}"
                   class="category-tab ${currentCategory != null && currentCategory.id == category.id ? 'active' : ''}">
                    <span class="category-name">
                        ${lang == 'en' ? category.displayNameEn : category.displayName}
                    </span>
                    <c:if test="${categoryCounts != null && categoryCounts[category.id] != null}">
                        <span class="category-count">(${categoryCounts[category.id]})</span>
                    </c:if>
                </a>
            </c:forEach>
        </div>

        <!-- Success/Error Messages -->
        <c:if test="${not empty success}">
            <div class="alert alert-success">${success}</div>
        </c:if>
        <c:if test="${not empty error}">
            <div class="alert alert-error">${error}</div>
        </c:if>

        <!-- Category Products -->
        <c:if test="${currentCategory != null}">
            <div class="category-header">
                <h2 class="category-title">
                    ${lang == 'en' ? currentCategory.displayNameEn : currentCategory.displayName}
                </h2>
                <div class="category-info">
                    <span class="product-count">
                        ${productCount} ${lang == 'en' ? 'products' : '개 상품'}
                    </span>
                    <!-- Admin Refresh Button (optional) -->
                    <a href="${pageContext.request.contextPath}/store?action=refresh&category=${currentCategory.id}"
                       class="btn-refresh" title="${lang == 'en' ? 'Refresh products' : '상품 새로고침'}">
                        🔄 ${lang == 'en' ? 'Refresh' : '새로고침'}
                    </a>
                </div>
            </div>
        </c:if>

            <!-- Products Grid -->
            <c:choose>
                <c:when test="${not empty products}">
                    <div class="products-grid">
                        <c:forEach var="product" items="${products}">
                            <div class="product-card">
                                <a href="${product.link}" target="_blank" rel="noopener noreferrer" class="product-link">
                                    <div class="product-image-container">
                                        <img src="${product.image}" alt="${product.title}" class="product-image" loading="lazy" />
                                    </div>
                                    <div class="product-info">
                                        <h3 class="product-title">${product.title}</h3>
                                        <div class="product-price">
                                            <span class="price-label">${lang == 'en' ? 'From' : '최저가'}</span>
                                            <span class="price-value">${product.priceString}</span>
                                        </div>
                                        <div class="product-meta">
                                            <span class="badge-mall">${product.mallName}</span>
                                            <c:if test="${not empty product.brand}">
                                                <span class="product-brand">${product.brand}</span>
                                            </c:if>
                                        </div>
                                    </div>
                                </a>
                            </div>
                        </c:forEach>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="empty-state">
                        <p class="empty-message">
                            ${lang == 'en' ? 'No products available for this category.' : '이 카테고리의 상품이 없습니다.'}
                        </p>
                        <p class="empty-hint">
                            ${lang == 'en' ? 'Products will be loaded automatically. Please try again in a moment.' : '상품이 자동으로 로드됩니다. 잠시 후 다시 시도해주세요.'}
                        </p>
                    </div>
                </c:otherwise>
			</c:choose>


    </div>

    <%@ include file="footer.jsp" %>
</body>
</html>
