package ma.youcode.clinic.factory;

import ma.youcode.clinic.service.AuthService;
import ma.youcode.clinic.service.ConsultationService;
import ma.youcode.clinic.service.PatientService;

/**
 * Centralized Factory for Services.
 * Wires DAO interfaces into Services using Constructor Injection.
 */
public final class ServiceFactory {

    private ServiceFactory() {
    }

    public static AuthService getAuthService() {
        return new AuthService(DaoFactory.getUserDAO());
    }

    public static PatientService getPatientService() {
        return new PatientService(DaoFactory.getPatientDAO());
    }

    public static ConsultationService getConsultationService() {
        return new ConsultationService(DaoFactory.getConsultationDAO(), DaoFactory.getPatientDAO());
    }
}
