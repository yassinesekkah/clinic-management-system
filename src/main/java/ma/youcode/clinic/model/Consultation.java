package ma.youcode.clinic.model;

import ma.youcode.clinic.model.enums.ConsultationStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Consultation {
    private Long id;
    private Long patientId;
    private Long medecinId;

    // Optional navigation objects (useful for views & JPA migration)
    private Patient patient;
    private Utilisateur medecin;

    private LocalDateTime dateConsultation;
    private String motif;
    private String observations;
    private String diagnostic;
    private String traitement;
    private BigDecimal cout;
    private ConsultationStatus statut;

    public Consultation() {
        this.dateConsultation = LocalDateTime.now();
        this.cout = new BigDecimal("150.00");
        this.statut = ConsultationStatus.TERMINEE;
    }

    public Consultation(Long id, Long patientId, Long medecinId, LocalDateTime dateConsultation,
                        String motif, String observations, String diagnostic, String traitement,
                        BigDecimal cout, ConsultationStatus statut) {
        this.id = id;
        this.patientId = patientId;
        this.medecinId = medecinId;
        this.dateConsultation = dateConsultation;
        this.motif = motif;
        this.observations = observations;
        this.diagnostic = diagnostic;
        this.traitement = traitement;
        this.cout = cout;
        this.statut = statut;
    }

    public Consultation(Long patientId, Long medecinId, String motif, String observations,
                        String diagnostic, String traitement) {
        this();
        this.patientId = patientId;
        this.medecinId = medecinId;
        this.motif = motif;
        this.observations = observations;
        this.diagnostic = diagnostic;
        this.traitement = traitement;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public Long getMedecinId() {
        return medecinId;
    }

    public void setMedecinId(Long medecinId) {
        this.medecinId = medecinId;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
        if (patient != null) {
            this.patientId = patient.getId();
        }
    }

    public Utilisateur getMedecin() {
        return medecin;
    }

    public void setMedecin(Utilisateur medecin) {
        this.medecin = medecin;
        if (medecin != null) {
            this.medecinId = medecin.getId();
        }
    }

    public LocalDateTime getDateConsultation() {
        return dateConsultation;
    }

    public void setDateConsultation(LocalDateTime dateConsultation) {
        this.dateConsultation = dateConsultation;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public String getDiagnostic() {
        return diagnostic;
    }

    public void setDiagnostic(String diagnostic) {
        this.diagnostic = diagnostic;
    }

    public String getTraitement() {
        return traitement;
    }

    public void setTraitement(String traitement) {
        this.traitement = traitement;
    }

    public BigDecimal getCout() {
        return cout;
    }

    public void setCout(BigDecimal cout) {
        this.cout = cout;
    }

    public ConsultationStatus getStatut() {
        return statut;
    }

    public void setStatut(ConsultationStatus statut) {
        this.statut = statut;
    }

    @Override
    public String toString() {
        return "Consultation{" +
                "id=" + id +
                ", patientId=" + patientId +
                ", medecinId=" + medecinId +
                ", dateConsultation=" + dateConsultation +
                ", cout=" + cout +
                ", statut=" + statut +
                '}';
    }
}
