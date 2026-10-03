<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Consultations du Jour - Clinique</title>
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
            --warning-bg: #fff7ed;
            --warning-border: #fed7aa;
            --warning-text: #c2410c;
            --success-bg: #ecfdf5;
            --success-border: #a7f3d0;
            --success-text: #065f46;
            --radius-md: 10px;
            --radius-lg: 14px;
            --shadow-sm: 0 1px 3px rgba(0, 0, 0, 0.04);
            --shadow-md: 0 4px 16px rgba(15, 23, 42, 0.06);
        }

        * { box-sizing: border-box; margin: 0; padding: 0; }

        body {
            font-family: 'Inter', system-ui, -apple-system, sans-serif;
            background-color: var(--bg-page);
            color: var(--text-main);
            min-height: 100vh;
            padding: 32px 20px;
        }

        .container { max-width: 1150px; margin: 0 auto; }

        /* Top Navigation Bar */
        .top-navbar {
            display: flex;
            align-items: center;
            justify-content: space-between;
            margin-bottom: 24px;
            padding-bottom: 16px;
            border-bottom: 1px solid var(--border);
        }
        .clinic-brand {
            font-size: 15px;
            font-weight: 700;
            color: var(--primary);
            display: inline-flex;
            align-items: center;
            gap: 8px;
        }
        .user-tag {
            font-size: 13px;
            padding: 4px 12px;
            border-radius: 999px;
            font-weight: 600;
            background: #f1f5f9;
            color: #334155;
        }
        .user-tag.doctor {
            background: #eff6ff;
            color: #1d4ed8;
            border: 1px solid #bfdbfe;
        }
        .user-tag.nurse {
            background: #f0fdf4;
            color: #15803d;
            border: 1px solid #bbf7d0;
        }

        /* Page Header */
        .page-header {
            margin-bottom: 24px;
            display: flex;
            justify-content: space-between;
            align-items: flex-end;
            flex-wrap: wrap;
            gap: 16px;
        }
        .page-header h1 {
            font-size: 24px;
            font-weight: 700;
            color: var(--text-main);
            letter-spacing: -0.02em;
        }
        .page-header p {
            font-size: 14px;
            color: var(--text-muted);
            margin-top: 4px;
        }

        /* Stats Cards */
        .stats-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 16px;
            margin-bottom: 24px;
        }
        .stat-card {
            background: var(--surface);
            border: 1px solid var(--border);
            border-radius: var(--radius-md);
            padding: 16px 20px;
            box-shadow: var(--shadow-sm);
        }
        .stat-card .label {
            font-size: 12px;
            text-transform: uppercase;
            font-weight: 600;
            letter-spacing: 0.05em;
            color: var(--text-muted);
        }
        .stat-card .value {
            font-size: 26px;
            font-weight: 700;
            margin-top: 4px;
            color: var(--text-main);
        }
        .stat-card.waiting .value { color: #d97706; }
        .stat-card.done .value { color: #059669; }

        /* Table Card */
        .table-card {
            background: var(--surface);
            border: 1px solid var(--border);
            border-radius: var(--radius-lg);
            box-shadow: var(--shadow-md);
            overflow: hidden;
        }
        table {
            width: 100%;
            border-collapse: collapse;
        }
        th, td {
            padding: 14px 18px;
            text-align: left;
            vertical-align: middle;
            border-bottom: 1px solid var(--border);
        }
        th {
            background: #f8fafc;
            color: #475569;
            font-size: 12px;
            font-weight: 600;
            text-transform: uppercase;
            letter-spacing: 0.03em;
        }
        tbody tr:last-child td { border-bottom: none; }
        tbody tr:hover { background-color: #fbfcfe; }

        .time-col {
            font-size: 13px;
            font-weight: 600;
            color: var(--text-muted);
        }
        .patient-cell .name {
            font-weight: 600;
            font-size: 14px;
            color: var(--text-main);
        }
        .patient-cell .ssn {
            font-size: 12px;
            color: var(--text-muted);
            margin-top: 2px;
        }

        /* Vital Signs Chips */
        .vitals-wrap {
            display: flex;
            flex-wrap: wrap;
            gap: 6px;
        }
        .vital-chip {
            background: #f1f5f9;
            padding: 3px 8px;
            border-radius: 6px;
            font-size: 12px;
            font-weight: 500;
            color: #334155;
        }

        /* Status Badge */
        .badge {
            display: inline-flex;
            align-items: center;
            gap: 6px;
            padding: 4px 10px;
            border-radius: 999px;
            font-size: 12px;
            font-weight: 600;
        }
        .badge-waiting {
            background: var(--warning-bg);
            color: var(--warning-text);
            border: 1px solid var(--warning-border);
        }
        .badge-done {
            background: var(--success-bg);
            color: var(--success-text);
            border: 1px solid var(--success-border);
        }

        /* Action Buttons & Tags */
        .btn-start {
            display: inline-flex;
            align-items: center;
            justify-content: center;
            padding: 8px 14px;
            background: var(--primary);
            color: #ffffff;
            font-size: 13px;
            font-weight: 600;
            border-radius: 8px;
            text-decoration: none;
            transition: all 0.15s ease;
        }
        .btn-start:hover {
            background: var(--primary-hover);
            transform: translateY(-1px);
        }
        .tag-readonly {
            color: #94a3b8;
            font-size: 13px;
            font-style: italic;
        }

        .empty-state {
            padding: 50px 20px;
            text-align: center;
            color: var(--text-muted);
            font-size: 14px;
        }
    </style>
</head>
<body>

<main class="container">
    <!-- Top Bar -->
    <header class="top-navbar">
        <div class="clinic-brand">
            🏥 Clinique Santé Plus
        </div>
        <div style="display: flex; align-items: center; gap: 12px;">
            <c:choose>
                <c:when test="${sessionScope.role == 'GENERALISTE'}">
                    <span class="user-tag doctor">👨‍⚕️ Espace Médecin</span>
                </c:when>
                <c:otherwise>
                    <span class="user-tag nurse">👩‍⚕️ Espace Infirmier (Consultation)</span>
                    <a href="${pageContext.request.contextPath}/patients" style="font-size: 13px; color: var(--primary); text-decoration: none; font-weight: 600;">
                        ← Retour aux Admissions
                    </a>
                </c:otherwise>
            </c:choose>
            <a href="${pageContext.request.contextPath}/logout" style="color: #ef4444; font-weight: 600; text-decoration: none; padding: 4px 10px; border-radius: 6px; background: #fef2f2; border: 1px solid #fee2e2; font-size: 13px;">
                🚪 Déconnexion
            </a>
        </div>
    </header>

    <!-- Page Title -->
    <div class="page-header">
        <div>
            <h1>File d'attente des Consultations</h1>
            <p>Patients admis aujourd'hui en attente d'examen médical.</p>
        </div>
    </div>

    <!-- Stats Analysis Grid -->
    <div class="stats-grid">
        <div class="stat-card">
            <div class="label">Total en attente</div>
            <div class="value waiting">
                ${not empty consultations ? consultations.size() : 0}
            </div>
        </div>
        <div class="stat-card">
            <div class="label">Priorité normale</div>
            <div class="value">
                ${not empty consultations ? consultations.size() : 0}
            </div>
        </div>
    </div>

    <!-- Table -->
    <div class="table-card">
        <c:choose>
            <c:when test="${empty consultations}">
                <div class="empty-state">
                    Aucun patient en attente de consultation pour le moment.
                </div>
            </c:when>
            <c:otherwise>
                <table>
                    <thead>
                        <tr>
                            <th>Heure</th>
                            <th>Patient</th>
                            <th>Constantes Vitales</th>
                            <th>Statut</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="consultation" items="${consultations}">
                            <c:set var="p" value="${consultation.patient}" />
                            <tr>
                                <td class="time-col">
                                    <c:out value="${p.heureArrivee}" />
                                </td>
                                <td class="patient-cell">
                                    <div class="name">
                                        <c:out value="${p.prenom}" /> <c:out value="${p.nom}" />
                                    </div>
                                    <div class="ssn">
                                        NSS: <c:out value="${p.numeroSecuriteSociale}" />
                                    </div>
                                </td>
                                <td>
                                    <div class="vitals-wrap">
                                        <span class="vital-chip" title="Tension">🩺 ${p.tensionArterielle}</span>
                                        <span class="vital-chip" title="Fréquence Cardiaque">❤️ ${p.frequenceCardiaque} bpm</span>
                                        <span class="vital-chip" title="Température">🌡️ ${p.temperature}°C</span>
                                        <span class="vital-chip" title="Fréquence Respiratoire">🫁 ${p.frequenceRespiratoire}/min</span>
                                    </div>
                                </td>
                                <td>
                                    <span class="badge badge-waiting">En attente</span>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${sessionScope.role == 'GENERALISTE'}">
                                            <a class="btn-start"
                                               href="${pageContext.request.contextPath}/consultations/nouvelle?patientId=${p.id}">
                                                Démarrer
                                            </a>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="tag-readonly">En attente médecin</span>
                                        </c:otherwise>
                                    </c:choose>
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
