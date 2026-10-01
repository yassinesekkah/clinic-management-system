package ma.youcode.clinic.dao;

import ma.youcode.clinic.model.Consultation;

import java.util.List;
import java.util.Optional;

public interface ConsultationDAO {
    Consultation save(Consultation consultation);
    Optional<Consultation> findById(Long id);
    Optional<Consultation> findByPatientId(Long patientId);
    List<Consultation> findAll();
}
