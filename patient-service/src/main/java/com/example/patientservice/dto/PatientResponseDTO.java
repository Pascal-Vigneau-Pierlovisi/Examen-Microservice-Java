package com.example.patientservice.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PatientResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
