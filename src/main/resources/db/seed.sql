-- ========================================================
-- Seed Data: Clinic Management System
-- Default Password for all demo accounts: password123
-- BCrypt Hash: $2a$10$.wgDiWFX7LDEKuAK1RdYxOjnYKJBj.diL8h7tQPll5MMRcOcEtCc2
-- ========================================================

-- 1. Demo Users
INSERT INTO utilisateur (nom, prenom, email, mot_de_passe, role)
VALUES 
    ('Alami', 'Fatima', 'infirmier@clinic.ma', '$2a$10$.wgDiWFX7LDEKuAK1RdYxOjnYKJBj.diL8h7tQPll5MMRcOcEtCc2', 'INFIRMIER'),
    ('Bennani', 'Karim', 'docteur@clinic.ma', '$2a$10$.wgDiWFX7LDEKuAK1RdYxOjnYKJBj.diL8h7tQPll5MMRcOcEtCc2', 'GENERALISTE');

-- 2. Demo Patients for testing US2 (list of today's patients) & US3 (waiting patients)
INSERT INTO patient (nom, prenom, date_naissance, numero_securite_sociale, heure_arrivee, tension_arterielle, frequence_cardiaque, temperature, frequence_respiratoire, statut)
VALUES
    ('Idrissi', 'Omar', '1988-04-12', 'SSN-100234', CURRENT_TIMESTAMP - INTERVAL '2 hours', '120/80', 72, 36.8, 16, 'EN_ATTENTE'),
    ('Chraibi', 'Salma', '1995-09-23', 'SSN-200567', CURRENT_TIMESTAMP - INTERVAL '1 hour', '135/85', 84, 38.2, 18, 'EN_ATTENTE'),
    ('Tazi', 'Mehdi', '1976-11-05', 'SSN-300891', CURRENT_TIMESTAMP - INTERVAL '30 minutes', '118/75', 68, 37.0, 15, 'EN_ATTENTE');
