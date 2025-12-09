<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ page import="data.CategoryData" %> 
<% pageContext.setAttribute("bodyParts",CategoryData.getBodyParts()); 
pageContext.setAttribute("sports",CategoryData.getSports()); 
pageContext.setAttribute("machines",CategoryData.getMachines()); 
pageContext.setAttribute("products",CategoryData.getProducts()); %>
 <%@ include file="header.jsp" %>

<!-- Hero Section -->
<section class="hero">
  	<h1 class="hero-title">${lang == 'ko' ? '워크' : 'WORK'}<br />${lang == 'ko' ? '아웃' : 'OUT'}</h1>
	<div class="hero-search">
	  <form action="${pageContext.request.contextPath}/exercises" method="get">
	    <input type="hidden" name="action" value="search">
	    <input type="text" name="q" 
	           placeholder="${lang == 'ko' ? '예) 이두근, 삼두근, 가슴' : 'ex) biceps, triceps, chest'}" />
	    <button type="submit">🔍</button>
	  </form>
	</div>
</section>

<!-- BODY Section -->
<section class="category-section">
  <h2 class="category-header">${lang == 'ko' ? '신체 부위' : 'BODY'}</h2>

  <input type="radio" id="body-all" name="body-tab" class="tab-radio" checked />
  <input type="radio" id="body-upper" name="body-tab" class="tab-radio" />
  <input type="radio" id="body-lower" name="body-tab" class="tab-radio" />

  <div class="category-tabs">
    <label for="body-all" class="category-tab">${lang == 'ko' ? '전체' : 'ALL'}</label>
    <label for="body-upper" class="category-tab">${lang == 'ko' ? '상체' : 'UPPER'}</label>
    <label for="body-lower" class="category-tab">${lang == 'ko' ? '하체' : 'LOWER'}</label>
  </div>

  <div class="grid-container">
    <div class="category-grid" id="bodyGrid-all">
      <c:forEach var="item" items="${bodyParts}">
        <div class="category-item" data-search-name="${item.name}">
          <div class="category-icon">${item.icon}</div>
          <div class="category-name">${lang == 'ko' ? item.nameKo : item.name}</div>
        </div>
      </c:forEach>
    </div>
  </div>

	<div class="svg-container">
	    <form id="muscleForm" action="${pageContext.request.contextPath}/exercises" method="get">
	        <input type="hidden" name="action" value="search">
	        <input type="hidden" id="hidden-q" name="q" value=""> 
	        <object
	          id="svg-front"
	          class="muscle-obj"
	          data="./asset/front.svg"
	          type="image/svg+xml"
	          width="250"
	          height="400"
	        ></object>
	        <object
	          id="svg-back"
	          class="muscle-obj"
	          data="./asset/back.svg"
	          type="image/svg+xml"
	          width="250"
	          height="400"
	        ></object>
	    </form>
	  </div>
</section>

<!-- SPORTS Section -->
<section class="category-section">
  <h2 class="category-header">${lang == 'ko' ? '스포츠' : 'SPORTS'}</h2>

  <input type="radio" id="sports-all" name="sports-tab" class="tab-radio" checked />
  <input type="radio" id="sports-water" name="sports-tab" class="tab-radio" />
  <input type="radio" id="sports-ground" name="sports-tab" class="tab-radio" />
  <input type="radio" id="sports-home" name="sports-tab" class="tab-radio" />
  <input type="radio" id="sports-etc" name="sports-tab" class="tab-radio" />

  <div class="category-tabs">
    <label for="sports-all" class="category-tab">${lang == 'ko' ? '전체' : 'ALL'}</label>
    <label for="sports-water" class="category-tab">${lang == 'ko' ? '수상' : 'WATER'}</label>
    <label for="sports-ground" class="category-tab">${lang == 'ko' ? '지상' : 'GROUND'}</label>
    <label for="sports-home" class="category-tab">${lang == 'ko' ? '홈' : 'HOME'}</label>
    <label for="sports-etc" class="category-tab">${lang == 'ko' ? '기타' : 'ETC'}</label>
  </div>

  <div class="grid-container">
    <!-- ALL Grid -->
    <div class="category-grid" id="sportsGrid-all">
      <c:forEach var="item" items="${sports}">
        <div class="category-item" data-search-name="${item.name}">
          <div class="category-icon">${item.icon}</div>
          <div class="category-name">${lang == 'ko' ? item.nameKo : item.name}</div>
        </div>
      </c:forEach>
    </div>

    <!-- WATER Grid -->
    <div class="category-grid" id="sportsGrid-water">
      <c:forEach var="item" items="${sports}">
        <c:if test="${item.category == 'water'}">
          <div class="category-item" data-search-name="${item.name}">
            <div class="category-icon">${item.icon}</div>
            <div class="category-name">${lang == 'ko' ? item.nameKo : item.name}</div>
          </div>
        </c:if>
      </c:forEach>
    </div>

    <!-- GROUND Grid -->
    <div class="category-grid" id="sportsGrid-ground">
      <c:forEach var="item" items="${sports}">
        <c:if test="${item.category == 'ground'}">
          <div class="category-item" data-search-name="${item.name}">
            <div class="category-icon">${item.icon}</div>
            <div class="category-name">${lang == 'ko' ? item.nameKo : item.name}</div>
          </div>
        </c:if>
      </c:forEach>
    </div>

    <!-- HOME Grid -->
    <div class="category-grid" id="sportsGrid-home">
      <c:forEach var="item" items="${sports}">
        <c:if test="${item.category == 'home'}">
          <div class="category-item" data-search-name="${item.name}">
            <div class="category-icon">${item.icon}</div>
            <div class="category-name">${lang == 'ko' ? item.nameKo : item.name}</div>
          </div>
        </c:if>
      </c:forEach>
    </div>

    <!-- ETC Grid -->
    <div class="category-grid" id="sportsGrid-etc">
      <c:forEach var="item" items="${sports}">
        <c:if test="${item.category == 'etc'}">
          <div class="category-item" data-search-name="${item.name}">
            <div class="category-icon">${item.icon}</div>
            <div class="category-name">${lang == 'ko' ? item.nameKo : item.name}</div>
          </div>
        </c:if>
      </c:forEach>
    </div>
  </div>
</section>

<!-- Products Section -->
<section class="products-section">
  <h2 class="section-title">${lang == 'ko' ? '오늘의 할인 상품' : 'Today\'s Discount'}</h2>
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

<script>
window.addEventListener('load', function() {
    var form = document.getElementById('muscleForm');
    var hiddenInput = document.getElementById('hidden-q');
    var currentLang = '${lang}';

    var setupSvgInteractions = function(svgObject) {
        svgObject.onload = function() {
            var svgDoc = svgObject.contentDocument;
            if (svgDoc) {
                var muscles = svgDoc.querySelectorAll('.muscle');
                muscles.forEach(function(muscle) {
                    muscle.style.cursor = 'pointer';
                    muscle.addEventListener('click', function() {
                        var muscleName = currentLang === 'ko' 
                            ? (this.dataset.valueK || this.dataset.valueE)
                            : this.dataset.valueE;

                        hiddenInput.value = muscleName;
                        form.submit();
                    });
                });
            }
        };
        
        if (svgObject.contentDocument) {
            svgObject.onload();
        }
    };

    document.querySelectorAll('.muscle-obj').forEach(setupSvgInteractions);

    var categoryItems = document.querySelectorAll('.category-item[data-search-name]');

    categoryItems.forEach(function(item) {
        item.style.cursor = 'pointer';
        item.addEventListener('click', function() {
            hiddenInput.value = this.dataset.searchName;
            form.submit();
        });
    });
});
</script>
<%@ include file="footer.jsp" %>