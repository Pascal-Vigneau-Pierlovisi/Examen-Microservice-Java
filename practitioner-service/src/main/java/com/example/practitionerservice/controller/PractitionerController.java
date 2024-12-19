package com.example.practitionerservice.controller;

import com.example.practitionerservice.dto.PractitionerCreateDTO;
import com.example.practitionerservice.dto.PractitionerResponseDTO;
import com.example.practitionerservice.entity.Practitioner;
import com.example.practitionerservice.service.PractitionerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/practitioners")
@Tag(name = "Practitioners", description = "Endpoints pour gérer les praticiens")
public class PractitionerController {

    private final PractitionerService service;

    public PractitionerController(PractitionerService service) {
        this.service = service;
    }

    @Operation(summary = "Liste de tous les praticiens")
    @GetMapping
    public List<PractitionerResponseDTO> getAllPractitioners() {
        return service.getAllPractitioners().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Récupérer un praticien par ID")
    @GetMapping("/{id}")
    public PractitionerResponseDTO getPractitionerById(@PathVariable Long id) {
        Practitioner practitioner = service.getPractitionerById(id)
                .orElseThrow(() -> new RuntimeException("Practitioner not found"));
        return convertToResponseDTO(practitioner);
    }

    @Operation(summary = "Créer un nouveau praticien")
    @PostMapping
    public PractitionerResponseDTO createPractitioner(@RequestBody PractitionerCreateDTO dto) {
        Practitioner practitioner = new Practitioner();
        practitioner.setFirstName(dto.getFirstName());
        practitioner.setLastName(dto.getLastName());
        practitioner.setSpecialization(dto.getSpecialization());
        practitioner.setEmail(dto.getEmail());
        practitioner.setPhoneNumber(dto.getPhoneNumber());
        return convertToResponseDTO(service.savePractitioner(practitioner));
    }

    @Operation(summary = "Supprimer un praticien")
    @DeleteMapping("/{id}")
    public void deletePractitioner(@PathVariable Long id) {
        service.deletePractitioner(id);
    }

    // Méthode utilitaire pour convertir un Practitioner en PractitionerResponseDTO
    private PractitionerResponseDTO convertToResponseDTO(Practitioner practitioner) {
        PractitionerResponseDTO dto = new PractitionerResponseDTO();
        dto.setId(practitioner.getId());
        dto.setFirstName(practitioner.getFirstName());
        dto.setLastName(practitioner.getLastName());
        dto.setSpecialization(practitioner.getSpecialization());
        dto.setEmail(practitioner.getEmail());
        dto.setPhoneNumber(practitioner.getPhoneNumber());
        dto.setCreatedAt(practitioner.getCreatedAt());
        dto.setUpdatedAt(practitioner.getUpdatedAt());
        return dto;
    }
}
