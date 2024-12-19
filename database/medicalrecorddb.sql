CREATE DATABASE medicalrecorddb;

USE medicalrecorddb;

CREATE TABLE medical_records (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_id BIGINT NOT NULL,
    practitioner_id BIGINT NOT NULL,
    diagnosis TEXT,
    treatment TEXT,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (patient_id) REFERENCES patientdb.patients(id),
    FOREIGN KEY (practitioner_id) REFERENCES practitionerdatabase.practitioners(id)
);
