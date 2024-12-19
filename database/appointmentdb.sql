USE appointmentdb;

CREATE TABLE IF NOT EXISTS appointments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_id BIGINT NOT NULL,
    practitioner_id BIGINT NOT NULL,
    appointment_date DATETIME NOT NULL,
    status ENUM('Scheduled', 'Cancelled', 'Completed') DEFAULT 'Scheduled',
    google_event_id VARCHAR(255) DEFAULT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
