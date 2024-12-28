<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/tags/timeUtil" prefix="timeUtil" %>
<html>
<head>
    <title>Meal list</title>
</head>
<body>
<table border="1">
    <caption>Список еды</caption>
    <a href="meals?methodSelect=createForm">Add meal</a>
    <tr>
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
    </tr>
    <c:forEach var="meal" items="${mealsList}">
        <tr style="color: ${meal.isExcess() ? 'red' : 'green'};">
            <td>${timeUtil:formatDate(meal.dateTime)}</td>
            <td>${meal.description}</td>
            <td>${meal.calories}</td>
            <td>
                <form action="meals" method="post" style="display:inline;">
                    <input type="hidden" name="id" value=${meal.id}>
                    <input type="hidden" name="methodSelect" value="delete">
                    <button type="submit">Delete</button>
                </form>
            </td>
            <td>
               <form action="meals" method="get" style="display:inline;">
                   <input type="hidden" name="id" value=${meal.id}>
                <input type="hidden" name="methodSelect" value="updateForm">
                <button type="submit">Update</button>
                </form>
</td>
</tr>
</c:forEach>
</table>
</body>
</html>
