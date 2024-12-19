package com.example.appointmentservice.controller;

import com.example.appointmentservice.dto.AppointmentRequest;
import com.example.appointmentservice.model.Appointment;
import com.example.appointmentservice.service.AppointmentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@Tag(name = "Appointment Controller", description = "API de gestion des rendez-vous")
@Slf4j
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping
    @Operation(summary = "Créer un rendez-vous")
    @ApiResponse(responseCode = "200", description = "Rendez-vous créé avec succès")
    public ResponseEntity<Appointment> createAppointment(@RequestBody AppointmentRequest request) {
        log.info("Création d'un rendez-vous pour le patient {}", request.getPatientId());
        return ResponseEntity.ok(appointmentService.createAppointment(request));
    }

    @GetMapping
    @Operation(summary = "Récupérer tous les rendez-vous")
    @ApiResponse(responseCode = "200", description = "Liste des rendez-vous récupérée avec succès")
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        log.info("Récupération de tous les rendez-vous");
        return ResponseEntity.ok(appointmentService.getAllAppointments());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un rendez-vous par son ID")
    @ApiResponse(responseCode = "200", description = "Rendez-vous trouvé")
    @ApiResponse(responseCode = "404", description = "Rendez-vous non trouvé")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable Long id) {
        log.info("Récupération du rendez-vous {}", id);
        return appointmentService.getAppointmentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un rendez-vous")
    @ApiResponse(responseCode = "204", description = "Rendez-vous supprimé avec succès")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        log.info("Suppression du rendez-vous {}", id);
        appointmentService.deleteAppointment(id);
        return ResponseEntity.noContent().build();
    }
} 