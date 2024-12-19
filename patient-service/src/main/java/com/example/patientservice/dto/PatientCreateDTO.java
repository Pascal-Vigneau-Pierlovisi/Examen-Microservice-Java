package com.example.patientservice.dto;

import lombok.Data;

@Data
public class PatientCreateDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
}