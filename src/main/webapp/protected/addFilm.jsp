<%--
  Created by IntelliJ IDEA.
  User: max
  Date: 27.09.2016
  Time: 17:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<style type="text/css"> <%@include file="/css/addFilm.css" %> </style>
<form action="/FilmListHandler" method="post" enctype="multipart/form-data">
    <table>
        <tr>
            <td>Title: </td>
            <td> <input type="text" name="title" value="титул"></td>
            <td><div class="message">${messages['title']}</div></td>
        </tr>
        <tr>
            <td>Producer: </td>
            <td><input type="text" name="producer" value="Продусер"></td>
            <td><div class="message">${messages['producer']}</div></td>
        </tr>
        <tr>
            <td>Year: </td>
            <td> <input type="text" name="year" value="1993"></td>
            <td><div class="message">${messages['year']}</div></td>
        </tr>
        <tr>
            <td>Country: </td>
            <td> <input type="text" name="country" value="УСА"></td>
            <td><div class="message">${messages['country']}</div></td>
        </tr>
        <tr>
            <td>Run time: </td>
            <td> <input type="text" name="runtime" value="4:20"></td>
            <td><div class="message">${messages['runTime']}</div></td>
        </tr>
        <tr>
            <td>Price: </td>
            <td> <input type="text" name="price" value="300"></td>
            <td><div class="message">${messages['price']}</div></td>
        </tr>
    </table>

    <div class="message">${messages['cover']}</div>
    <input type="file" name="file" id="file"/>

    <div class="message">${messages['description']}</div>
    <p><label for="description">Description:</label></p>
    <p><textarea id="description" cols="60" rows="6" name="description" maxlength="600">Описание фильма</textarea></p>

    <div class="message">${messages['genres']}</div>
    <table>
        <tr>
            <td><input type="checkbox" name="genre" value="комедия"> комедия</td>
            <td><input type="checkbox" name="genre" value="ужасы"> ужасы</td>
        </tr>
        <tr>
            <td><input type="checkbox" name="genre" value="драма"> драма</td>
            <td><input type="checkbox" name="genre" value="исторический"> исторический</td>
        </tr>
        <tr>
            <td><input type="checkbox" name="genre" value="криминал"> криминал</td>
            <td><input type="checkbox" name="genre" value="боевик"> боевик</td>
        </tr>
        <tr>
            <td><input type="checkbox" name="genre" value="триллер"> триллер</td>
            <td><input type="checkbox" name="genre" value="спорт"> спорт</td>
        </tr>
        <tr>
            <td><input type="checkbox" name="genre" value="военный"> военный</td>
            <td><input type="checkbox" name="genre" value="фантастика"> фантастика</td>
        </tr>
    </table>

    <input type="submit" value="Submit">
</form>
