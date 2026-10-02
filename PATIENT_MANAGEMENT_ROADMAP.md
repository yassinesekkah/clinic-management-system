# Roadmap : Module Infirmier (Gestion des Patients - US1 & US2)

**Branche de travail** : `feature/patient-management`  
**Objectif** : Implémenter l'enregistrement des patients avec leurs signes vitaux (US1) et l'affichage de la file d'attente du jour filtrée via la Stream API (US2).

---

## 1. Rappel des Besoins Fonctionnels

### US1 : Enregistrer un patient
- **Champs d'identité** : Nom, prénom, date de naissance, numéro de sécurité sociale (`ssn` unique).
- **Signes vitaux** (stockés directement dans `Patient`) :
  - Tension artérielle (ex: `"120/80"`)
  - Fréquence cardiaque (ex: `75` bpm)
  - Température (ex: `37.2` °C)
  - Fréquence respiratoire (ex: `16` cycles/min)
- **Heure d'arrivée** : Enregistrée automatiquement (`LocalDateTime.now()`).
- **Statut initial** : `EN_ATTENTE` (automatiquement prêt pour consultation).

### US2 : Voir la liste des patients du jour
- **Affichage** : Nom, prénom, numéro de sécurité sociale, heure d'arrivée, signes vitaux, statut.
- **Tri** : Du plus ancien au plus récent (premier arrivé, premier servi).
- **Contrainte technique explicite** : Utiliser la **Stream API** de Java pour filtrer les patients du jour par date d'enregistrement.

---

## 2. Découpage en Tâches Techniques

```
┌─────────────────────────────────────────────────────────────┐
│ Tâche 1 : Couche DAO (JdbcPatientDAO)                       │
│  - Requêtes SQL : save, findAll, findBySSN, findById        │
└──────────────────────────────┬──────────────────────────────┘
                               │
                               ▼
┌─────────────────────────────────────────────────────────────┐
│ Tâche 2 : Couche Métier (PatientService)                    │
│  - Validation des données & unicité SSN                     │
│  - Filtrage & tri via Stream API                            │
└──────────────────────────────┬──────────────────────────────┘
                               │
                               ▼
┌─────────────────────────────────────────────────────────────┐
│ Tâche 3 : Couche Contrôleur (PatientServlet)                │
│  - GET : Affichage formulaire (US1) & Liste du jour (US2)   │
│  - POST : Traitement formulaire & pattern Post-Redirect-Get │
└──────────────────────────────┬──────────────────────────────┘
                               │
                               ▼
┌─────────────────────────────────────────────────────────────┐
│ Tâche 4 : Couche Vues (JSP + JSTL)                          │
│  - patient-form.jsp (Saisie identité & constantes vitales)  │
│  - patient-list.jsp (Tableau moderne des patients du jour)  │
└─────────────────────────────────────────────────────────────┘
```

---

### Tâche 1 : Couche DAO (`JdbcPatientDAO.java`)
**Fichier** : `src/main/java/ma/youcode/clinic/dao/jdbc/JdbcPatientDAO.java`

* [x] **`save(Patient patient)`** :
  - `INSERT INTO patient (nom, prenom, date_naissance, numero_securite_sociale, heure_arrivee, tension_arterielle, frequence_cardiaque, temperature, frequence_respiratoire, statut) VALUES (...) RETURNING id;`
  - Récupérer l'ID généré et l'assigner à l'objet.
* [x] **`findByNumeroSecuriteSociale(String ssn)`** :
  - Permet de vérifier si le patient existe déjà avant insertion.
* [x] **`findAll()`** :
  - `SELECT * FROM patient ORDER BY heure_arrivee ASC;`
* [x] **`findById(Long id)`** :
  - Utilisé par le médecin pour consulter les détails d'un patient.
* [x] **`updateStatut(Long id, PatientStatus statut)`** :
  - Met à jour le statut du patient (par ex. vers `TERMINEE` après consultation).

---

### Tâche 2 : Couche Métier (`PatientService.java`)
**Fichier** : `src/main/java/ma/youcode/clinic/service/PatientService.java`

* [x] **`enregistrerPatient(Patient patient)` / `addPatient(Patient patient)`** :
  - Vérifier que les champs obligatoires sont non vides.
  - Vérifier l'unicité du numéro de sécurité sociale (`findByNumeroSecuriteSociale`).
  - Définir l'heure d'arrivée si absente (`LocalDateTime.now()`).
  - Définir le statut par défaut : `PatientStatus.EN_ATTENTE`.
  - Appeler `patientDAO.save(patient)`.
* [x] **`getPatientsDuJour()`** (Exigence technique Stream API) :
  - Récupérer `patientDAO.findAll()`.
  - Appliquer le flux :
    ```java
    LocalDate today = LocalDate.now();
    return patientDAO.findAll().stream()
        .filter(p -> p.getHeureArrivee() != null && p.getHeureArrivee().toLocalDate().isEqual(today))
        .sorted(Comparator.comparing(Patient::getHeureArrivee))
        .collect(Collectors.toList());
    ```

---

### Tâche 3 : Couche Contrôleur (`PatientServlet.java`)
**Fichier** : `src/main/java/ma/youcode/clinic/servlet/PatientServlet.java`

* [x] **Méthode `doGet(HttpServletRequest req, HttpServletResponse resp)`** :
  - Si l'action demandée est `/patients/nouveau` : forwarder vers le formulaire `patient-form.jsp`.
  - Sinon (par défaut `/patients`) :
    1. Récupérer `patientService.getPatientsDuJour()`.
    2. Placer la liste dans le scope requête : `req.setAttribute("patients", patients)`.
    3. Forwarder vers `patient-list.jsp`.
* [x] **Méthode `doPost(HttpServletRequest req, HttpServletResponse resp)`** :
  - Récupérer et parser les paramètres du formulaire (`nom`, `prenom`, `dateNaissance`, `ssn`, constantes vitales).
  - Gérer les erreurs de saisie (chiffres invalides, dates mal formatées).
  - Si erreur : ré-afficher le formulaire avec message d'erreur et données saisies.
  - Si succès : appeler `patientService.enregistrerPatient(patient)`.
  - Appliquer le pattern **Post-Redirect-Get** : `resp.sendRedirect(req.getContextPath() + "/patients?success=true");`.

---

### Tâche 4 : Couche Présentation (`JSP` + `JSTL`)

* [x] **`src/main/webapp/WEB-INF/views/nurse/patient-form.jsp`** :
  - Formulaire intuitif divisé en 2 sections :
    1. **Identité** (Nom, Prénom, Date de Naissance, N° Sécurité Sociale).
    2. **Signes Vitaux** (Tension Artérielle, Fréquence Cardiaque bpm, Température °C, Fréquence Respiratoire).
  - Affichage des messages d'erreur de validation.
* [x] **`src/main/webapp/WEB-INF/views/nurse/patient-list.jsp`** :
  - Tableau moderne listant la file d'attente du jour.
  - Badge de statut (`EN_ATTENTE` en orange/bleu).
  - Colonnes claires : N° SSN, Nom complet, Heure d'arrivée, Tension, Pouls, Température.
  - Bouton d'action pour enregistrer un nouveau patient.


---

## 3. Plan d'Action Recommandé (Pas à Pas)

1. **Étape 1** : Écrire et tester les requêtes SQL dans `JdbcPatientDAO.java`.
2. **Étape 2** : Implémenter la logique et la Stream API dans `PatientService.java`.
3. **Étape 3** : Créer le `PatientServlet.java` pour relier le service aux vues.
4. **Étape 4** : Créer les pages JSP (`patient-form.jsp` et `patient-list.jsp`) avec un design soigné.
5. **Étape 5** : Tester sur Tomcat (enregistrer un patient $\rightarrow$ vérifier la liste du jour).
