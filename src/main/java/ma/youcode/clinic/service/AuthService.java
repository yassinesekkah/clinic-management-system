package ma.youcode.clinic.service;

import ma.youcode.clinic.dao.UserDAO;
import ma.youcode.clinic.model.Utilisateur;
import ma.youcode.clinic.util.PasswordUtil;

import java.util.Optional;

public class AuthService {

    private final UserDAO userDAO;

    public AuthService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }
    public Optional<Utilisateur> login(String email, String password) {
        // 1. Verifier que les champs sont remplis.
        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            return Optional.empty();
        }

        // 2. Chercher l'utilisateur dans la base de donnees.
        Optional<Utilisateur> resultat = userDAO.findByEmail(email.trim());
        if (resultat.isEmpty()) {
            return Optional.empty();
        }

        // 3. Comparer le mot de passe saisi avec le hash enregistre.
        Utilisateur utilisateur = resultat.get();
        boolean passwordCorrect = PasswordUtil.verify(password, utilisateur.getMotDePasse());
        if (!passwordCorrect) {
            return Optional.empty();
        }

        // 4. Retourner l'utilisateur si les identifiants sont corrects.
        return Optional.of(utilisateur);
    }
}
