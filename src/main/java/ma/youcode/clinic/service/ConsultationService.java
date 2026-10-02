package ma.youcode.clinic.service;

import ma.youcode.clinic.dao.ConsultationDAO;
import ma.youcode.clinic.dao.PatientDAO;
import ma.youcode.clinic.model.Consultation;
import ma.youcode.clinic.model.Patient;

import java.util.Collections;
import java.util.List;

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
    public List<Patient> getPatientsEnAttenteConsultationDuJour() {
        // TODO (Coéquipier): Filtrer via Stream API les patients du jour avec statut EN_ATTENTE
        return Collections.emptyList();
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
        // TODO (Vous): Valider, créer la consultation (statut TERMINEE, cout 150), sauvegarder et màj patient
        return null;
    }
}
