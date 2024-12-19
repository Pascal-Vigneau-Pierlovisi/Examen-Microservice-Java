package com.example.gatewayservice.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;

@RestController
@RequestMapping("/practitioners")
public class PractitionerProxyController {
    
    private static final Logger logger = LoggerFactory.getLogger(PractitionerProxyController.class);
    private final WebClient.Builder webClientBuilder;
    
    public PractitionerProxyController(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }
    
    @GetMapping
    public Mono<Object> getAllPractitioners() {
        return webClientBuilder.build()
            .get()
            .uri("http://practitioner-service/practitioners")
            .retrieve()
            .bodyToMono(Object.class)
            .doOnError(error -> logger.error("Error calling practitioner service: ", error))
            .onErrorResume(error -> Mono.just("Service practitioner temporairement indisponible"));
    }
    
    @GetMapping("/{id}")
    public Mono<Object> getPractitionerById(@PathVariable Long id) {
        return webClientBuilder.build()
            .get()
            .uri("http://practitioner-service/practitioners/{id}", id)
            .retrieve()
            .bodyToMono(Object.class)
            .doOnError(error -> logger.error("Error calling practitioner service for id {}: ", id, error))
            .onErrorResume(error -> Mono.just("Practitioner " + id + " temporairement indisponible"));
    }
    
    @PostMapping
    public Mono<Object> createPractitioner(@RequestBody Object practitioner) {
        return webClientBuilder.build()
            .post()
            .uri("http://practitioner-service/practitioners")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(practitioner)
            .retrieve()
            .bodyToMono(Object.class)
            .doOnError(error -> logger.error("Error creating practitioner: ", error))
            .onErrorResume(error -> Mono.just("Erreur lors de la création du practitioner"));
    }
    
    @PutMapping("/{id}")
    public Mono<Object> updatePractitioner(@PathVariable Long id, @RequestBody Object practitioner) {
        return webClientBuilder.build()
            .put()
            .uri("http://practitioner-service/practitioners/{id}", id)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(practitioner)
            .retrieve()
            .bodyToMono(Object.class)
            .doOnError(error -> logger.error("Error updating practitioner {}: ", id, error))
            .onErrorResume(error -> Mono.just("Erreur lors de la mise à jour du practitioner " + id));
    }
    
    @DeleteMapping("/{id}")
    public Mono<Object> deletePractitioner(@PathVariable Long id) {
        return webClientBuilder.build()
            .delete()
            .uri("http://practitioner-service/practitioners/{id}", id)
            .retrieve()
            .bodyToMono(Object.class)
            .doOnError(error -> logger.error("Error deleting practitioner {}: ", id, error))
            .onErrorResume(error -> Mono.just("Erreur lors de la suppression du practitioner " + id));
    }
} 