package ma.youcode.clinic.service;

import ma.youcode.clinic.dao.ConsultationDAO;
import ma.youcode.clinic.dao.PatientDAO;
import ma.youcode.clinic.model.Consultation;
import ma.youcode.clinic.model.enums.ConsultationStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ConsultationService {

    private final ConsultationDAO consultationDAO;
    private final PatientDAO patientDAO;

    // Constructor Injection: Depends strictly on DAO interfaces
    public ConsultationService(ConsultationDAO consultationDAO, PatientDAO patientDAO) {
        this.consultationDAO = consultationDAO;
        this.patientDAO = patientDAO;
    }

    /**
     * Crée une consultation en attente pour un patient donné (utilisé lors de l'admission par l'infirmier).
     */
    public Consultation creerConsultationEnAttente(Long patientId) {
        if (patientId == null) {
            throw new IllegalArgumentException("L'identifiant du patient ne peut pas être null.");
        }
        Consultation consultation = new Consultation(patientId);
        Consultation saved = consultationDAO.save(consultation);
        patientDAO.updateStatut(patientId, ma.youcode.clinic.model.enums.PatientStatus.EN_ATTENTE);
        return saved;
    }

    // =========================================================================
    // TRANCHE 2 (COÉQUIPIER) : File d'attente du jour pour le médecin
    // =========================================================================

    /**
     * Récupère la liste des patients admis aujourd'hui n'ayant pas encore de consultation.
     */
    public List<Consultation> getConsultationsEnAttenteDuJour() {
        return consultationDAO.findByDateAndStatus(
            LocalDate.now(),
            ConsultationStatus.EN_ATTENTE);
    }

    public Set<Long> getPatientIdsWithConsultationEnAttenteDuJour() {
        return getConsultationsEnAttenteDuJour().stream()
                .map(Consultation::getPatientId)
                .collect(Collectors.toSet());
    }

    public Set<Long> getPatientIdsWithConsultationTermineeDuJour() {
        return consultationDAO.findByDateAndStatus(
            LocalDate.now(),
            ConsultationStatus.TERMINEE).stream()
                .map(Consultation::getPatientId)
                .collect(Collectors.toSet());
    }

    // =========================================================================
    // TRANCHE 1 (VOUS) : Clôture et enregistrement de la consultation
    // =========================================================================

    /**
     * Valide et enregistre la consultation terminée (150 DH fixe) et met à jour le patient.
     */
    public Consultation cloturerConsultation(Long patientId, Long medecinId,
                                             String motif, String observations,
                                             String diagnostic, String traitement) {
        // 1. Validation des champs obligatoires
        List<String> errors = new java.util.ArrayList<>();
        if (patientId == null) {
            errors.add("L'identifiant du patient est obligatoire.");
        }
        if (medecinId == null) {
            errors.add("L'identifiant du médecin est obligatoire.");
        }
        if (motif == null || motif.isBlank()) {
            errors.add("Le motif de la consultation est obligatoire.");
        }
        if (diagnostic == null || diagnostic.isBlank()) {
            errors.add("Le diagnostic médical est obligatoire.");
        }
        if (traitement == null || traitement.isBlank()) {
            errors.add("La prescription / traitement est obligatoire.");
        }

        if (!errors.isEmpty()) {
            throw new ma.youcode.clinic.exception.ValidationException(errors);
        }

        // 2. Vérifier si une consultation en attente existe déjà pour ce patient
        java.util.Optional<Consultation> existingOpt = consultationDAO.findByPatientId(patientId);
        Consultation consultation;

        if (existingOpt.isPresent() && existingOpt.get().getStatut() == ConsultationStatus.EN_ATTENTE) {
            // Mettre à jour la consultation existante créée lors de l'admission
            consultation = existingOpt.get();
            consultation.setMedecinId(medecinId);
            consultation.setMotif(motif.trim());
            consultation.setObservations(observations != null ? observations.trim() : "");
            consultation.setDiagnostic(diagnostic.trim());
            consultation.setTraitement(traitement.trim());
            consultation.setCout(new java.math.BigDecimal("150.00"));
            consultation.setStatut(ConsultationStatus.TERMINEE);
            consultationDAO.update(consultation);
        } else {
            // Créer une nouvelle consultation directement finalisée
            consultation = new Consultation(
                    patientId,
                    medecinId,
                    motif.trim(),
                    observations != null ? observations.trim() : "",
                    diagnostic.trim(),
                    traitement.trim()
            );
            consultation.setCout(new java.math.BigDecimal("150.00"));
            consultation.setStatut(ConsultationStatus.TERMINEE);
            consultation = consultationDAO.save(consultation);
        }

        // 3. Mettre à jour le statut du patient en base vers TERMINEE
        patientDAO.updateStatut(patientId, ma.youcode.clinic.model.enums.PatientStatus.TERMINEE);

        return consultation;
    }
}
