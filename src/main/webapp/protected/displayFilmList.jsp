<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: max
  Date: 21.09.2016
  Time: 1:30
  To change this template use File | Settings | File Templates.
--%>

<%--<html>--%>
<%--<head>--%>
<%--<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">--%>
<%--<title>Employees</title>--%>
<style type="text/css"> <%@include file="../css/filmlist.css" %> </style>
<%--</head>--%>

<c:forEach var="film" items="${filmList}">

    <div id="film">
        <div id="filminfo">
            <div id="title">${film.title}</div>
            <div id="description">${film.description}</div>

            <div id="details">
                <table>
                    <tr>
                        <td><b>Producer:</b></td>
                        <td>${film.producer}</td>
                    </tr>
                    <tr>
                        <td><b>Year:</b></td>
                        <td>${film.year}</td>
                    </tr>
                    <tr>
                        <td><b>Country:</b></td>
                        <td>${film.country}</td>
                    </tr>
                    <tr>
                        <td><b>Run Time: </b></td>
                        <td>${film.runTime}</td>
                    </tr>
                    <tr>
                    <td><b>Genre:</b></td>
                    <td>
                        <c:forEach var="genre" items="${film.genres}">
                            ${genre}
                        </c:forEach>
                    </td>
                    <tr>
                        <td><b>Price:</b></td>
                        <td>
                            ${film.price} руб.
                        </td>
                    </tr>
                </tr>
                </table>
            </div>
            <div class="add">
                <a href="<c:url value="/CartHandler">
                    <c:param name="id" value="${film.filmId}"/>
                    <c:param name="action" value="add"/>
                    <c:param name="currentPage" value="${currentPage}"/>
                </c:url>">
                    Add to cart
                </a>
            </div>

        </div>
        <div id="cover"><img src="/images/${film.cover}" width="354" height="480"></div>
    </div>

</c:forEach>



<c:if test="${currentPage != 1}">
    <a id="previous" href="/FilmListHandler?page=${currentPage - 1}">Previous</a>
</c:if>


<%--<table border="1" cellpadding="5" cellspacing="5">--%>
<%--<tr>--%>
<%--<c:forEach begin="1" end="${noOfPages}" var="i">--%>
<%--<c:choose>--%>
<%--<c:when test="${currentPage eq i}">--%>
<%--<td>${i}</td>--%>
<%--</c:when>--%>
<%--<c:otherwise>--%>
<%--<td><a href="FilmListHandler?page=${i}">${i}</a></td>--%>
<%--</c:otherwise>--%>
<%--</c:choose>--%>
<%--</c:forEach>--%>
<%--</tr>--%>
<%--</table>--%>


<c:if test="${currentPage lt noOfPages}">
    <a id="next" href="/FilmListHandler?page=${currentPage + 1}">Next</a>
</c:if>