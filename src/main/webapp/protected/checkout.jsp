
<form action="/OrderHandler" method="post">
    <table>
        <tr>
            <td>Name</td>
            <td><input name="name"
                         id="name"
                         size="15"></td>
            <td>${messages['name']}</td>
        </tr>
        <tr>
            <td>Address:</td>
            <td> <input id="address"
                        name="address"
                        size="15"></td>
            <td>${messages['address']}</td>
        </tr>
        <tr>
            <td>Telephone:</td>
            <td><input id="telephone"
                        name="telephone"
                        size="15"></td>
            <td>${messages['telephone']}</td>
        </tr>
    </table>
    <input type="submit" value="Confirm">
</form>

