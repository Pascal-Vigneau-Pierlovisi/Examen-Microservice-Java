package com.example.gatewayservice.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;

@RestController
@RequestMapping("/patients")
public class PatientProxyController {
    
    private static final Logger logger = LoggerFactory.getLogger(PatientProxyController.class);
    private final WebClient.Builder webClientBuilder;
    
    public PatientProxyController(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }
    
    @GetMapping
    public Mono<Object> getAllPatients() {
        return webClientBuilder.build()
            .get()
            .uri("http://patient-service/patients")
            .retrieve()
            .bodyToMono(Object.class)
            .doOnError(error -> logger.error("Error calling patient service: ", error))
            .onErrorResume(error -> Mono.just("Service patient temporairement indisponible"));
    }
    
    @GetMapping("/{id}")
    public Mono<Object> getPatientById(@PathVariable Long id) {
        return webClientBuilder.build()
            .get()
            .uri("http://patient-service/patients/{id}", id)
            .retrieve()
            .bodyToMono(Object.class)
            .doOnError(error -> logger.error("Error calling patient service for id {}: ", id, error))
            .onErrorResume(error -> Mono.just("Patient " + id + " temporairement indisponible"));
    }
    
    @PostMapping
    public Mono<Object> createPatient(@RequestBody Object patient) {
        return webClientBuilder.build()
            .post()
            .uri("http://patient-service/patients")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(patient)
            .retrieve()
            .bodyToMono(Object.class)
            .doOnError(error -> logger.error("Error creating patient: ", error))
            .onErrorResume(error -> Mono.just("Erreur lors de la création du patient"));
    }
    
    @PutMapping("/{id}")
    public Mono<Object> updatePatient(@PathVariable Long id, @RequestBody Object patient) {
        return webClientBuilder.build()
            .put()
            .uri("http://patient-service/patients/{id}", id)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(patient)
            .retrieve()
            .bodyToMono(Object.class)
            .doOnError(error -> logger.error("Error updating patient {}: ", id, error))
            .onErrorResume(error -> Mono.just("Erreur lors de la mise à jour du patient " + id));
    }
    
    @DeleteMapping("/{id}")
    public Mono<Object> deletePatient(@PathVariable Long id) {
        return webClientBuilder.build()
            .delete()
            .uri("http://patient-service/patients/{id}", id)
            .retrieve()
            .bodyToMono(Object.class)
            .doOnError(error -> logger.error("Error deleting patient {}: ", id, error))
            .onErrorResume(error -> Mono.just("Erreur lors de la suppression du patient " + id));
    }
} 