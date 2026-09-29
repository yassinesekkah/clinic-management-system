<%--
  Created by IntelliJ IDEA.
  User: pc
  Date: 29/09/2026
  Time: 18:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<html>
<head>
    <title>Clinic</title>
</head>
<body>
    <h1>Test JSTL</h1>

    <ul>
        <c:forEach var="nom" items="${noms}">
            <li>${nom}</li>
        </c:forEach>
    </ul>
</body>
</html>
