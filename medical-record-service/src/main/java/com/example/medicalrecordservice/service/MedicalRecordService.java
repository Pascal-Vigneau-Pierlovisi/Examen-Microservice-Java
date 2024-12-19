package com.example.medicalrecordservice.service;

import com.example.medicalrecordservice.dto.MedicalRecordDTO;
import com.example.medicalrecordservice.dto.MedicalRecordRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MedicalRecordService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private MedicalRecordDTO mapRowToDTO(ResultSet rs, int rowNum) throws SQLException {
        MedicalRecordDTO dto = new MedicalRecordDTO();
        dto.setId(rs.getLong("id"));
        dto.setPatientId(rs.getLong("patient_id"));
        dto.setPractitionerId(rs.getLong("practitioner_id"));
        dto.setDiagnosis(rs.getString("diagnosis"));
        dto.setTreatment(rs.getString("treatment"));
        dto.setNotes(rs.getString("notes"));
        
        // Gestion sécurisée des timestamps
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            dto.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            dto.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        return dto;
    }

    public List<MedicalRecordDTO> getMedicalRecordsByPatientId(Long patientId) {
        String sql = "SELECT * FROM medical_records WHERE patient_id = ?";
        return jdbcTemplate.query(sql, this::mapRowToDTO, patientId);
    }

    public MedicalRecordDTO createMedicalRecord(MedicalRecordRequestDTO request) {
        String sql = "INSERT INTO medical_records (patient_id, practitioner_id, diagnosis, treatment, notes, created_at, updated_at) VALUES (?, ?, ?, ?, ?, NOW(), NOW())";
        
        jdbcTemplate.update(sql,
            request.getPatientId(),
            request.getPractitionerId(),
            request.getDiagnosis(),
            request.getTreatment(),
            request.getNotes()
        );
        
        Long id = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
        return getMedicalRecordsByPatientId(request.getPatientId()).stream()
            .filter(record -> record.getId().equals(id))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Created record not found"));
    }

    public MedicalRecordDTO updateMedicalRecord(Long patientId, MedicalRecordRequestDTO request) {
        String sql = "UPDATE medical_records SET diagnosis = ?, treatment = ?, notes = ?, practitioner_id = ?, updated_at = NOW() WHERE patient_id = ?";
        
        jdbcTemplate.update(sql,
            request.getDiagnosis(),
            request.getTreatment(),
            request.getNotes(),
            request.getPractitionerId(),
            patientId
        );
        
        return getMedicalRecordsByPatientId(patientId).stream()
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Updated record not found"));
    }

    public void deleteMedicalRecord(Long patientId) {
        String sql = "DELETE FROM medical_records WHERE patient_id = ?";
        jdbcTemplate.update(sql, patientId);
    }
} 