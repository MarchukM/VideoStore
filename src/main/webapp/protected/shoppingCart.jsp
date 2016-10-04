<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="с" uri="http://java.sun.com/jsp/jstl/core" %>


<style type="text/css"> <%@include file="/css/cart.css" %> </style>
<с:set var="cart" value="${sessionScope.shoppingCart}"/>
<c:choose>
    <c:when test="${empty cart.films}">
        <h1> Your cart is empty.<h1>
    </c:when>
    <c:otherwise>
        <table id="cart">
            <tr>
                <caption>Your cart:</caption>
                <th>ID</th>
                <th>Title</th>
                <th>Cost</th>
                <th></th>
            </tr>
            <c:set var="totalCost" value="${0}"/>
            <c:forEach var="film" items="${cart.films}">
                <tr>
                    <td>${film.filmId}</td>
                    <td>${film.title}</td>
                    <td>${film.price}</td>
                    <td>
                        <a href="<c:url value="/CartHandler">
                    <c:param name="id" value="${film.filmId}"/>
                    <c:param name="action" value="remove"/>
                </c:url>"> x
                        </a>
                    </td>
                    <c:set var="totalCost" value="${cart.totalCost}"/>
                </tr>
            </c:forEach>
            <td>
            <td colspan="2"> Total cost:</td>
            <td colspan="2">${totalCost}</td>
            </td>
            <tr>
                <td colspan="4"><a href="/protected/main.jsp?action=checkout">Checkout</a></td>
            </tr>
        </table>

    </c:otherwise>
</c:choose>