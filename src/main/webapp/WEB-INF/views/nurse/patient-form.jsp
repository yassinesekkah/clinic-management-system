<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admission Patient - Signes Vitaux</title>
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
            --border-focus: #3b82f6;
            --danger-bg: #fef2f2;
            --danger-border: #fecaca;
            --danger-text: #991b1b;
            --success-bg: #ecfdf5;
            --success-text: #065f46;
            --radius-md: 10px;
            --radius-lg: 16px;
            --shadow-sm: 0 1px 3px rgba(0, 0, 0, 0.05);
            --shadow-md: 0 10px 25px -5px rgba(15, 23, 42, 0.08);
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
            padding: 40px 20px;
        }

        .container {
            max-width: 800px;
            margin: 0 auto;
        }

        .header-bar {
            display: flex;
            align-items: center;
            justify-content: space-between;
            margin-bottom: 24px;
        }

        .back-link {
            display: inline-flex;
            align-items: center;
            gap: 8px;
            color: var(--text-muted);
            text-decoration: none;
            font-size: 14px;
            font-weight: 500;
            padding: 8px 14px;
            border-radius: var(--radius-md);
            background: var(--surface);
            border: 1px solid var(--border);
            transition: all 0.2s ease;
        }

        .back-link:hover {
            color: var(--text-main);
            border-color: var(--border-focus);
            background: var(--primary-light);
        }

        .card {
            background: var(--surface);
            border-radius: var(--radius-lg);
            border: 1px solid var(--border);
            box-shadow: var(--shadow-md);
            overflow: hidden;
        }

        .card-header {
            padding: 28px 32px;
            border-bottom: 1px solid var(--border);
            background: linear-gradient(to right, #ffffff, #f8fafc);
        }

        .card-header h1 {
            font-size: 22px;
            font-weight: 700;
            color: var(--text-main);
            letter-spacing: -0.02em;
        }

        .card-header p {
            margin-top: 6px;
            font-size: 14px;
            color: var(--text-muted);
        }

        .card-body {
            padding: 32px;
        }

        .alert-danger {
            background-color: var(--danger-bg);
            border: 1px solid var(--danger-border);
            color: var(--danger-text);
            border-radius: var(--radius-md);
            padding: 16px 20px;
            margin-bottom: 28px;
        }

        .alert-danger h3 {
            font-size: 14px;
            font-weight: 600;
            margin-bottom: 8px;
        }

        .alert-danger ul {
            margin-left: 20px;
            font-size: 13.5px;
            line-height: 1.5;
        }

        .form-section {
            margin-bottom: 32px;
        }

        .section-title {
            display: flex;
            align-items: center;
            gap: 10px;
            font-size: 15px;
            font-weight: 600;
            color: var(--primary);
            text-transform: uppercase;
            letter-spacing: 0.05em;
            margin-bottom: 20px;
            padding-bottom: 8px;
            border-bottom: 2px solid var(--primary-light);
        }

        .grid-2 {
            display: grid;
            grid-template-columns: repeat(2, 1fr);
            gap: 20px;
        }

        @media (max-width: 640px) {
            .grid-2 {
                grid-template-columns: 1fr;
            }
        }

        .form-group {
            display: flex;
            flex-direction: column;
            gap: 6px;
        }

        label {
            font-size: 13.5px;
            font-weight: 600;
            color: var(--text-main);
        }

        .hint {
            font-size: 12px;
            color: var(--text-muted);
        }

        input[type="text"],
        input[type="date"],
        input[type="number"] {
            width: 100%;
            height: 44px;
            padding: 0 14px;
            border-radius: var(--radius-md);
            border: 1px solid var(--border);
            background: #fff;
            color: var(--text-main);
            font-size: 14px;
            font-family: inherit;
            transition: all 0.15s ease;
        }

        input:focus {
            outline: none;
            border-color: var(--border-focus);
            box-shadow: 0 0 0 3px rgba(37, 99, 235, 0.15);
        }

        .form-actions {
            display: flex;
            align-items: center;
            justify-content: flex-end;
            gap: 14px;
            margin-top: 36px;
            padding-top: 24px;
            border-top: 1px solid var(--border);
        }

        .btn {
            display: inline-flex;
            align-items: center;
            justify-content: center;
            height: 44px;
            padding: 0 24px;
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
        }

        .btn-secondary {
            background-color: transparent;
            color: var(--text-muted);
            border: 1px solid var(--border);
        }

        .btn-secondary:hover {
            background-color: var(--primary-light);
            color: var(--text-main);
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header-bar">
            <a href="${pageContext.request.contextPath}/patients" class="back-link">
                &larr; Retour à la file d'attente
            </a>
            <span style="font-size: 13px; color: var(--text-muted);">Espace Infirmier / Admission</span>
        </div>

        <div class="card">
            <div class="card-header">
                <h1>Enregistrer un nouveau patient</h1>
                <p>Saisissez les coordonnées d'identité du patient ainsi que ses constantes vitales d'admission.</p>
            </div>

            <div class="card-body">
                <c:if test="${not empty errors}">
                    <div class="alert-danger" role="alert">
                        <h3>Veuillez corriger les erreurs suivantes :</h3>
                        <ul>
                            <c:forEach var="err" items="${errors}">
                                <li><c:out value="${err}" /></li>
                            </c:forEach>
                        </ul>
                    </div>
                </c:if>

                <form action="${pageContext.request.contextPath}/patients" method="POST" autocomplete="off">
                    <!-- 1. IDENTITE -->
                    <div class="form-section">
                        <div class="section-title">
                            1. Identité du patient
                        </div>
                        <div class="grid-2">
                            <div class="form-group">
                                <label for="nom">Nom <span style="color: #ef4444;">*</span></label>
                                <input type="text" id="nom" name="nom" required 
                                       value="<c:out value='${patient.nom}' />" placeholder="ex: Benali">
                            </div>

                            <div class="form-group">
                                <label for="prenom">Prénom <span style="color: #ef4444;">*</span></label>
                                <input type="text" id="prenom" name="prenom" required 
                                       value="<c:out value='${patient.prenom}' />" placeholder="ex: Fatima">
                            </div>

                            <div class="form-group">
                                <label for="dateNaissance">Date de naissance <span style="color: #ef4444;">*</span></label>
                                <input type="date" id="dateNaissance" name="dateNaissance" required 
                                       value="${patient.dateNaissance}">
                            </div>

                            <div class="form-group">
                                <label for="numeroSecuriteSociale">N° Sécurité Sociale (Unique) <span style="color: #ef4444;">*</span></label>
                                <input type="text" id="numeroSecuriteSociale" name="numeroSecuriteSociale" required 
                                       value="<c:out value='${patient.numeroSecuriteSociale}' />" placeholder="ex: SSN-2024-0012">
                            </div>
                        </div>
                    </div>

                    <!-- 2. CONSTANTES VITALES -->
                    <div class="form-section">
                        <div class="section-title">
                            2. Constantes vitales initiales
                        </div>
                        <div class="grid-2">
                            <div class="form-group">
                                <label for="tensionArterielle">Tension artérielle <span style="color: #ef4444;">*</span></label>
                                <input type="text" id="tensionArterielle" name="tensionArterielle" required 
                                       placeholder="ex: 120/80" value="<c:out value='${patient.tensionArterielle}' />">
                                <span class="hint">Format attendu : mmHg (ex: 120/80 ou 13/8)</span>
                            </div>

                            <div class="form-group">
                                <label for="frequenceCardiaque">Fréquence cardiaque (bpm) <span style="color: #ef4444;">*</span></label>
                                <input type="number" id="frequenceCardiaque" name="frequenceCardiaque" required 
                                       min="30" max="250" placeholder="ex: 75" value="${patient.frequenceCardiaque != 0 ? patient.frequenceCardiaque : ''}">
                                <span class="hint">Battements par minute (normale : 60-100)</span>
                            </div>

                            <div class="form-group">
                                <label for="temperature">Température corporelle (°C) <span style="color: #ef4444;">*</span></label>
                                <input type="number" id="temperature" name="temperature" required step="0.1" 
                                       min="30.0" max="45.0" placeholder="ex: 37.2" value="${patient.temperature}">
                                <span class="hint">En degrés Celsius (ex: 37.2)</span>
                            </div>

                            <div class="form-group">
                                <label for="frequenceRespiratoire">Fréquence respiratoire <span style="color: #ef4444;">*</span></label>
                                <input type="number" id="frequenceRespiratoire" name="frequenceRespiratoire" required 
                                       min="5" max="60" placeholder="ex: 16" value="${patient.frequenceRespiratoire != 0 ? patient.frequenceRespiratoire : ''}">
                                <span class="hint">Cycles respiratoires par minute</span>
                            </div>
                        </div>
                    </div>

                    <div class="form-actions">
                        <a href="${pageContext.request.contextPath}/patients" class="btn btn-secondary">Annuler</a>
                        <button type="submit" id="btn-submit" class="btn btn-primary">Valider et mettre en attente</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</body>
</html>
