package ma.youcode.clinic.dao.jdbc;

import ma.youcode.clinic.dao.ConsultationDAO;
import ma.youcode.clinic.model.Consultation;
import ma.youcode.clinic.model.Patient;
import ma.youcode.clinic.model.enums.ConsultationStatus;
import ma.youcode.clinic.model.enums.PatientStatus;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcConsultationDAO implements ConsultationDAO {

    private final DataSource dataSource;

    public JdbcConsultationDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Consultation save(Consultation consultation) {
        String sql = """
                    INSERT INTO consultation (
                        patient_id, medecin_id, date_consultation,
                        motif, observations, diagnostic, traitement,
                        cout, statut
                    ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                    RETURNING id, date_consultation
                """;

        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, consultation.getPatientId());
            statement.setObject(2, consultation.getMedecinId());

            LocalDateTime dateConsultation = consultation.getDateConsultation() != null
                    ? consultation.getDateConsultation()
                    : LocalDateTime.now();
            statement.setObject(3, dateConsultation);

            statement.setString(4, consultation.getMotif());
            statement.setString(5, consultation.getObservations());
            statement.setString(6, consultation.getDiagnostic());
            statement.setString(7, consultation.getTraitement());

            BigDecimal cout = consultation.getCout() != null ? consultation.getCout() : new BigDecimal("150.00");
            statement.setBigDecimal(8, cout);

            ConsultationStatus statut = consultation.getStatut() != null
                    ? consultation.getStatut()
                    : ConsultationStatus.EN_ATTENTE;
            statement.setString(9, statut.name());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    throw new SQLException("Consultation insertion returned no generated values");
                }
                consultation.setId(resultSet.getLong("id"));
                consultation.setDateConsultation(resultSet.getObject("date_consultation", LocalDateTime.class));
                consultation.setStatut(statut);
                consultation.setCout(cout);
                return consultation;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to save consultation", e);
        }
    }

    @Override
    public Optional<Consultation> findById(Long id) {
        String sql = """
                SELECT
                    c.id AS consultation_id,
                    c.patient_id,
                    c.medecin_id,
                    c.date_consultation,
                    c.motif,
                    c.observations,
                    c.diagnostic,
                    c.traitement,
                    c.cout,
                    c.statut AS consultation_statut,
                    p.id AS patient_id_detail,
                    p.nom AS patient_nom,
                    p.prenom AS patient_prenom,
                    p.date_naissance,
                    p.numero_securite_sociale,
                    p.heure_arrivee,
                    p.tension_arterielle,
                    p.frequence_cardiaque,
                    p.temperature,
                    p.frequence_respiratoire,
                    p.statut AS patient_statut
                FROM consultation c
                JOIN patient p ON p.id = c.patient_id
                WHERE c.id = ?
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapResultSetToConsultation(resultSet));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to find consultation with id: " + id, e);
        }
    }

    @Override
    public Optional<Consultation> findByPatientId(Long patientId) {
        String sql = """
                SELECT
                    c.id AS consultation_id,
                    c.patient_id,
                    c.medecin_id,
                    c.date_consultation,
                    c.motif,
                    c.observations,
                    c.diagnostic,
                    c.traitement,
                    c.cout,
                    c.statut AS consultation_statut,
                    p.id AS patient_id_detail,
                    p.nom AS patient_nom,
                    p.prenom AS patient_prenom,
                    p.date_naissance,
                    p.numero_securite_sociale,
                    p.heure_arrivee,
                    p.tension_arterielle,
                    p.frequence_cardiaque,
                    p.temperature,
                    p.frequence_respiratoire,
                    p.statut AS patient_statut
                FROM consultation c
                JOIN patient p ON p.id = c.patient_id
                WHERE c.patient_id = ?
                ORDER BY c.date_consultation DESC
                LIMIT 1
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, patientId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapResultSetToConsultation(resultSet));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to find consultation for patientId: " + patientId, e);
        }
    }

    @Override
    public List<Consultation> findAll() {
        return List.of();
    }

    @Override
    public void update(Consultation consultation) {
        String sql = """
                UPDATE consultation
                SET medecin_id = ?,
                    motif = ?,
                    observations = ?,
                    diagnostic = ?,
                    traitement = ?,
                    cout = ?,
                    statut = ?
                WHERE id = ?
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setObject(1, consultation.getMedecinId());
            statement.setString(2, consultation.getMotif());
            statement.setString(3, consultation.getObservations());
            statement.setString(4, consultation.getDiagnostic());
            statement.setString(5, consultation.getTraitement());

            BigDecimal cout = consultation.getCout() != null ? consultation.getCout() : new BigDecimal("150.00");
            statement.setBigDecimal(6, cout);

            ConsultationStatus statut = consultation.getStatut() != null
                    ? consultation.getStatut()
                    : ConsultationStatus.TERMINEE;
            statement.setString(7, statut.name());

            statement.setLong(8, consultation.getId());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Échec de la mise à jour : consultation introuvable avec l'ID " + consultation.getId());
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to update consultation with id: " + consultation.getId(), e);
        }
    }

    @Override
    public List<Consultation> findByDateAndStatus(LocalDate date, ConsultationStatus status) {

        String sql = """
                SELECT
                    c.id AS consultation_id,
                    c.patient_id,
                    c.medecin_id,
                    c.date_consultation,
                    c.motif,
                    c.observations,
                    c.diagnostic,
                    c.traitement,
                    c.cout,
                    c.statut AS consultation_statut,
                    p.id AS patient_id_detail,
                    p.nom AS patient_nom,
                    p.prenom AS patient_prenom,
                    p.date_naissance,
                    p.numero_securite_sociale,
                    p.heure_arrivee,
                    p.tension_arterielle,
                    p.frequence_cardiaque,
                    p.temperature,
                    p.frequence_respiratoire,
                    p.statut AS patient_statut
                FROM consultation c
                JOIN patient p ON p.id = c.patient_id
                WHERE c.date_consultation >= ?
                  AND c.date_consultation < ?
                  AND c.statut = ?
                ORDER BY p.heure_arrivee ASC
                """;

        List<Consultation> consultations = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setObject(1, date.atStartOfDay());
            statement.setObject(2, date.plusDays(1).atStartOfDay());
            statement.setString(3, status.name());

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    consultations.add(mapResultSetToConsultation(resultSet));
                }
            }

            return consultations;

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Failed to find consultations by date and status",
                    e);
        }
    }

    private Consultation mapResultSetToConsultation(ResultSet resultSet) throws SQLException {
        Consultation consultation = new Consultation(
                resultSet.getLong("consultation_id"),
                resultSet.getLong("patient_id"),
                getNullableLong(resultSet, "medecin_id"),
                resultSet.getObject("date_consultation", LocalDateTime.class),
                resultSet.getString("motif"),
                resultSet.getString("observations"),
                resultSet.getString("diagnostic"),
                resultSet.getString("traitement"),
                resultSet.getBigDecimal("cout"),
                ConsultationStatus.valueOf(resultSet.getString("consultation_statut")));

        Patient patient = new Patient(
                resultSet.getLong("patient_id_detail"),
                resultSet.getString("patient_nom"),
                resultSet.getString("patient_prenom"),
                resultSet.getObject("date_naissance", LocalDate.class),
                resultSet.getString("numero_securite_sociale"),
                resultSet.getObject("heure_arrivee", LocalDateTime.class),
                resultSet.getString("tension_arterielle"),
                resultSet.getInt("frequence_cardiaque"),
                resultSet.getBigDecimal("temperature"),
                resultSet.getInt("frequence_respiratoire"),
                PatientStatus.valueOf(resultSet.getString("patient_statut")));

        consultation.setPatient(patient);
        return consultation;
    }

    private Long getNullableLong(ResultSet resultSet, String column)
            throws SQLException {
        long value = resultSet.getLong(column);
        return resultSet.wasNull() ? null : value;
    }

}
