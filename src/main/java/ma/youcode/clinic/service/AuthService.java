package ma.youcode.clinic.service;

import ma.youcode.clinic.dao.UserDAO;

public class AuthService {

    private final UserDAO userDAO;

    // Constructor Injection: Depends strictly on UserDAO interface
    public AuthService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }
    
}
