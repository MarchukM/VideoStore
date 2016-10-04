<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Video Store</title>
    <style type="text/css"> <%@include file="/css/index.css" %> </style>
</head>
<body>
<c:set var="isAdmin" value="${sessionScope.validUser.admin}"/>

<div id="header">
    <a href="/FilmListHandler" class="title">VIDEO STORE</a>
    <table id="headerTable">
            <tr>
                <c:if test="${sessionScope.validUser != null}">
                 <td><a class="headerLink" href="/LogOut">Logout</a></td>
                    <td> <a class="headerLink" href="${pageContext.request.contextPath}/protected/main.jsp?action=cart">Cart</a></td>
                </c:if>
                <c:if test="${isAdmin == true}">
                    <td>  <a class="headerLink"
                              href="${pageContext.request.contextPath}/protected/main.jsp?mode=admin&action=adminPage">admin</a></td>
                </c:if>
            </tr>

    </table>
</div>


<c:choose>
    <c:when test="${param.mode == 'admin'}">
        <div id="mainBlock">
            <c:choose>
                <c:when test="${isAdmin == true && param.action == 'adminPage'}">
                    <jsp:include page="adminPannel.jsp"/>
                </c:when>
                <c:when test="${isAdmin == true && param.action == 'listOfOrders'}">
                    <jsp:include page="listOfOrders.jsp"/>
                </c:when>
                <c:when test="${isAdmin == true && param.action == 'details'}">
                    <jsp:include page="orderDetails.jsp"/>
                </c:when>
                <c:when test="${isAdmin == true && param.action == 'addFilm'}">
                    <jsp:include page="addFilm.jsp"/>
                </c:when>
                <c:otherwise>
                    <h1>Nope!</h1>
                </c:otherwise>
            </c:choose>
        </div>
    </c:when>
    <c:when test="${param.action == 'gratitude'}">
        <div id="mainBlock">
            <jsp:include page="gratitude.html"/>
        </div>
    </c:when>
    <c:when test="${param.action == 'checkout'}">
        <div id="mainBlock">
            <jsp:include page="checkout.jsp"/>
        </div>
    </c:when>
    <c:when test="${param.action == 'cart'}">
        <div id="mainBlock">
            <jsp:include page="shoppingCart.jsp"/>
        </div>
    </c:when>
    <c:otherwise>
        <div id="mainBlock">
            <c:if test="${currentPage == null}">
                <jsp:forward page="/FilmListHandler"></jsp:forward>
            </c:if>
            <jsp:include page="displayFilmList.jsp"></jsp:include>
        </div>
    </c:otherwise>
</c:choose>

<%--<h1> MAIN PAGE</h1>--%>
</body>
</html>
