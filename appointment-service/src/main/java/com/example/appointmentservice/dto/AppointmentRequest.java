package com.example.appointmentservice.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AppointmentRequest {
    private Long patientId;
    private Long practitionerId;
    private LocalDateTime appointmentDate;
} 