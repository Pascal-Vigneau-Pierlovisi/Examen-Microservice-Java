package com.example.medicalrecordservice.controller;

import com.example.medicalrecordservice.dto.MedicalRecordDTO;
import com.example.medicalrecordservice.dto.MedicalRecordRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.medicalrecordservice.service.MedicalRecordService;
import java.util.List;

@RestController
@RequestMapping("/medical-records")
public class MedicalRecordController {

    @Autowired
    private MedicalRecordService medicalRecordService;

    @GetMapping("/{patientId}")
    public ResponseEntity<List<MedicalRecordDTO>> getPatientMedicalRecords(@PathVariable Long patientId) {
        return ResponseEntity.ok(medicalRecordService.getMedicalRecordsByPatientId(patientId));
    }

    @PostMapping
    public ResponseEntity<MedicalRecordDTO> createMedicalRecord(@RequestBody MedicalRecordRequestDTO request) {
        return ResponseEntity.ok(medicalRecordService.createMedicalRecord(request));
    }

    @PutMapping("/{patientId}")
    public ResponseEntity<MedicalRecordDTO> updateMedicalRecord(
            @PathVariable Long patientId,
            @RequestBody MedicalRecordRequestDTO request) {
        return ResponseEntity.ok(medicalRecordService.updateMedicalRecord(patientId, request));
    }

    @DeleteMapping("/{patientId}")
    public ResponseEntity<Void> deleteMedicalRecord(@PathVariable Long patientId) {
        medicalRecordService.deleteMedicalRecord(patientId);
        return ResponseEntity.ok().build();
    }
} 