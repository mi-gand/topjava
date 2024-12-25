<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>Edit Meal</title>
</head>
<body>
<h1>Edit meal</h1>
<a href="index.html">Home</a>
<form method="post" action="meals" >
    <input type="hidden" name="methodSelect" value="createOrUpdate">
    <input type="hidden" name="id" value=${param.id}>
    <table>
        <tr>
            <td><label for="dateTime">DateTime:</label></td>
            <td><input type="datetime-local" id="dateTime" name="dateTime" placeholder="Введите время приема пищи"
                       value=${param.dateTime} required /></td>
        </tr>
        <tr>
            <td><label for="description">Description:</label></td>
            <td><input type="text" id="description" name="description" placeholder="Что вы сьели?"
                       value=${param.description} required /></td>
        </tr>
        <tr>
            <td><label for="calories">Calories:</label></td>
            <td><input type="number" id="calories" name="calories" placeholder="Введите количество калорий"
                       value=${param.calories} required /></td>
        </tr>
    </table>
    <button type="submit">Save</button>
    <a href="meals">Cancel</a>
</form>
</body>
</html>