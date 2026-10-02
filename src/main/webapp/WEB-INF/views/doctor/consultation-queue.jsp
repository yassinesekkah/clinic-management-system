<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Patients en attente</title>
    <style>
        * { box-sizing: border-box; }
        body {
            margin: 0;
            padding: 40px 20px;
            background: #f4f7fb;
            color: #1f2937;
            font-family: Arial, sans-serif;
        }
        .container { max-width: 1100px; margin: 0 auto; }
        .header { margin-bottom: 24px; }
        .header h1 { margin: 0 0 8px; font-size: 28px; }
        .header p { margin: 0; color: #6b7280; }
        .table-card {
            overflow-x: auto;
            background: #fff;
            border: 1px solid #e5e7eb;
            border-radius: 12px;
            box-shadow: 0 4px 16px rgba(15, 23, 42, 0.06);
        }
        table { width: 100%; border-collapse: collapse; }
        th, td {
            padding: 16px;
            border-bottom: 1px solid #e5e7eb;
            text-align: left;
            vertical-align: middle;
        }
        th {
            background: #f8fafc;
            color: #475569;
            font-size: 13px;
            text-transform: uppercase;
        }
        tbody tr:last-child td { border-bottom: 0; }
        tbody tr:hover { background: #f8fbff; }
        .patient-name { font-weight: 700; }
        .vitals { color: #4b5563; font-size: 14px; line-height: 1.7; }
        .badge {
            display: inline-block;
            padding: 5px 9px;
            border-radius: 999px;
            background: #fff7ed;
            color: #c2410c;
            font-size: 12px;
            font-weight: 700;
        }
        .button {
            display: inline-block;
            padding: 9px 13px;
            border-radius: 7px;
            background: #2563eb;
            color: #fff;
            font-size: 14px;
            font-weight: 700;
            text-decoration: none;
            white-space: nowrap;
        }
        .button:hover { background: #1d4ed8; }
        .empty-state { padding: 50px 20px; color: #6b7280; text-align: center; }
    </style>
</head>
<body>
<main class="container">
    <header class="header">
        <h1>Patients en attente</h1>
        <p>Consultations en attente enregistrées aujourd'hui.</p>
    </header>

    <div class="table-card">
        <c:choose>
            <c:when test="${empty consultations}">
                <div class="empty-state">
                    Aucun patient en attente de consultation aujourd'hui.
                </div>
            </c:when>
            <c:otherwise>
                <table>
                    <thead>
                    <tr>
                        <th>Heure d'arrivée</th>
                        <th>Patient</th>
                        <th>Signes vitaux</th>
                        <th>Statut</th>
                        <th>Action</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="consultation" items="${consultations}">
                        <c:set var="p" value="${consultation.patient}" />
                        <tr>
                            <td><c:out value="${p.heureArrivee}" /></td>
                            <td class="patient-name">
                                <c:out value="${p.prenom}" />
                                <c:out value="${p.nom}" />
                            </td>
                            <td class="vitals">
                                Tension : <c:out value="${p.tensionArterielle}" /><br>
                                FC : <c:out value="${p.frequenceCardiaque}" /> bpm ·
                                Temp. : <c:out value="${p.temperature}" /> °C ·
                                FR : <c:out value="${p.frequenceRespiratoire}" />/min
                            </td>
                            <td><span class="badge">En attente</span></td>
                            <td>
                                <a class="button"
                                   href="${pageContext.request.contextPath}/consultations/nouvelle?patientId=${p.id}">
                                    Démarrer la consultation
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:otherwise>
        </c:choose>
    </div>
</main>
</body>
</html>
