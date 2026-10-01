package ma.youcode.clinic.service;

import ma.youcode.clinic.dao.PatientDAO;

public class PatientService {

    private final PatientDAO patientDAO;

    // Constructor Injection: Depends strictly on PatientDAO interface
    public PatientService(PatientDAO patientDAO) {
        this.patientDAO = patientDAO;
    }

}
