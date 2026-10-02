package ma.youcode.clinic.service;

import ma.youcode.clinic.dao.PatientDAO;
import ma.youcode.clinic.exception.ValidationException;
import ma.youcode.clinic.model.Patient;
import ma.youcode.clinic.model.enums.PatientStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PatientService {

    private static final Pattern TENSION_PATTERN = Pattern.compile("^\\d{2,3}/\\d{2,3}$");

    private final PatientDAO patientDAO;

    // Constructor Injection: Depends strictly on PatientDAO interface
    public PatientService(PatientDAO patientDAO) {
        this.patientDAO = patientDAO;
    }

    /**
     * Enregistre un nouveau patient après validation complète des champs d'identité,
     * des constantes vitales, et vérification de l'unicité du numéro de sécurité sociale.
     *
     * @param patient l'objet patient à enregistrer
     * @return le patient persisté avec son identifiant généré
     * @throws ValidationException si des données sont manquantes ou invalides
     */
    public Patient addPatient(Patient patient) {
        validate(patient);

        // Nettoyage et application des valeurs par défaut
        patient.setNom(patient.getNom().trim());
        patient.setPrenom(patient.getPrenom().trim());
        patient.setNumeroSecuriteSociale(patient.getNumeroSecuriteSociale().trim());

        if (patient.getHeureArrivee() == null) {
            patient.setHeureArrivee(LocalDateTime.now());
        }

        if (patient.getStatut() == null) {
            patient.setStatut(PatientStatus.EN_ATTENTE);
        }

        return patientDAO.save(patient);
    }

    /**
     * Récupère la liste ordonnée des patients enregistrés aujourd'hui en attente de consultation.
     * Utilise la Stream API pour filtrer par date du jour et trier par ordre d'arrivée chronologique.
     */
    public List<Patient> getPatientsDuJour() {
        LocalDate today = LocalDate.now();

        return patientDAO.findAll().stream()
                .filter(p -> p.getHeureArrivee() != null && p.getHeureArrivee().toLocalDate().isEqual(today))
                .sorted(Comparator.comparing(Patient::getHeureArrivee))
                .collect(Collectors.toList());
    }

    public Optional<Patient> getPatientById(Long id) {
        return patientDAO.findById(id);
    }

    public List<Patient> getAllPatients() {
        return patientDAO.findAll();
    }

    /**
     * Recherche des patients par nom, prénom, numéro de sécurité sociale ou ID via la Stream API.
     */
    public List<Patient> searchPatients(String query) {
        if (query == null || query.isBlank()) {
            return List.of();
        }
        String cleanQuery = query.trim().toLowerCase();
        return patientDAO.findAll().stream()
                .filter(p -> (p.getNom() != null && p.getNom().toLowerCase().contains(cleanQuery))
                        || (p.getPrenom() != null && p.getPrenom().toLowerCase().contains(cleanQuery))
                        || (p.getNumeroSecuriteSociale() != null && p.getNumeroSecuriteSociale().toLowerCase().contains(cleanQuery))
                        || String.valueOf(p.getId()).equals(cleanQuery))
                .collect(Collectors.toList());
    }

    public void updateStatut(Long id, PatientStatus statut) {
        patientDAO.updateStatut(id, statut);
    }

    /**
     * Valide l'ensemble des règles métier et accumule les erreurs
     * pour fournir un retour complet à l'utilisateur.
     */
    private void validate(Patient patient) {
        List<String> errors = new ArrayList<>();

        if (patient == null) {
            throw new ValidationException("Les informations du patient sont obligatoires.");
        }

        // 1. Validation de l'identité
        if (patient.getNom() == null || patient.getNom().isBlank()) {
            errors.add("Le nom est obligatoire.");
        }

        if (patient.getPrenom() == null || patient.getPrenom().isBlank()) {
            errors.add("Le prénom est obligatoire.");
        }

        if (patient.getDateNaissance() == null) {
            errors.add("La date de naissance est obligatoire.");
        } else if (patient.getDateNaissance().isAfter(LocalDate.now())) {
            errors.add("La date de naissance ne peut pas être dans le futur.");
        }

        // 2. Validation et unicité du numéro de sécurité sociale
        if (patient.getNumeroSecuriteSociale() == null || patient.getNumeroSecuriteSociale().isBlank()) {
            errors.add("Le numéro de sécurité sociale est obligatoire.");
        } else {
            String ssn = patient.getNumeroSecuriteSociale().trim();
            Optional<Patient> existing = patientDAO.findByNumeroSecuriteSociale(ssn);
            if (existing.isPresent()) {
                errors.add("Un patient avec le numéro de sécurité sociale '" + ssn + "' est déjà enregistré.");
            }
        }

        // 3. Validation des signes vitaux (constantes physiologiques)
        if (patient.getTensionArterielle() == null || patient.getTensionArterielle().isBlank()) {
            errors.add("La tension artérielle est obligatoire.");
        } else if (!TENSION_PATTERN.matcher(patient.getTensionArterielle().trim()).matches()) {
            errors.add("La tension artérielle doit respecter le format valide (ex: '120/80' ou '12/8').");
        }

        if (patient.getFrequenceCardiaque() < 30 || patient.getFrequenceCardiaque() > 250) {
            errors.add("La fréquence cardiaque doit être comprise entre 30 et 250 bpm.");
        }

        if (patient.getTemperature() == null) {
            errors.add("La température corporelle est obligatoire.");
        } else if (patient.getTemperature().compareTo(BigDecimal.valueOf(30.0)) < 0
                || patient.getTemperature().compareTo(BigDecimal.valueOf(45.0)) > 0) {
            errors.add("La température corporelle doit être comprise entre 30.0°C et 45.0°C.");
        }

        if (patient.getFrequenceRespiratoire() < 5 || patient.getFrequenceRespiratoire() > 60) {
            errors.add("La fréquence respiratoire doit être comprise entre 5 et 60 cycles/min.");
        }

        // Si des erreurs ont été détectées, lever l'exception avec la liste complète
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
}

