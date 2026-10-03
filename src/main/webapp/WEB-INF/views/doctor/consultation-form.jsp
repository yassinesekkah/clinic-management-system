<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Examen Médical - Dossier Patient #${patient.id}</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
    <style>
        :root {
            --primary: #2563eb;
            --primary-hover: #1d4ed8;
            --primary-light: #eff6ff;
            --surface: #ffffff;
            --bg-page: #f8fafc;
            --text-main: #0f172a;
            --text-muted: #64748b;
            --border: #e2e8f0;
            --border-focus: #3b82f6;
            --success: #16a34a;
            --success-light: #f0fdf4;
            --success-border: #bbf7d0;
            --danger: #dc2626;
            --danger-light: #fef2f2;
            --danger-border: #fecaca;
            --radius-md: 10px;
            --radius-lg: 16px;
            --shadow-sm: 0 1px 3px rgba(0, 0, 0, 0.05);
            --shadow-md: 0 10px 25px -5px rgba(15, 23, 42, 0.07);
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
            padding: 30px 24px;
        }

        .container {
            max-width: 1200px;
            margin: 0 auto;
        }

        /* Top Navbar */
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

        .user-nav-actions {
            display: flex;
            align-items: center;
            gap: 12px;
        }

        .user-tag {
            font-size: 13px;
            padding: 4px 12px;
            border-radius: 999px;
            font-weight: 600;
            background: #eff6ff;
            color: #1d4ed8;
            border: 1px solid #bfdbfe;
        }

        .btn-logout {
            color: var(--danger);
            font-weight: 600;
            text-decoration: none;
            padding: 5px 12px;
            border-radius: 8px;
            background: var(--danger-light);
            border: 1px solid var(--danger-border);
            font-size: 13px;
            transition: all 0.15s ease;
        }

        .btn-logout:hover {
            background: #fee2e2;
        }

        /* Page Header */
        .header-bar {
            display: flex;
            align-items: center;
            justify-content: space-between;
            margin-bottom: 24px;
            flex-wrap: wrap;
            gap: 12px;
        }

        .back-link {
            display: inline-flex;
            align-items: center;
            gap: 6px;
            font-size: 14px;
            font-weight: 600;
            color: var(--primary);
            text-decoration: none;
            transition: transform 0.15s ease;
        }

        .back-link:hover {
            transform: translateX(-2px);
        }

        .page-badge {
            font-size: 12px;
            font-weight: 700;
            text-transform: uppercase;
            letter-spacing: 0.5px;
            padding: 4px 10px;
            border-radius: 6px;
            background: #f1f5f9;
            color: #475569;
        }

        /* 2-Column Clinical Layout */
        .clinical-grid {
            display: grid;
            grid-template-columns: 380px 1fr;
            gap: 24px;
            align-items: start;
        }

        @media (max-width: 960px) {
            .clinical-grid {
                grid-template-columns: 1fr;
            }
        }

        /* Card System */
        .card {
            background: var(--surface);
            border: 1px solid var(--border);
            border-radius: var(--radius-lg);
            box-shadow: var(--shadow-sm);
            overflow: hidden;
        }

        .card-header {
            padding: 20px 24px;
            border-bottom: 1px solid var(--border);
            background: #ffffff;
        }

        .card-header h2 {
            font-size: 17px;
            font-weight: 700;
            color: var(--text-main);
            display: flex;
            align-items: center;
            gap: 8px;
        }

        .card-header p {
            font-size: 13px;
            color: var(--text-muted);
            margin-top: 4px;
        }

        .card-body {
            padding: 24px;
        }

        /* Patient Overview (Left Column) */
        .patient-avatar-box {
            display: flex;
            align-items: center;
            gap: 16px;
            padding-bottom: 20px;
            border-bottom: 1px solid var(--border);
            margin-bottom: 20px;
        }

        .avatar-circle {
            width: 54px;
            height: 54px;
            border-radius: 50%;
            background: #dbeafe;
            color: #1e40af;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 20px;
            font-weight: 700;
            flex-shrink: 0;
        }

        .patient-name-title {
            font-size: 18px;
            font-weight: 700;
            color: var(--text-main);
        }

        .patient-ssn-sub {
            font-size: 13px;
            color: var(--text-muted);
            font-family: monospace;
            margin-top: 2px;
        }

        .info-row {
            display: flex;
            justify-content: space-between;
            padding: 10px 0;
            border-bottom: 1px dashed #f1f5f9;
            font-size: 13px;
        }

        .info-label {
            color: var(--text-muted);
        }

        .info-value {
            font-weight: 600;
            color: var(--text-main);
        }

        /* Vitals Section */
        .vitals-header {
            font-size: 14px;
            font-weight: 700;
            color: var(--text-main);
            margin: 24px 0 12px;
            display: flex;
            align-items: center;
            gap: 6px;
        }

        .vitals-grid {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 10px;
        }

        .vital-box {
            background: #f8fafc;
            border: 1px solid #e2e8f0;
            border-radius: var(--radius-md);
            padding: 12px;
            display: flex;
            flex-direction: column;
            gap: 4px;
        }

        .vital-box-label {
            font-size: 11px;
            font-weight: 600;
            text-transform: uppercase;
            color: var(--text-muted);
            letter-spacing: 0.3px;
        }

        .vital-box-value {
            font-size: 16px;
            font-weight: 700;
            color: #1e293b;
        }

        /* Examination Form (Right Column) */
        .form-group {
            margin-bottom: 20px;
        }

        .form-group label {
            display: block;
            font-size: 13px;
            font-weight: 600;
            color: var(--text-main);
            margin-bottom: 8px;
        }

        .form-group .helper-text {
            font-size: 12px;
            color: var(--text-muted);
            margin-top: 4px;
        }

        .input-text, textarea {
            width: 100%;
            padding: 12px 14px;
            font-family: inherit;
            font-size: 14px;
            color: var(--text-main);
            background: #ffffff;
            border: 1px solid var(--border);
            border-radius: var(--radius-md);
            transition: all 0.2s ease;
            resize: vertical;
        }

        .input-text:focus, textarea:focus {
            outline: none;
            border-color: var(--border-focus);
            box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.15);
        }

        /* Tarification Banner */
        .tariff-banner {
            display: flex;
            align-items: center;
            justify-content: space-between;
            padding: 14px 18px;
            background: var(--success-light);
            border: 1px solid var(--success-border);
            border-radius: var(--radius-md);
            margin-top: 10px;
            margin-bottom: 24px;
        }

        .tariff-info {
            display: flex;
            align-items: center;
            gap: 10px;
        }

        .tariff-info-text {
            font-size: 13px;
            color: #166534;
        }

        .tariff-info-text strong {
            font-weight: 700;
        }

        .tariff-badge {
            background: var(--success);
            color: #ffffff;
            font-size: 14px;
            font-weight: 700;
            padding: 6px 14px;
            border-radius: 999px;
            letter-spacing: 0.3px;
        }

        /* Action Buttons */
        .form-actions {
            display: flex;
            align-items: center;
            justify-content: flex-end;
            gap: 12px;
            padding-top: 20px;
            border-top: 1px solid var(--border);
        }

        .btn {
            display: inline-flex;
            align-items: center;
            justify-content: center;
            gap: 8px;
            padding: 12px 22px;
            font-size: 14px;
            font-weight: 600;
            border-radius: var(--radius-md);
            cursor: pointer;
            text-decoration: none;
            transition: all 0.15s ease;
            border: none;
        }

        .btn-cancel {
            background: #ffffff;
            color: var(--text-muted);
            border: 1px solid var(--border);
        }

        .btn-cancel:hover {
            background: #f1f5f9;
            color: var(--text-main);
        }

        .btn-submit {
            background: var(--primary);
            color: #ffffff;
            box-shadow: 0 2px 4px rgba(37, 99, 235, 0.2);
        }

        .btn-submit:hover {
            background: var(--primary-hover);
            transform: translateY(-1px);
        }

        .alert-error {
            padding: 14px 18px;
            background: var(--danger-light);
            border: 1px solid var(--danger-border);
            border-radius: var(--radius-md);
            color: var(--danger);
            font-size: 13px;
            margin-bottom: 20px;
        }
    </style>
</head>
<body>

<div class="container">
    <!-- Top Navigation Bar -->
    <header class="top-navbar">
        <div class="clinic-brand">
            🏥 Clinique Santé Plus
        </div>
        <div class="user-nav-actions">
            <span class="user-tag">👨‍⚕️ Espace Médecin</span>
            <a href="${pageContext.request.contextPath}/logout" class="btn-logout">
                🚪 Déconnexion
            </a>
        </div>
    </header>

    <!-- Page Header Navigation -->
    <div class="header-bar">
        <a href="${pageContext.request.contextPath}/consultations" class="back-link">
            ← Retour à la file d'attente
        </a>
        <span class="page-badge">Examen Clinique & Clôture</span>
    </div>

    <!-- Error Alert if any -->
    <c:if test="${not empty errors}">
        <div class="alert-error" role="alert">
            <c:forEach var="err" items="${errors}">
                <p>⚠️ <c:out value="${err}" /></p>
            </c:forEach>
        </div>
    </c:if>

    <!-- 2-Column Clinical Dashboard Layout -->
    <div class="clinical-grid">

        <!-- ========================================================= -->
        <!-- LEFT COLUMN: Dossier Patient & Signes Vitaux d'Admission -->
        <!-- ========================================================= -->
        <aside class="card">
            <div class="card-header">
                <h2>📋 Dossier Patient</h2>
                <p>Constantes d'admission enregistrées par l'infirmier</p>
            </div>
            <div class="card-body">
                <!-- Avatar & Identity -->
                <div class="patient-avatar-box">
                    <div class="avatar-circle">
                        ${patient.prenom.substring(0, 1)}${patient.nom.substring(0, 1)}
                    </div>
                    <div>
                        <div class="patient-name-title">
                            <c:out value="${patient.prenom}" /> <c:out value="${patient.nom}" />
                        </div>
                        <div class="patient-ssn-sub">
                            NSS : <c:out value="${patient.numeroSecuriteSociale}" />
                        </div>
                    </div>
                </div>

                <!-- Identity Details -->
                <div class="info-row">
                    <span class="info-label">Identifiant Patient</span>
                    <span class="info-value">#<c:out value="${patient.id}" /></span>
                </div>
                <div class="info-row">
                    <span class="info-label">Date de naissance</span>
                    <span class="info-value"><c:out value="${patient.dateNaissance}" /></span>
                </div>
                <div class="info-row">
                    <span class="info-label">Heure d'admission</span>
                    <span class="info-value"><c:out value="${patient.heureArrivee}" /></span>
                </div>

                <!-- Vital Signs Section -->
                <div class="vitals-header">
                    🩺 Signes Vitaux d'Admission
                </div>
                <div class="vitals-grid">
                    <div class="vital-box">
                        <span class="vital-box-label">Tension artérielle</span>
                        <span class="vital-box-value">${patient.tensionArterielle}</span>
                    </div>
                    <div class="vital-box">
                        <span class="vital-box-label">Fréquence cardiaque</span>
                        <span class="vital-box-value">${patient.frequenceCardiaque} bpm</span>
                    </div>
                    <div class="vital-box">
                        <span class="vital-box-label">Température</span>
                        <span class="vital-box-value">${patient.temperature} °C</span>
                    </div>
                    <div class="vital-box">
                        <span class="vital-box-label">Fréq. respiratoire</span>
                        <span class="vital-box-value">${patient.frequenceRespiratoire} /min</span>
                    </div>
                </div>
            </div>
        </aside>

        <!-- ========================================================= -->
        <!-- RIGHT COLUMN: Formulaire d'Examen et Clôture Médicale     -->
        <!-- ========================================================= -->
        <main class="card">
            <div class="card-header">
                <h2>🩺 Examen Médical & Prescription</h2>
                <p>Renseignez les observations, diagnostic et ordonnance pour clôturer la consultation</p>
            </div>
            <div class="card-body">
                <form action="${pageContext.request.contextPath}/consultations" method="POST" autocomplete="off">
                    <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
                    <input type="hidden" name="patientId" value="${patient.id}">

                    <!-- 1. Motif -->
                    <div class="form-group">
                        <label for="motif">Motif de la consultation <span style="color: var(--danger);">*</span></label>
                        <input type="text" id="motif" name="motif" class="input-text" required
                               placeholder="ex: Céphalées persistantes, toux sèche, contrôle de routine..."
                               value="<c:out value='${param.motif}' />">
                        <div class="helper-text">Raison principale de la visite exprimée par le patient.</div>
                    </div>

                    <!-- 2. Observations Cliniques -->
                    <div class="form-group">
                        <label for="observations">Observations cliniques</label>
                        <textarea id="observations" name="observations" rows="3"
                                  placeholder="Observations à l'auscultation, palpation, antécédents pertinents..."><c:out value="${param.observations}" /></textarea>
                        <div class="helper-text">Résultats de l'examen physique effectué en cabinet.</div>
                    </div>

                    <!-- 3. Diagnostic Médical -->
                    <div class="form-group">
                        <label for="diagnostic">Diagnostic médical <span style="color: var(--danger);">*</span></label>
                        <textarea id="diagnostic" name="diagnostic" rows="3" required
                                  placeholder="ex: Rhinopharyngite aiguë d'origine virale sans complication..."><c:out value="${param.diagnostic}" /></textarea>
                        <div class="helper-text">Conclusion médicale du praticien.</div>
                    </div>

                    <!-- 4. Traitement & Ordonnance -->
                    <div class="form-group">
                        <label for="traitement">Prescription & Traitement <span style="color: var(--danger);">*</span></label>
                        <textarea id="traitement" name="traitement" rows="4" required
                                  placeholder="ex: Paracétamol 1g (1 cp 3x/jour si douleur/fièvre pendant 5j)&#10;Spray nasal sérum physiologique..."><c:out value="${param.traitement}" /></textarea>
                        <div class="helper-text">Médicaments prescrits, posologie, recommandations et repos.</div>
                    </div>

                    <!-- 5. Tarification fixe (150 DH) -->
                    <div class="tariff-banner">
                        <div class="tariff-info">
                            <span style="font-size: 20px;">💳</span>
                            <div class="tariff-info-text">
                                <strong>Tarif de consultation standard</strong>
                                <div>Montant forfaitaire réglementaire appliqué au dossier.</div>
                            </div>
                        </div>
                        <div class="tariff-badge">
                            150.00 DH
                        </div>
                    </div>

                    <!-- Buttons -->
                    <div class="form-actions">
                        <a href="${pageContext.request.contextPath}/consultations" class="btn btn-cancel">
                            Annuler
                        </a>
                        <button type="submit" class="btn btn-submit">
                            💾 Enregistrer et clôturer la consultation
                        </button>
                    </div>
                </form>
            </div>
        </main>

    </div>
</div>

</body>
</html>
