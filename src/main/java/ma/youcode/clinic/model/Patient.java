package ma.youcode.clinic.model;

import ma.youcode.clinic.model.enums.PatientStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Patient {
    private Long id;
    private String nom;
    private String prenom;
    private LocalDate dateNaissance;
    private String numeroSecuriteSociale;
    private LocalDateTime heureArrivee;

    // Signes vitaux (stockés directement dans Patient comme requis par le brief)
    private String tensionArterielle;
    private int frequenceCardiaque;
    private BigDecimal temperature;
    private int frequenceRespiratoire;

    private PatientStatus statut;

    public Patient() {
        this.statut = PatientStatus.EN_ATTENTE;
        this.heureArrivee = LocalDateTime.now();
    }

    public Patient(Long id, String nom, String prenom, LocalDate dateNaissance, String numeroSecuriteSociale,
                   LocalDateTime heureArrivee, String tensionArterielle, int frequenceCardiaque,
                   BigDecimal temperature, int frequenceRespiratoire, PatientStatus statut) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.numeroSecuriteSociale = numeroSecuriteSociale;
        this.heureArrivee = heureArrivee;
        this.tensionArterielle = tensionArterielle;
        this.frequenceCardiaque = frequenceCardiaque;
        this.temperature = temperature;
        this.frequenceRespiratoire = frequenceRespiratoire;
        this.statut = statut;
    }

    public Patient(String nom, String prenom, LocalDate dateNaissance, String numeroSecuriteSociale,
                   String tensionArterielle, int frequenceCardiaque, BigDecimal temperature,
                   int frequenceRespiratoire) {
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.numeroSecuriteSociale = numeroSecuriteSociale;
        this.heureArrivee = LocalDateTime.now();
        this.tensionArterielle = tensionArterielle;
        this.frequenceCardiaque = frequenceCardiaque;
        this.temperature = temperature;
        this.frequenceRespiratoire = frequenceRespiratoire;
        this.statut = PatientStatus.EN_ATTENTE;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getNumeroSecuriteSociale() {
        return numeroSecuriteSociale;
    }

    public void setNumeroSecuriteSociale(String numeroSecuriteSociale) {
        this.numeroSecuriteSociale = numeroSecuriteSociale;
    }

    public LocalDateTime getHeureArrivee() {
        return heureArrivee;
    }

    public void setHeureArrivee(LocalDateTime heureArrivee) {
        this.heureArrivee = heureArrivee;
    }

    public String getTensionArterielle() {
        return tensionArterielle;
    }

    public void setTensionArterielle(String tensionArterielle) {
        this.tensionArterielle = tensionArterielle;
    }

    public int getFrequenceCardiaque() {
        return frequenceCardiaque;
    }

    public void setFrequenceCardiaque(int frequenceCardiaque) {
        this.frequenceCardiaque = frequenceCardiaque;
    }

    public BigDecimal getTemperature() {
        return temperature;
    }

    public void setTemperature(BigDecimal temperature) {
        this.temperature = temperature;
    }

    public int getFrequenceRespiratoire() {
        return frequenceRespiratoire;
    }

    public void setFrequenceRespiratoire(int frequenceRespiratoire) {
        this.frequenceRespiratoire = frequenceRespiratoire;
    }

    public PatientStatus getStatut() {
        return statut;
    }

    public void setStatut(PatientStatus statut) {
        this.statut = statut;
    }

    public String getNomComplet() {
        return prenom + " " + nom;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", ssn='" + numeroSecuriteSociale + '\'' +
                ", heureArrivee=" + heureArrivee +
                ", statut=" + statut +
                '}';
    }
}
