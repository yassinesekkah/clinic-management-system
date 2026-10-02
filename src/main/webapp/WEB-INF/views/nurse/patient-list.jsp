<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>File d'Attente des Patients du Jour</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
    <style>
        :root {
            --primary: #2563eb;
            --primary-hover: #1d4ed8;
            --primary-light: #eff6ff;
            --bg-page: #f8fafc;
            --surface: #ffffff;
            --text-main: #0f172a;
            --text-muted: #64748b;
            --border: #e2e8f0;
            --badge-waiting-bg: #fef3c7;
            --badge-waiting-text: #92400e;
            --badge-done-bg: #e2e8f0;
            --badge-done-text: #475569;
            --success-bg: #ecfdf5;
            --success-border: #a7f3d0;
            --success-text: #065f46;
            --radius-md: 10px;
            --radius-lg: 16px;
            --shadow-sm: 0 1px 3px rgba(0, 0, 0, 0.05);
            --shadow-md: 0 10px 25px -5px rgba(15, 23, 42, 0.06);
        }

        * {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
        }

        body {
            font-family: 'Inter', system-ui, -apple-system, sans-serif;
            background-color: var(--bg-page);
            color: var(--text-main);
            min-height: 100vh;
            padding: 40px 24px;
        }

        .container {
            max-width: 1100px;
            margin: 0 auto;
        }

        .top-navbar {
            display: flex;
            align-items: center;
            justify-content: space-between;
            margin-bottom: 28px;
        }

        .brand-badge {
            display: inline-flex;
            align-items: center;
            gap: 8px;
            font-weight: 600;
            font-size: 14px;
            color: var(--primary);
            background: var(--primary-light);
            padding: 6px 14px;
            border-radius: 9999px;
            border: 1px solid rgba(37, 99, 235, 0.2);
        }

        .page-header {
            display: flex;
            align-items: center;
            justify-content: space-between;
            flex-wrap: wrap;
            gap: 16px;
            margin-bottom: 24px;
        }

        .header-title h1 {
            font-size: 26px;
            font-weight: 700;
            letter-spacing: -0.02em;
        }

        .header-title p {
            margin-top: 4px;
            font-size: 14px;
            color: var(--text-muted);
        }

        .btn {
            display: inline-flex;
            align-items: center;
            gap: 8px;
            height: 42px;
            padding: 0 20px;
            border-radius: var(--radius-md);
            font-size: 14px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.2s ease;
            text-decoration: none;
        }

        .btn-primary {
            background-color: var(--primary);
            color: #fff;
            border: 1px solid var(--primary);
            box-shadow: 0 2px 4px rgba(37, 99, 235, 0.2);
        }

        .btn-primary:hover {
            background-color: var(--primary-hover);
            transform: translateY(-1px);
        }

        .btn-secondary {
            background-color: #f1f5f9;
            color: #334155;
            border: 1px solid var(--border);
        }

        .btn-secondary:hover {
            background-color: #e2e8f0;
            color: #0f172a;
        }

        .btn-outline {
            background-color: transparent;
            color: var(--text-muted);
            border: 1px solid var(--border);
        }

        .btn-outline:hover {
            background-color: #f8fafc;
            color: var(--text-main);
        }

        .alert-success {
            background-color: var(--success-bg);
            border: 1px solid var(--success-border);
            color: var(--success-text);
            border-radius: var(--radius-md);
            padding: 14px 20px;
            margin-bottom: 24px;
            display: flex;
            align-items: center;
            gap: 10px;
            font-size: 14px;
            font-weight: 500;
            box-shadow: var(--shadow-sm);
        }

        .alert-error {
            background-color: #fef2f2;
            border: 1px solid #fecaca;
            color: #991b1b;
            border-radius: var(--radius-md);
            padding: 14px 20px;
            margin-bottom: 24px;
            display: flex;
            align-items: center;
            gap: 10px;
            font-size: 14px;
            font-weight: 500;
            box-shadow: var(--shadow-sm);
        }

        .search-toolbar {
            background: var(--surface);
            border: 1px solid var(--border);
            border-radius: var(--radius-lg);
            padding: 16px 20px;
            margin-bottom: 20px;
            display: flex;
            align-items: center;
            justify-content: space-between;
            gap: 16px;
            flex-wrap: wrap;
            box-shadow: var(--shadow-sm);
        }

        .search-form {
            display: flex;
            align-items: center;
            gap: 10px;
            flex-wrap: wrap;
        }

        .search-input {
            height: 42px;
            padding: 0 16px;
            font-size: 14px;
            font-family: inherit;
            border: 1px solid var(--border);
            border-radius: var(--radius-md);
            outline: none;
            width: 260px;
            transition: border-color 0.2s, box-shadow 0.2s;
        }

        .search-input:focus {
            border-color: var(--primary);
            box-shadow: 0 0 0 3px rgba(37, 99, 235, 0.15);
        }

        .btn-action-consultation {
            display: inline-flex;
            align-items: center;
            gap: 4px;
            padding: 6px 12px;
            font-size: 12.5px;
            font-weight: 600;
            color: var(--primary);
            background: var(--primary-light);
            border: 1px solid rgba(37, 99, 235, 0.2);
            border-radius: var(--radius-md);
            text-decoration: none;
            transition: all 0.2s ease;
            white-space: nowrap;
        }

        .btn-action-consultation:hover {
            background-color: var(--primary);
            color: #ffffff;
            transform: translateY(-1px);
        }

        .table-card {
            background: var(--surface);
            border-radius: var(--radius-lg);
            border: 1px solid var(--border);
            box-shadow: var(--shadow-md);
            overflow: hidden;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            text-align: left;
            font-size: 13.5px;
        }

        thead {
            background-color: #f1f5f9;
            border-bottom: 1px solid var(--border);
        }

        th {
            padding: 14px 20px;
            font-weight: 600;
            color: var(--text-muted);
            text-transform: uppercase;
            font-size: 11.5px;
            letter-spacing: 0.05em;
        }

        tbody tr {
            border-bottom: 1px solid var(--border);
            transition: background-color 0.15s ease;
        }

        tbody tr:last-child {
            border-bottom: none;
        }

        tbody tr:hover {
            background-color: #f8fafc;
        }

        td {
            padding: 16px 20px;
            vertical-align: middle;
        }

        .patient-name {
            font-weight: 600;
            color: var(--text-main);
            font-size: 14px;
        }

        .ssn-badge {
            font-family: monospace;
            font-size: 12px;
            background: #f1f5f9;
            color: #334155;
            padding: 3px 8px;
            border-radius: 6px;
            border: 1px solid var(--border);
        }

        .time-badge {
            display: inline-flex;
            align-items: center;
            gap: 4px;
            color: var(--text-muted);
            font-size: 13px;
        }

        .vitals-chip {
            display: inline-flex;
            align-items: center;
            gap: 4px;
            background: #f8fafc;
            border: 1px solid var(--border);
            padding: 3px 8px;
            border-radius: 6px;
            font-size: 12.5px;
            font-weight: 500;
        }

        .badge-status {
            display: inline-flex;
            align-items: center;
            padding: 4px 10px;
            border-radius: 9999px;
            font-size: 12px;
            font-weight: 600;
        }

        .badge-waiting {
            background-color: var(--badge-waiting-bg);
            color: var(--badge-waiting-text);
        }

        .badge-done {
            background-color: var(--badge-done-bg);
            color: var(--badge-done-text);
        }

        .empty-state {
            padding: 60px 20px;
            text-align: center;
        }

        .empty-state h3 {
            font-size: 17px;
            font-weight: 600;
            color: var(--text-main);
            margin-bottom: 8px;
        }

        .empty-state p {
            font-size: 14px;
            color: var(--text-muted);
            margin-bottom: 20px;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="top-navbar">
            <span class="brand-badge">Clinique - Module Infirmier</span>
            <div style="font-size: 13px; color: var(--text-muted);">
                Date : <strong><%= java.time.LocalDate.now() %></strong>
            </div>
        </div>

        <c:if test="${not empty successMessage}">
            <div class="alert-success" role="status">
                <svg width="20" height="20" viewBox="0 0 20 20" fill="currentColor">
                    <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clip-rule="evenodd"/>
                </svg>
                <c:out value="${successMessage}" />
            </div>
        </c:if>

        <c:if test="${not empty errorMessage}">
            <div class="alert-error" role="alert">
                <svg width="20" height="20" viewBox="0 0 20 20" fill="currentColor">
                    <path fill-rule="evenodd" d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7 4a1 1 0 11-2 0 1 1 0 012 0zm-1-9a1 1 0 00-1 1v4a1 1 0 102 0V6a1 1 0 00-1-1z" clip-rule="evenodd"/>
                </svg>
                <c:out value="${errorMessage}" />
            </div>
        </c:if>

        <div class="page-header">
            <div class="header-title">
                <h1>File d'attente du jour</h1>
                <p>Consultez et suivez l'ordre d'arrivée des patients admis pour consultation.</p>
            </div>
            <a href="${pageContext.request.contextPath}/patients/nouveau" class="btn btn-primary" id="btn-nouveau-patient">
                + Admettre un patient
            </a>
        </div>

        <!-- Barre de recherche multi-critères (Nom, Prénom, SSN ou ID) -->
        <div class="search-toolbar">
            <form method="GET" action="${pageContext.request.contextPath}/patients" class="search-form">
                <input type="text" name="search" class="search-input" placeholder="Rechercher par nom, prénom, SSN ou ID..."
                       value="<c:out value='${searchQuery != null ? searchQuery : param.id}'/>" required />
                <button type="submit" class="btn btn-secondary">Rechercher</button>
                <c:if test="${IDsearch}">
                    <a href="${pageContext.request.contextPath}/patients" class="btn btn-outline">Réinitialiser</a>
                </c:if>
            </form>
            <c:if test="${IDsearch}">
                <div style="font-size: 13px; color: var(--text-muted);">
                    Filtre actif : Recherche "<strong><c:out value="${searchQuery != null ? searchQuery : param.id}" /></strong>"
                </div>
            </c:if>
        </div>

        <div class="table-card">
            <c:choose>
                <c:when test="${empty patients}">
                    <div class="empty-state">
                        <c:choose>
                            <c:when test="${IDsearch}">
                                <h3>Aucun patient trouvé</h3>
                                <p>Aucun dossier patient ne correspond à votre recherche "<strong><c:out value="${searchQuery != null ? searchQuery : param.id}" /></strong>".</p>
                                <a href="${pageContext.request.contextPath}/patients" class="btn btn-primary" style="margin-top: 12px;">
                                    Voir tous les patients du jour
                                </a>
                            </c:when>
                            <c:otherwise>
                                <h3>Aucun patient dans la file d'attente aujourd'hui</h3>
                                <p>Les patients enregistrés par l'infirmier apparaîtront ici par ordre d'arrivée.</p>
                                <a href="${pageContext.request.contextPath}/patients/nouveau" class="btn btn-primary">
                                    Enregistrer le premier patient
                                </a>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </c:when>
                <c:otherwise>
                    <table>
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Heure</th>
                                <th>Patient</th>
                                <th>N° Sécurité Sociale</th>
                                <th>Tension</th>
                                <th>Fréq. Cardiaque</th>
                                <th>Température</th>
                                <th>Fréq. Resp.</th>
                                <th>Statut</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="p" items="${patients}">
                                <tr>
                                    <td>
                                        <span class="ssn-badge" style="font-weight: 600; color: var(--primary);">#<c:out value="${p.id}" /></span>
                                    </td>
                                    <td>
                                        <span class="time-badge">
                                            ${p.heureArrivee.toLocalTime().toString().substring(0, 5)}
                                        </span>
                                    </td>
                                    <td>
                                        <div class="patient-name"><c:out value="${p.nomComplet}" /></div>
                                    </td>
                                    <td>
                                        <span class="ssn-badge"><c:out value="${p.numeroSecuriteSociale}" /></span>
                                    </td>
                                    <td>
                                        <span class="vitals-chip"><c:out value="${p.tensionArterielle}" /> mmHg</span>
                                    </td>
                                    <td>
                                        <span class="vitals-chip"><c:out value="${p.frequenceCardiaque}" /> bpm</span>
                                    </td>
                                    <td>
                                        <span class="vitals-chip"><c:out value="${p.temperature}" /> °C</span>
                                    </td>
                                    <td>
                                        <span class="vitals-chip"><c:out value="${p.frequenceRespiratoire}" /> c/min</span>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${p.statut == 'EN_ATTENTE'}">
                                                <span class="badge-status badge-waiting">En attente</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge-status badge-done"><c:out value="${p.statut}" /></span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/patients?action=creerConsultation&patientId=${p.id}"
                                           class="btn-action-consultation"
                                           onclick="return confirm('Ouvrir une consultation en attente pour ce patient ?');"
                                           title="Ouvrir une consultation en attente pour ce patient">
                                            ➕ Consultation
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</body>
</html>
