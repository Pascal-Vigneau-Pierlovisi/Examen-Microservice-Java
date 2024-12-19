package com.example.patientservice.controller;

import com.example.patientservice.dto.PatientCreateDTO;
import com.example.patientservice.dto.PatientResponseDTO;
import com.example.patientservice.entity.Patient;
import com.example.patientservice.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/patients")
@Tag(name = "Patients", description = "Endpoints pour gérer les patients")
public class PatientController {

    private final PatientService service;

    public PatientController(PatientService service) {
        this.service = service;
    }

    @Operation(summary = "Liste de tous les patients")
    @GetMapping
    public List<PatientResponseDTO> getAllPatients() {
        return service.getAllPatients().stream().map(this::convertToResponseDTO).collect(Collectors.toList());
    }

    @Operation(summary = "Récupérer un patient par ID")
    @GetMapping("/{id}")
    public PatientResponseDTO getPatientById(@PathVariable Long id) {
        Patient patient = service.getPatientById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        return convertToResponseDTO(patient);
    }

    @Operation(summary = "Créer un nouveau patient")
    @PostMapping
    public PatientResponseDTO createPatient(@RequestBody PatientCreateDTO dto) {
        Patient patient = new Patient();
        patient.setFirstName(dto.getFirstName());
        patient.setLastName(dto.getLastName());
        patient.setEmail(dto.getEmail());
        patient.setPhoneNumber(dto.getPhoneNumber());
        return convertToResponseDTO(service.savePatient(patient));
    }

    @Operation(summary = "Supprimer un patient")
    @DeleteMapping("/{id}")
    public void deletePatient(@PathVariable Long id) {
        service.deletePatient(id);
    }

    // Méthode utilitaire pour convertir un Patient en PatientResponseDTO
    private PatientResponseDTO convertToResponseDTO(Patient patient) {
        PatientResponseDTO dto = new PatientResponseDTO();
        dto.setId(patient.getId());
        dto.setFirstName(patient.getFirstName());
        dto.setLastName(patient.getLastName());
        dto.setEmail(patient.getEmail());
        dto.setPhoneNumber(patient.getPhoneNumber());
        dto.setCreatedAt(patient.getCreatedAt());
        dto.setUpdatedAt(patient.getUpdatedAt());
        return dto;
    }
}
