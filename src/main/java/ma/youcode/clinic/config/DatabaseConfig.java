package ma.youcode.clinic.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public final class DatabaseConfig {

    private static volatile HikariDataSource dataSource;

    private DatabaseConfig() {
    }

    /**
     * Initializes and returns the HikariCP DataSource.
     * Uses double-checked locking to guarantee a single connection pool.
     */
    public static DataSource getDataSource() {
        if (dataSource == null) {
            synchronized (DatabaseConfig.class) {
                if (dataSource == null) {
                    dataSource = initDataSource();
                }
            }
        }
        return dataSource;
    }

    private static HikariDataSource initDataSource() {
        Properties props = loadProperties();

        // 1. Resolve credentials (environment variables take priority over db.properties)
        String url = getEnvOrDefault("DB_URL", props.getProperty("db.url", "jdbc:postgresql://localhost:5432/clinic_db"));
        String user = getEnvOrDefault("DB_USER", props.getProperty("db.user", "postgres"));
        String password = getEnvOrDefault("DB_PASSWORD", props.getProperty("db.password", "postgres"));
        int poolSize = Integer.parseInt(props.getProperty("db.pool.size", "10"));

        // 2. Configure HikariCP connection pool
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("org.postgresql.Driver");
        config.setJdbcUrl(url);
        config.setUsername(user);
        config.setPassword(password);
        config.setMaximumPoolSize(poolSize);
        config.setMinimumIdle(2);
        config.setIdleTimeout(30000);
        config.setConnectionTimeout(20000);
        config.setPoolName("ClinicHikariPool");

        return new HikariDataSource(config);
    }

    private static Properties loadProperties() {
        Properties props = new Properties();
        try (InputStream in = DatabaseConfig.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (in != null) {
                props.load(in);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load db.properties from classpath", e);
        }
        return props;
    }

    private static String getEnvOrDefault(String envVar, String defaultValue) {
        String value = System.getenv(envVar);
        return (value != null && !value.isBlank()) ? value : defaultValue;
    }

    /**
     * Borrows a connection from the pool.
     * Must be used in a try-with-resources statement:
     * try (Connection conn = DatabaseConfig.getConnection()) { ... }
     */
    public static Connection getConnection() throws SQLException {
        return getDataSource().getConnection();
    }

    /**
     * Closes the connection pool and terminates all socket connections.
     * Should be called when the application shuts down.
     */
    public static void shutdown() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}
