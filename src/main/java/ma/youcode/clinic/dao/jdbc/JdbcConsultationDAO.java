package ma.youcode.clinic.dao.jdbc;

import ma.youcode.clinic.dao.ConsultationDAO;
import ma.youcode.clinic.model.Consultation;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

public class JdbcConsultationDAO implements ConsultationDAO {

    private final DataSource dataSource;

    public JdbcConsultationDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Consultation save(Consultation consultation) {
        return null;
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
}
