<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: max
  Date: 26.09.2016
  Time: 18:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<body>

<table>
    <tr><td>Order id:</td><td>${order.orderId}</td></tr>
    <tr><td>Name:</td><td>${order.name}</td></tr>
    <tr><td>Address: </td><td>${order.address}</td></tr>
    <tr><td>Telephone number</td><td>${order.telephone}</td></tr>
    <tr><td>Order date: </td><td>${order.orderDate}</td></tr>
</table>

<table>
    <tr>
        <th>Film Id</th>
        <th>Title</th>
        <th>Cost</th>
    </tr>

    <c:forEach var="film" items="${order.orderedFilms}">
        <tr>
            <td>${film.filmId}</td>
            <td>${film.title}</td>
            <td>${film.price}</td>
        </tr>
    </c:forEach>
    <td colspan="2"> Total cost:</td>
    <td>${order.totalCost}</td>
</table>

