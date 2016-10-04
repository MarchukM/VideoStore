<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html lang="en">
<head>
    <title>Video Store</title>
    <style type="text/css"> <%@include file="/css/index.css" %> </style>
</head>

<body bgcolor="white">
<div id="header">
    <p class="title">VIDEO STORE</p>
</div>


<c:choose>
    <c:when test="${param.action == 'registration'}">
        <div id="mainBlock">
            <jsp:include page="protected/registration.jsp"/>
        </div>
    </c:when>
    <c:when test="${sessionScope.validUser != null}">
        <c:redirect url="protected/main.jsp?action=filmList"></c:redirect>
    </c:when>


    <c:otherwise>
        <div id="authenticateForm">
            <form action="/Authenticate" method="post">
                <c:if test="${param.action == 'success'}">
                    <div id="success">Registration completed successfully.<br> Please log in.
                    </div>
                </c:if>
                <label for="username">Username:</label>
                <table>
                    <tr>
                        <td><input type="text"
                                   name="userName"
                                   id="username"
                                   size="25"><br>
                            <div class="message">${messages['userName']}</div>
                        </td>
                    </tr>
                </table>


                <label for="password">Password:</label>
                <table>
                    <tr>
                        <td><input type="password"
                                   id="password"
                                   name="password"
                                   size="25"><br>
                            <div class="message">${messages['password']}</div>
                        </td>
                    </tr>
                </table>




                <input type="submit" value="Sign In">
                <a href="index.jsp?action=registration">Sign Up</a>
            </form>
        </div>
    </c:otherwise>
</c:choose>


</body>


</html>