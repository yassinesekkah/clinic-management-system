package ma.youcode.clinic.factory;

import ma.youcode.clinic.config.DatabaseConfig;
import ma.youcode.clinic.dao.ConsultationDAO;
import ma.youcode.clinic.dao.PatientDAO;
import ma.youcode.clinic.dao.UserDAO;
import ma.youcode.clinic.dao.jdbc.JdbcConsultationDAO;
import ma.youcode.clinic.dao.jdbc.JdbcPatientDAO;
import ma.youcode.clinic.dao.jdbc.JdbcUserDAO;

import javax.sql.DataSource;


public final class DaoFactory {

    private DaoFactory() {
    }

    private static DataSource getDataSource() {
        return DatabaseConfig.getDataSource();
    }

    public static UserDAO getUserDAO() {
        return new JdbcUserDAO(getDataSource());
    }

    public static PatientDAO getPatientDAO() {
        return new JdbcPatientDAO(getDataSource());
    }

    public static ConsultationDAO getConsultationDAO() {
        return new JdbcConsultationDAO(getDataSource());
    }
}
