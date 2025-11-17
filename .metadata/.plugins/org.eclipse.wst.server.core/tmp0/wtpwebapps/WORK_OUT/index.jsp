<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="data.CategoryData" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>WORK OUT - 운동 플랫폼</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
    <%
        // CategoryData를 사용하여 데이터 가져오기
        pageContext.setAttribute("bodyParts", CategoryData.getBodyParts());
        pageContext.setAttribute("sports", CategoryData.getSports());
        pageContext.setAttribute("machines", CategoryData.getMachines());
        pageContext.setAttribute("products", CategoryData.getProducts());
    %>
    
    <!-- Navigation Bar -->
    <nav class="navbar">
        <a href="#" class="logo">🏋️</a>
        <div class="search-container">
            <form action="search.jsp" method="get">
                <input type="text" name="q" class="search-input" placeholder="검색...">
            </form>
        </div>
        <div class="nav-right">
            <div class="lang-selector">
                <input type="radio" id="lang-en" name="language" class="lang-radio" checked>
                <label for="lang-en" class="lang-label">EN</label>
                /
                <input type="radio" id="lang-kr" name="language" class="lang-radio">
                <label for="lang-kr" class="lang-label">KR</label>
            </div>
            <a href="store.jsp" class="nav-button">store</a>
            <a href="register.jsp" class="nav-button">regist</a>
            <a href="login.jsp" class="nav-button login">login</a>
        </div>
    </nav>

    <!-- Hero Section -->
    <section class="hero">
        <h1 class="hero-title">WORK<br>OUT</h1>
        <div class="hero-search">
            <form action="search.jsp" method="get">
                <input type="text" name="q" placeholder="search...">
            </form>
        </div>
    </section>

    <!-- BODY Section -->
    <section class="category-section">
        <h2 class="category-header">BODY</h2>
        
        <!-- Radio buttons for tabs -->
        <input type="radio" id="body-all" name="body-tab" class="tab-radio" checked>
        <input type="radio" id="body-upper" name="body-tab" class="tab-radio">
        <input type="radio" id="body-lower" name="body-tab" class="tab-radio">
        
        <div class="category-tabs">
            <label for="body-all" class="category-tab">ALL</label>
            <label for="body-upper" class="category-tab">UPPER</label>
            <label for="body-lower" class="category-tab">LOWER</label>
        </div>
        
        <div class="grid-container">
            <!-- ALL Grid -->
            <div class="category-grid" id="bodyGrid-all">
                <c:forEach var="item" items="${bodyParts}">
                    <div class="category-item">
                        <div class="category-icon">${item.icon}</div>
                        <div class="category-name">${item.name}</div>
                    </div>
                </c:forEach>
            </div>

            <!-- UPPER Grid -->
            <div class="category-grid" id="bodyGrid-upper">
                <c:forEach var="item" items="${bodyParts}">
                    <c:if test="${item.category == 'upper'}">
                        <div class="category-item">
                            <div class="category-icon">${item.icon}</div>
                            <div class="category-name">${item.name}</div>
                        </div>
                    </c:if>
                </c:forEach>
            </div>

            <!-- LOWER Grid -->
            <div class="category-grid" id="bodyGrid-lower">
                <c:forEach var="item" items="${bodyParts}">
                    <c:if test="${item.category == 'lower'}">
                        <div class="category-item">
                            <div class="category-icon">${item.icon}</div>
                            <div class="category-name">${item.name}</div>
                        </div>
                    </c:if>
                </c:forEach>
            </div>
        </div>
    </section>

    <!-- SPORTS Section -->
    <section class="category-section">
        <h2 class="category-header">SPORTS</h2>
        
        <!-- Radio buttons for tabs -->
        <input type="radio" id="sports-all" name="sports-tab" class="tab-radio" checked>
        <input type="radio" id="sports-water" name="sports-tab" class="tab-radio">
        <input type="radio" id="sports-ground" name="sports-tab" class="tab-radio">
        <input type="radio" id="sports-home" name="sports-tab" class="tab-radio">
        <input type="radio" id="sports-etc" name="sports-tab" class="tab-radio">
        
        <div class="category-tabs">
            <label for="sports-all" class="category-tab">ALL</label>
            <label for="sports-water" class="category-tab">WATER</label>
            <label for="sports-ground" class="category-tab">GROUND</label>
            <label for="sports-home" class="category-tab">HOME</label>
            <label for="sports-etc" class="category-tab">ETC</label>
        </div>
        
        <div class="grid-container">
            <!-- ALL Grid -->
            <div class="category-grid" id="sportsGrid-all">
                <c:forEach var="item" items="${sports}">
                    <div class="category-item">
                        <div class="category-icon">${item.icon}</div>
                        <div class="category-name">${item.name}</div>
                    </div>
                </c:forEach>
            </div>

            <!-- WATER Grid -->
            <div class="category-grid" id="sportsGrid-water">
                <c:forEach var="item" items="${sports}">
                    <c:if test="${item.category == 'water'}">
                        <div class="category-item">
                            <div class="category-icon">${item.icon}</div>
                            <div class="category-name">${item.name}</div>
                        </div>
                    </c:if>
                </c:forEach>
            </div>

            <!-- GROUND Grid -->
            <div class="category-grid" id="sportsGrid-ground">
                <c:forEach var="item" items="${sports}">
                    <c:if test="${item.category == 'ground'}">
                        <div class="category-item">
                            <div class="category-icon">${item.icon}</div>
                            <div class="category-name">${item.name}</div>
                        </div>
                    </c:if>
                </c:forEach>
            </div>

            <!-- HOME Grid -->
            <div class="category-grid" id="sportsGrid-home">
                <c:forEach var="item" items="${sports}">
                    <c:if test="${item.category == 'home'}">
                        <div class="category-item">
                            <div class="category-icon">${item.icon}</div>
                            <div class="category-name">${item.name}</div>
                        </div>
                    </c:if>
                </c:forEach>
            </div>

            <!-- ETC Grid -->
            <div class="category-grid" id="sportsGrid-etc">
                <c:forEach var="item" items="${sports}">
                    <c:if test="${item.category == 'etc'}">
                        <div class="category-item">
                            <div class="category-icon">${item.icon}</div>
                            <div class="category-name">${item.name}</div>
                        </div>
                    </c:if>
                </c:forEach>
            </div>
        </div>
    </section>

    <!-- MACHINE Section -->
    <section class="category-section">
        <h2 class="category-header">MACHINE</h2>
        
        <!-- Radio buttons for tabs -->
        <input type="radio" id="machine-all" name="machine-tab" class="tab-radio" checked>
        <input type="radio" id="machine-upper" name="machine-tab" class="tab-radio">
        <input type="radio" id="machine-lower" name="machine-tab" class="tab-radio">
        <input type="radio" id="machine-etc" name="machine-tab" class="tab-radio">
        
        <div class="category-tabs">
            <label for="machine-all" class="category-tab">ALL</label>
            <label for="machine-upper" class="category-tab">UPPER</label>
            <label for="machine-lower" class="category-tab">LOWER</label>
            <label for="machine-etc" class="category-tab">ETC</label>
        </div>
        
        <div class="grid-container">
            <!-- ALL Grid -->
            <div class="category-grid" id="machineGrid-all">
                <c:forEach var="item" items="${machines}">
                    <div class="category-item">
                        <div class="category-icon">${item.icon}</div>
                        <div class="category-name">${item.name}</div>
                    </div>
                </c:forEach>
            </div>

            <!-- UPPER Grid -->
            <div class="category-grid" id="machineGrid-upper">
                <c:forEach var="item" items="${machines}">
                    <c:if test="${item.category == 'upper'}">
                        <div class="category-item">
                            <div class="category-icon">${item.icon}</div>
                            <div class="category-name">${item.name}</div>
                        </div>
                    </c:if>
                </c:forEach>
            </div>

            <!-- LOWER Grid -->
            <div class="category-grid" id="machineGrid-lower">
                <c:forEach var="item" items="${machines}">
                    <c:if test="${item.category == 'lower'}">
                        <div class="category-item">
                            <div class="category-icon">${item.icon}</div>
                            <div class="category-name">${item.name}</div>
                        </div>
                    </c:if>
                </c:forEach>
            </div>

            <!-- ETC Grid -->
            <div class="category-grid" id="machineGrid-etc">
                <c:forEach var="item" items="${machines}">
                    <c:if test="${item.category == 'etc'}">
                        <div class="category-item">
                            <div class="category-icon">${item.icon}</div>
                            <div class="category-name">${item.name}</div>
                        </div>
                    </c:if>
                </c:forEach>
            </div>
        </div>
    </section>

    <!-- Products Section -->
    <section class="products-section">
        <h2 class="section-title">Today's Discount</h2>
        <div class="products-grid">
            <c:forEach var="product" items="${products}">
                <div class="product-card">
                    <div class="bookmark-icon">🔖</div>
                    <div class="product-image">${product.image}</div>
                    <div class="product-brand">${product.brand}</div>
                    <div class="product-name">${product.name}</div>
                    <div class="product-price">
                        <span class="original">${product.originalPrice}$</span>
                        <span class="discount">${product.price}$</span>
                    </div>
                </div>
            </c:forEach>
        </div>
    </section>

    <!-- Footer -->
    <footer class="footer">
        <div class="footer-links">
            <a href="#">Privacy Policy</a> |
            <a href="#">Terms of Service</a> |
            <a href="#">Medical Disclaimer</a> |
            <a href="#">Cookie Policy</a>
        </div>
        <div class="footer-disclaimer">
            *Medical Disclaimer: Always consult with a qualified healthcare professional before beginning any new diet or exercise program.
        </div>
        <div class="footer-copyright">
            © 2023 2조 강우석 이준영 조윤재. All rights reserved.
        </div>
    </footer>
</body>
</html>
