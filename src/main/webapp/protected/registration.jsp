<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="language" value="${param.language}"/>

<style type="text/css"> <%@include file="/css/reg.css" %> </style>
<div id="registration">
<form action="/Registration" method="post">

    <table>
        <caption>Registration:</caption>
        <tr>
            <td>Username:</td>
            <td><input name="userName"
                       value="${userName}"
                       id="username"
                       size="20"></td>
            <td><div class="message">${messages['userName']}</div></td>
        </tr>
        <tr>
            <td>First name:</td>
            <td><input name="firstName"
                       value="${firstName}"
                       id="firstName"
                       size="20"></td>
            <td><div class="message">${messages['firstName']}</div></td>
        </tr>
        <tr>
            <td>Last name:</td>
            <td><input name="lastName"
                       value="${lastName}"
                       id="lastName"
                       size="20"></td>
            <td><div class="message">${messages['lastName']}</div></td>
        </tr>
        <tr>
            <td>Password:</td>
            <td><input type="password"
                       id="password"
                       name="password"
                       size="20"></td>
            <td><div class="message">${messages['password']}</div></td>
        </tr>
        <tr>
            <td>Confirm password:</td>
            <td><input type="password"
                       id="confirmPass"
                       name="confirmPass"
                       size="20"></td>
            <td><div class="message">${messages['confirmPass']}</div></td>
        </tr>
        <tr>
            <td>Email:</td>
            <td><input name="email"
                       value="${email}"
                       id="email"
                       size="20"></td>
            <td><div class="message">${messages['email']}</div></td>
        </tr>
    </table>
    <input type="submit" value="Sign up">
</form>
</div>
