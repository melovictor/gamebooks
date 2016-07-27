<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div id="marketContent">
    <div class="marketBalance">
        <spring:message code="page.lw.label.market.balance" />
        <span id="marketBalanceActual"></span>
    </div>
    <input type="hidden" id="singleCcy" value="<spring:message code="${marketCommand.singleCcyKey}" />" />
    <input type="hidden" id="multipleCcy" value="<spring:message code="${marketCommand.multipleCcyKey}" />" />
    <input type="hidden" id="currentGold" value="${data[marketCommand.moneyAttribute]}" />
    <input type="hidden" id="mustHaveGold" value="${marketCommand.mustHaveGold}" />
    <input type="hidden" id="mustBuy" value="${marketCommand.mustBuy}" />
    <input type="hidden" id="mustSellExactly" value="${marketCommand.mustSellExactly}" />
    <c:if test="${marketCommand.itemsForSaleCount > 0}">
        <h2>
            <c:if test="${not empty marketCommand.saleLabel}">
                ${marketCommand.saleLabel }
            </c:if>
            <c:if test="${empty marketCommand.saleLabel}">
                <spring:message code="page.lw.label.market.sale" />
            </c:if>
        </h2>
        <div id="marketForSale">
            <c:forEach items="${marketCommand.itemsForSale}" var="item">
              <div class="row">
                  <div data-id="${item.id}" data-price="${item.price}" data-stock="${item.stock}">
                      <span>${item.name}</span>
                  </div>
                  <c:if test="${item.price > 0 }">
                    <div class="marketPrice">
                        <spring:message code="${item.price == 1 ? marketCommand.singleCcyKey : marketCommand.multipleCcyKey}Price" arguments="${item.price}" />
                    </div>
                  </c:if>
                  <c:if test="${item.stock > 1 }">
                    <div class="marketStock">
                        <spring:message code="page.lw.label.market.stock" arguments="${item.stock}" />
                    </div>
                  </c:if>
              </div>
            </c:forEach>
        </div>
    </c:if>
    
    <c:if test="${marketCommand.itemsForPurchaseCount > 0}">
        <h2>
            <c:if test="${not empty marketCommand.purchaseLabel}">
                ${marketCommand.purchaseLabel }
            </c:if>
            <c:if test="${empty marketCommand.purchaseLabel}">
                <spring:message code="${not empty marketCommand.giveUpMode ? 'page.lw.label.market.giveUp' : 'page.lw.label.market.purchase'}" />
            </c:if>
        </h2>
        <div id="marketForPurchase">
            <c:forEach items="${marketCommand.itemsForPurchase}" var="item">
              <c:if test="${item.stock > 0}">
                  <div class="row">
                      <div data-id="${item.id}" data-price="${item.price}" data-stock="${item.stock}">
                          <span>${item.name}</span>
                      </div>
                      <c:if test="${empty marketCommand.giveUpMode}">
                          <c:if test="${item.price > 0 }">
                              <div class="marketPrice">
                                  <spring:message code="${item.price == 1 ? marketCommand.singleCcyKey : marketCommand.multipleCcyKey}Price" arguments="${item.price}" />
                              </div>
                          </c:if>
                      </c:if>
                      <c:if test="${item.stock > 1 }">
                        <div class="marketStock">
                            <spring:message code="page.lw.label.market.stock" arguments="${item.stock}" />
                        </div>
                      </c:if>
                  </div>
              </c:if>
            </c:forEach>
        </div>
    </c:if>

    <c:if test="${empty marketCommand.giveUpMode}">
       <button data-market-close="lw" type="button" id="marketCommandFinish"><spring:message code="page.lw.label.market.finish" /></button>
    </c:if>
</div>
