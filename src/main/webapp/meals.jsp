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
    <form action="meals" method="post" style="display:inline;">
        <input type="hidden" name="id" value="newMeal">
        <input type="hidden" name="methodSelect" value="createOrUpdate">
        <button type="submit">Add meal</button>
    </form>
    <tr>
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
    </tr>
    <c:forEach var="meal" items="${mealsList}">
        <tr style="color: ${meal.isExcess() ? 'red' : 'green'};">
            <td><fmt:formatDate value="${timeUtil:formatDate(meal.dateTime)}" pattern="yyyy-MM-dd HH:mm" /></td>
            <td>${meal.description}</td>
            <td>${meal.calories}</td>
            <td>
                <form action="meals" method="post" style="display:inline;">
                    <input type="hidden" name="id" value=${meal.dateTime}>
                    <input type="hidden" name="methodSelect" value="delete">
                    <button type="submit">Delete</button>
                </form>
            </td>
            <td>
                <form action="mealEdit.jsp" method="get" style="display:inline;">
                    <input type="hidden" name="id" value=${meal.id}>
                    <input type="hidden" name="dateTime" value=${meal.dateTime}>
                    <input type="hidden" name="description" value=${meal.description}>
                    <input type="hidden" name="calories" value=${meal.calories}>
                    <button type="submit">Update</button>
                </form>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
