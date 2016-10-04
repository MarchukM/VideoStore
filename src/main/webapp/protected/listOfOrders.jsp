<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<table>
    <tr>
        <th>Order id</th>
        <th>Date</th>
        <th>Adress</th>
        <th>Amount</th>
    </tr>
    <c:forEach var="order" items="${orderList}">
        <tr>
            <td>${order.orderId}</td>
            <td>${order.orderDate}</td>
            <td>${order.address}</td>
            <td>${order.totalCost}</td>
            <td>
                <a href="<c:url value="/OrderHandler">
                    <c:param name="id" value="${order.orderId}"/>
                    <c:param name="action" value="details"/>
                </c:url>"> Details
                </a>
            </td>
        </tr>
    </c:forEach>
</table>

<c:if test="${currentPage != 1}">
    <td><a href="/OrderHandler?page=${currentPage - 1}&action=list">Previous</a></td>
</c:if>

<%--For displaying Page numbers.
The when condition does not display a link for the current page--%>
<%--<table border="1" cellpadding="5" cellspacing="5">--%>
<%--<tr>--%>
<%--<c:forEach begin="1" end="${noOfPages}" var="i">--%>
<%--<c:choose>--%>
<%--<c:when test="${currentPage eq i}">--%>
<%--<td>${i}</td>--%>
<%--</c:when>--%>
<%--<c:otherwise>--%>
<%--<td><a href="/OrderHandler?page=${i}">${i}</a></td>--%>
<%--</c:otherwise>--%>
<%--</c:choose>--%>
<%--</c:forEach>--%>
<%--</tr>--%>
<%--</table>--%>

<%--For displaying Next link --%>
<c:if test="${currentPage lt noOfPages}">
    <td><a href="/OrderHandler?page=${currentPage + 1}&action=list">Next</a></td>
</c:if>