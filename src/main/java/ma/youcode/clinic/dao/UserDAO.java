package ma.youcode.clinic.dao;

import ma.youcode.clinic.model.Utilisateur;

import java.util.List;
import java.util.Optional;

public interface UserDAO {
    Optional<Utilisateur> findById(Long id);
    Optional<Utilisateur> findByEmail(String email);
    List<Utilisateur> findAll();
    Utilisateur save(Utilisateur utilisateur);
}
