<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Connexion - Clinique</title>
    <style>
        * { box-sizing: border-box; }
        body {
            margin: 0;
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 24px;
            font-family: Arial, sans-serif;
            background: #f3f6fa;
            color: #243247;
        }
        main {
            width: 100%;
            max-width: 400px;
            padding: 32px;
            background: white;
            border: 1px solid #cbd5e1;
            border-radius: 12px;
        }
        h1 { margin: 0 0 24px; text-align: center; font-size: 26px; }
        form p { margin: 0 0 20px; }
        label { display: block; margin-bottom: 8px; font-weight: bold; }
        input:not([type="hidden"]) {
            width: 100%;
            padding: 12px;
            border: 1px solid #94a3b8;
            border-radius: 6px;
            font: inherit;
        }
        button {
            width: 100%;
            padding: 12px;
            border: none;
            border-radius: 6px;
            background: #1d4ed8;
            color: white;
            font: inherit;
            font-weight: bold;
            cursor: pointer;
        }
        button:hover { background: #1e40af; }
        input:focus-visible, button:focus-visible {
            outline: 3px solid #2563eb;
            outline-offset: 3px;
        }
        [role="alert"] {
            padding: 12px;
            border: 1px solid #fecaca;
            border-radius: 6px;
            background: #fef2f2;
            color: #991b1b;
        }
    </style>
</head>
<body>
    <main>
    <h1>Connexion</h1>

    <c:if test="${not empty error}">
        <p role="alert"><c:out value="${error}" /></p>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/login">
        <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">

        <p>
            <label for="email">Email</label>
            <input type="email" id="email" name="email" autocomplete="username" required>
        </p>
        <p>
            <label for="password">Mot de passe</label>
            <input type="password" id="password" name="password" autocomplete="current-password" required>
        </p>
        <button type="submit">Se connecter</button>
    </form>
    </main>
</body>
</html>
