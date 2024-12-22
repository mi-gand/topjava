<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <title>Meal list</title>
</head>
<body>
<table border="1">
    <caption>Список еды</caption>
    <a href="mealEdit.jsp">Add meal</a>
    <tr>
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
    </tr>
    <c:forEach var="meal" items="${mealsList}">
        <tr style="color: ${meal.isExcess() ? 'red' : 'green'};">
            <td><fmt:formatDate value="${timeUtil.formatDate(meal.getDateTime())}" pattern="yyyy-MM-dd HH:mm" /></td>
            <td>${meal.getDescription()}</td>
            <td>${meal.getCalories()}</td>
            <td>
                <form action="meals" method="post" style="display:inline;">
                    <input type="hidden" name="id" value=${meal.getId()}>
                    <input type="hidden" name="methodSelect" value="delete">
                    <button type="submit">Delete</button>
                </form>
            </td>
            <td>
                <form action="mealEdit.jsp" method="get" style="display:inline;">
                    <input type="hidden" name="id" value=${meal.getId()}>
                    <input type="hidden" name="dateTime" value=${meal.getDateTime()}>
                    <input type="hidden" name="description" value=${meal.getDescription()}>
                    <input type="hidden" name="calories" value=${meal.getCalories()}>
                    <button type="submit">Update</button>
                </form>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
