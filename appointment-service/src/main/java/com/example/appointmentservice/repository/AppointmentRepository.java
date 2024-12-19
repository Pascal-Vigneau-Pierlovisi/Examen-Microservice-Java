package com.example.appointmentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.appointmentservice.model.Appointment;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
} 