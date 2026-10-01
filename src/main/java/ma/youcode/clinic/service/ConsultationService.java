package ma.youcode.clinic.service;

import ma.youcode.clinic.dao.ConsultationDAO;
import ma.youcode.clinic.dao.PatientDAO;

public class ConsultationService {

    private final ConsultationDAO consultationDAO;
    private final PatientDAO patientDAO;

    // Constructor Injection: Depends strictly on DAO interfaces
    public ConsultationService(ConsultationDAO consultationDAO, PatientDAO patientDAO) {
        this.consultationDAO = consultationDAO;
        this.patientDAO = patientDAO;
    }

}
