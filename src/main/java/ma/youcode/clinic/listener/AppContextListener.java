package ma.youcode.clinic.listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import ma.youcode.clinic.config.DatabaseConfig;

import java.util.logging.Logger;

@WebListener
public class AppContextListener implements ServletContextListener {

    private static final Logger LOGGER = Logger.getLogger(AppContextListener.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        LOGGER.info("Starting Clinic Management System...");
        // Eagerly pre-warm the HikariCP connection pool on application boot
        try {
            DatabaseConfig.getDataSource();
            LOGGER.info("HikariCP connection pool successfully initialized.");
        } catch (Exception e) {
            LOGGER.severe("Failed to initialize database connection pool on startup: " + e.getMessage());
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        LOGGER.info("Shutting down Clinic Management System...");
        // Gracefully terminate HikariCP background threads and close all database connections
        DatabaseConfig.shutdown();
        LOGGER.info("HikariCP connection pool shut down cleanly.");
    }
}
