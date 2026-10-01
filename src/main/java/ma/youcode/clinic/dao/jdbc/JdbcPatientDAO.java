package ma.youcode.clinic.dao.jdbc;

import ma.youcode.clinic.dao.PatientDAO;
import ma.youcode.clinic.model.Patient;
import ma.youcode.clinic.model.enums.PatientStatus;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

public class JdbcPatientDAO implements PatientDAO {

    private final DataSource dataSource;

    public JdbcPatientDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Patient save(Patient patient) {
        return null;
    }

    @Override
    public Optional<Patient> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<Patient> findByNumeroSecuriteSociale(String numeroSecuriteSociale) {
        return Optional.empty();
    }

    @Override
    public List<Patient> findAll() {
        return List.of();
    }

    @Override
    public List<Patient> findPatientsDuJour() {
        return List.of();
    }

    @Override
    public void updateStatut(Long id, PatientStatus statut) {
    }
}
