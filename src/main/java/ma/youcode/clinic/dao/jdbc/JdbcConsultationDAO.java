package ma.youcode.clinic.dao.jdbc;

import ma.youcode.clinic.dao.ConsultationDAO;
import ma.youcode.clinic.model.Consultation;
import ma.youcode.clinic.model.enums.ConsultationStatus;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
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
        return Optional.empty();
    }

    @Override
    public Optional<Consultation> findByPatientId(Long patientId) {
        return Optional.empty();
    }

    @Override
    public List<Consultation> findAll() {
        return List.of();
    }

    @Override
    public void update(Consultation consultation) {
        // Will be implemented for doctor examination and closure
    }
}
