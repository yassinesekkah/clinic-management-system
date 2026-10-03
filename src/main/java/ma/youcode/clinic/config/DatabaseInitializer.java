package ma.youcode.clinic.config;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

/**
 * Automates execution of database schema.sql and seed.sql at application startup.
 * Includes idempotency check so restarting the server never overwrites existing data.
 */
public final class DatabaseInitializer {

    private static final Logger LOGGER = Logger.getLogger(DatabaseInitializer.class.getName());

    private DatabaseInitializer() {
    }

    public static void initialize(DataSource dataSource) {
        try (Connection conn = dataSource.getConnection()) {
            if (isDatabaseInitialized(conn)) {
                LOGGER.info("Database tables already exist. Skipping schema and seed script execution.");
                return;
            }

            LOGGER.info("Empty database detected. Initializing schema and seed data...");

            executeSqlScript(conn, "/db/schema.sql");
            LOGGER.info("schema.sql executed successfully.");

            executeSqlScript(conn, "/db/seed.sql");
            LOGGER.info("seed.sql executed successfully. Demo accounts and patients loaded.");

        } catch (SQLException | IOException e) {
            throw new IllegalStateException("Failed to initialize database schema", e);
        }
    }

    private static boolean isDatabaseInitialized(Connection conn) throws SQLException {
        DatabaseMetaData meta = conn.getMetaData();
        // Check for 'utilisateur' table (PostgreSQL stores unquoted table names in lowercase)
        try (ResultSet rs = meta.getTables(null, null, "utilisateur", new String[]{"TABLE"})) {
            if (rs.next()) {
                return true;
            }
        }
        // Fallback check for uppercase (some drivers return uppercase table names)
        try (ResultSet rs = meta.getTables(null, null, "UTILISATEUR", new String[]{"TABLE"})) {
            return rs.next();
        }
    }

    private static void executeSqlScript(Connection conn, String resourcePath) throws IOException, SQLException {
        try (InputStream in = DatabaseInitializer.class.getResourceAsStream(resourcePath)) {
            if (in == null) {
                throw new IllegalArgumentException("SQL script not found on classpath: " + resourcePath);
            }

            String sql = new String(in.readAllBytes(), StandardCharsets.UTF_8);
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(sql);
            }
        }
    }
}
