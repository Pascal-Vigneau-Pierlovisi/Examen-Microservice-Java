package com.example.practitionerservice.dto;

import lombok.Data;

@Data
public class PractitionerCreateDTO {
    private String firstName;
    private String lastName;
    private String specialization;
    private String email;
    private String phoneNumber;
}
