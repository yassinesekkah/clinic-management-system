package ma.youcode.clinic.listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import ma.youcode.clinic.config.DatabaseConfig;
import ma.youcode.clinic.config.DatabaseInitializer;

import java.util.logging.Logger;

@WebListener
public class AppContextListener implements ServletContextListener {

    private static final Logger LOGGER = Logger.getLogger(AppContextListener.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        LOGGER.info("Starting Clinic Management System...");
        try {
            // 1. Pre-warm connection pool
            DatabaseConfig.getDataSource();
            LOGGER.info("HikariCP connection pool successfully initialized.");

            // 2. Automatically execute schema and seed SQL if database is empty
            DatabaseInitializer.initialize(DatabaseConfig.getDataSource());
        } catch (Exception e) {
            LOGGER.severe("Failed to initialize database on startup: " + e.getMessage());
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
