-- ========================================================
-- Schema: Clinic Management System (Tele-expertise Medicale)
-- Target Database: PostgreSQL 14+
-- ========================================================

DROP TABLE IF EXISTS consultation CASCADE;
DROP TABLE IF EXISTS patient CASCADE;
DROP TABLE IF EXISTS utilisateur CASCADE;

-- 1. Table Utilisateur (Users & Roles)
CREATE TABLE utilisateur (
    id BIGSERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    mot_de_passe VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL CHECK (role IN ('INFIRMIER', 'GENERALISTE')),
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 2. Table Patient (Patients & Vital Signs)
-- Note: Vital signs are stored directly inside the Patient entity as required by the brief.
CREATE TABLE patient (
    id BIGSERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    date_naissance DATE NOT NULL,
    numero_securite_sociale VARCHAR(50) UNIQUE NOT NULL,
    heure_arrivee TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    tension_arterielle VARCHAR(10) NOT NULL,      -- e.g. "120/80" or "12/8"
    frequence_cardiaque INT NOT NULL,              -- bpm (e.g. 75)
    temperature NUMERIC(4, 1) NOT NULL,            -- Celsius (e.g. 37.2)
    frequence_respiratoire INT NOT NULL,           -- breaths/min (e.g. 16)
    statut VARCHAR(20) NOT NULL DEFAULT 'EN_ATTENTE' CHECK (statut IN ('EN_ATTENTE', 'TERMINEE'))
);

-- 3. Table Consultation
CREATE TABLE consultation (
    id BIGSERIAL PRIMARY KEY,
    patient_id BIGINT NOT NULL REFERENCES patient(id) ON DELETE CASCADE,
    medecin_id BIGINT REFERENCES utilisateur(id) ON DELETE RESTRICT,
    date_consultation TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    motif TEXT,
    observations TEXT,
    diagnostic TEXT,
    traitement TEXT,
    cout NUMERIC(8, 2) NOT NULL DEFAULT 150.00,
    statut VARCHAR(20) NOT NULL DEFAULT 'EN_ATTENTE' CHECK (statut IN ('EN_ATTENTE', 'TERMINEE'))
);

-- Indexes for performance
CREATE INDEX idx_patient_heure_arrivee ON patient(heure_arrivee);
CREATE INDEX idx_patient_statut ON patient(statut);
CREATE INDEX idx_patient_ssn ON patient(numero_securite_sociale);
CREATE INDEX idx_consultation_patient ON consultation(patient_id);
CREATE INDEX idx_consultation_medecin ON consultation(medecin_id);
