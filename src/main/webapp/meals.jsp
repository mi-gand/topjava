<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <title>Meal list</title>
</head>
<body>
<table border="1">
    <caption>Список еды</caption>
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

        </tr>
    </c:forEach>
</table>
</body>
</html>
