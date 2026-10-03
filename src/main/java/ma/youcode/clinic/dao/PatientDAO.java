package ma.youcode.clinic.dao;

import ma.youcode.clinic.model.Patient;
import ma.youcode.clinic.model.enums.PatientStatus;

import java.util.List;
import java.util.Optional;

public interface PatientDAO {
    Patient save(Patient patient);
    Optional<Patient> findById(Long id);
    Optional<Patient> findByNumeroSecuriteSociale(String numeroSecuriteSociale);
    List<Patient> findAll();
    void updateStatut(Long id, PatientStatus statut);
}
