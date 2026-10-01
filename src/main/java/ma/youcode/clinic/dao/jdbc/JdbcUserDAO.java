package ma.youcode.clinic.dao.jdbc;

import ma.youcode.clinic.dao.UserDAO;
import ma.youcode.clinic.model.Utilisateur;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

public class JdbcUserDAO implements UserDAO {

    private final DataSource dataSource;

    public JdbcUserDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Optional<Utilisateur> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<Utilisateur> findByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public List<Utilisateur> findAll() {
        return List.of();
    }

    @Override
    public Utilisateur save(Utilisateur utilisateur) {
        return null;
    }
}
