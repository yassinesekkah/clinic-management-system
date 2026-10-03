package ma.youcode.clinic.dao.jdbc;

import ma.youcode.clinic.dao.PatientDAO;
import ma.youcode.clinic.model.Patient;
import ma.youcode.clinic.model.enums.PatientStatus;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcPatientDAO implements PatientDAO {

    private final DataSource dataSource;

    public JdbcPatientDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Patient save(Patient patient) {
        String sql = """
            INSERT INTO patient (
                nom, prenom, date_naissance, numero_securite_sociale,
                heure_arrivee, tension_arterielle, frequence_cardiaque,
                temperature, frequence_respiratoire, statut
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            RETURNING id, heure_arrivee
        """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, patient.getNom());
            statement.setString(2, patient.getPrenom());
            statement.setObject(3, patient.getDateNaissance());
            statement.setString(4, patient.getNumeroSecuriteSociale());

            LocalDateTime heureArrivee = patient.getHeureArrivee() != null ? patient.getHeureArrivee() : LocalDateTime.now();
            statement.setObject(5, heureArrivee);

            statement.setString(6, patient.getTensionArterielle());
            statement.setInt(7, patient.getFrequenceCardiaque());
            statement.setBigDecimal(8, patient.getTemperature());
            statement.setInt(9, patient.getFrequenceRespiratoire());

            PatientStatus statut = patient.getStatut() != null ? patient.getStatut() : PatientStatus.EN_ATTENTE;
            statement.setString(10, statut.name());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    throw new SQLException("Patient insertion returned no generated values");
                }
                patient.setId(resultSet.getLong("id"));
                patient.setHeureArrivee(resultSet.getObject("heure_arrivee", LocalDateTime.class));
                patient.setStatut(statut);
                return patient;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to save patient", e);
        }
    }

    @Override
    public Optional<Patient> findById(Long id) {
        String sql = """
            SELECT id, nom, prenom, date_naissance, numero_securite_sociale,
                   heure_arrivee, tension_arterielle, frequence_cardiaque,
                   temperature, frequence_respiratoire, statut
            FROM patient
            WHERE id = ?
        """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? Optional.of(mapRow(resultSet)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to find patient by ID: " + id, e);
        }
    }

    @Override
    public Optional<Patient> findByNumeroSecuriteSociale(String numeroSecuriteSociale) {
        String sql = """
            SELECT id, nom, prenom, date_naissance, numero_securite_sociale,
                   heure_arrivee, tension_arterielle, frequence_cardiaque,
                   temperature, frequence_respiratoire, statut
            FROM patient
            WHERE numero_securite_sociale = ?
        """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, numeroSecuriteSociale);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? Optional.of(mapRow(resultSet)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to find patient by SSN: " + numeroSecuriteSociale, e);
        }
    }

    @Override
    public List<Patient> findAll() {
        String sql = """
            SELECT id, nom, prenom, date_naissance, numero_securite_sociale,
                   heure_arrivee, tension_arterielle, frequence_cardiaque,
                   temperature, frequence_respiratoire, statut
            FROM patient
            ORDER BY heure_arrivee ASC
        """;

        List<Patient> patients = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                patients.add(mapRow(resultSet));
            }
            return patients;
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to list all patients", e);
        }
    }

    @Override
    public void updateStatut(Long id, PatientStatus statut) {
        String sql = "UPDATE patient SET statut = ? WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, statut.name());
            statement.setLong(2, id);

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new IllegalStateException("No patient found with ID: " + id + " to update status");
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to update status for patient ID: " + id, e);
        }
    }

    private Patient mapRow(ResultSet resultSet) throws SQLException {
        return new Patient(
                resultSet.getLong("id"),
                resultSet.getString("nom"),
                resultSet.getString("prenom"),
                resultSet.getObject("date_naissance", LocalDate.class),
                resultSet.getString("numero_securite_sociale"),
                resultSet.getObject("heure_arrivee", LocalDateTime.class),
                resultSet.getString("tension_arterielle"),
                resultSet.getInt("frequence_cardiaque"),
                resultSet.getBigDecimal("temperature"),
                resultSet.getInt("frequence_respiratoire"),
                PatientStatus.valueOf(resultSet.getString("statut"))
        );
    }
}


