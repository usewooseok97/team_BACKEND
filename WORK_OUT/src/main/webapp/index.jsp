<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="data.CategoryData" %>
<%
    // CategoryData를 사용하여 데이터 가져오기
    pageContext.setAttribute("bodyParts", CategoryData.getBodyParts());
    pageContext.setAttribute("sports", CategoryData.getSports());
    pageContext.setAttribute("machines", CategoryData.getMachines());
    pageContext.setAttribute("products", CategoryData.getProducts());
%>
<%@ include file="header.jsp" %>

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

<%@ include file="footer.jsp" %>
