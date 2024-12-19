package com.example.gatewayservice.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;

@RestController
@RequestMapping("/medical-records")
public class MedicalRecordProxyController {
    
    private static final Logger logger = LoggerFactory.getLogger(MedicalRecordProxyController.class);
    private final WebClient.Builder webClientBuilder;
    
    public MedicalRecordProxyController(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }
    
    @GetMapping("/{patientId}")
    public Mono<Object> getMedicalRecordsByPatientId(@PathVariable Long patientId) {
        return webClientBuilder.build()
            .get()
            .uri("http://medical-record-service/medical-records/{patientId}", patientId)
            .retrieve()
            .bodyToMono(Object.class)
            .doOnError(error -> logger.error("Error getting medical records for patient {}: ", patientId, error))
            .onErrorResume(error -> Mono.just("Dossier médical temporairement indisponible pour le patient " + patientId));
    }
    
    @PostMapping
    public Mono<Object> createMedicalRecord(@RequestBody Object medicalRecord) {
        return webClientBuilder.build()
            .post()
            .uri("http://medical-record-service/medical-records")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(medicalRecord)
            .retrieve()
            .bodyToMono(Object.class)
            .doOnError(error -> logger.error("Error creating medical record: ", error))
            .onErrorResume(error -> Mono.just("Erreur lors de la création du dossier médical"));
    }
    
    @PutMapping("/{patientId}")
    public Mono<Object> updateMedicalRecord(@PathVariable Long patientId, @RequestBody Object medicalRecord) {
        return webClientBuilder.build()
            .put()
            .uri("http://medical-record-service/medical-records/{patientId}", patientId)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(medicalRecord)
            .retrieve()
            .bodyToMono(Object.class)
            .doOnError(error -> logger.error("Error updating medical record for patient {}: ", patientId, error))
            .onErrorResume(error -> Mono.just("Erreur lors de la mise à jour du dossier médical du patient " + patientId));
    }
    
    @DeleteMapping("/{patientId}")
    public Mono<Object> deleteMedicalRecord(@PathVariable Long patientId) {
        return webClientBuilder.build()
            .delete()
            .uri("http://medical-record-service/medical-records/{patientId}", patientId)
            .retrieve()
            .bodyToMono(Object.class)
            .doOnError(error -> logger.error("Error deleting medical record for patient {}: ", patientId, error))
            .onErrorResume(error -> Mono.just("Erreur lors de la suppression du dossier médical du patient " + patientId));
    }
} 