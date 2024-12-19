package com.example.practitionerservice.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PractitionerResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String specialization;
    private String email;
    private String phoneNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
