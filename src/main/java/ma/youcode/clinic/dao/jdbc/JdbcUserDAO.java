package ma.youcode.clinic.dao.jdbc;

import ma.youcode.clinic.dao.UserDAO;
import ma.youcode.clinic.model.Utilisateur;
import ma.youcode.clinic.model.enums.Role;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcUserDAO implements UserDAO {

    private final DataSource dataSource;

    public JdbcUserDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Optional<Utilisateur> findById(Long id) {
        String sql = "SELECT id, nom, prenom, email, mot_de_passe, role, created_at FROM utilisateur WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? Optional.of(mapRow(resultSet)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to find user by ID", e);
        }
    }

    @Override
    public Optional<Utilisateur> findByEmail(String email) {
        String sql = "SELECT id, nom, prenom, email, mot_de_passe, role, created_at FROM utilisateur WHERE email = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? Optional.of(mapRow(resultSet)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to find user by email", e);
        }
    }

    @Override
    public List<Utilisateur> findAll() {
        String sql = "SELECT id, nom, prenom, email, mot_de_passe, role, created_at FROM utilisateur ORDER BY id";
        List<Utilisateur> utilisateurs = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                utilisateurs.add(mapRow(resultSet));
            }
            return utilisateurs;
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to list users", e);
        }
    }

    /** Inserts a new user. The password must already be hashed before calling this method. */
    @Override
    public Utilisateur save(Utilisateur utilisateur) {
        String sql = "INSERT INTO utilisateur (nom, prenom, email, mot_de_passe, role) VALUES (?, ?, ?, ?, ?) RETURNING id, created_at";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, utilisateur.getNom());
            statement.setString(2, utilisateur.getPrenom());
            statement.setString(3, utilisateur.getEmail());
            statement.setString(4, utilisateur.getMotDePasse());
            statement.setString(5, utilisateur.getRole().name());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    throw new SQLException("User insertion returned no generated values");
                }
                utilisateur.setId(resultSet.getLong("id"));
                Timestamp createdAt = resultSet.getTimestamp("created_at");
                utilisateur.setCreatedAt(createdAt == null ? null : createdAt.toLocalDateTime());
                return utilisateur;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to save user", e);
        }
    }

    private Utilisateur mapRow(ResultSet resultSet) throws SQLException {
        Timestamp createdAt = resultSet.getTimestamp("created_at");
        return new Utilisateur(
                resultSet.getLong("id"),
                resultSet.getString("nom"),
                resultSet.getString("prenom"),
                resultSet.getString("email"),
                resultSet.getString("mot_de_passe"),
                Role.valueOf(resultSet.getString("role")),
                createdAt == null ? null : createdAt.toLocalDateTime()
        );
    }
}
