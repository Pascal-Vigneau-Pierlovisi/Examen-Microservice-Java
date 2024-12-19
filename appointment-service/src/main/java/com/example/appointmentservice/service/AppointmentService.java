package com.example.appointmentservice.service;

import com.example.appointmentservice.dto.AppointmentRequest;
import com.example.appointmentservice.model.Appointment;
import com.example.appointmentservice.repository.AppointmentRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final GoogleCalendarService googleCalendarService;

    /**
     * Crée un rendez-vous en base de données et, si possible, sur Google Calendar.
     *
     * @param request Les détails du rendez-vous
     * @return Le rendez-vous créé
     */
    public Appointment createAppointment(AppointmentRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("AppointmentRequest ne peut pas être nul.");
        }

        Appointment appointment = Appointment.builder()
                .patientId(request.getPatientId())
                .practitionerId(request.getPractitionerId())
                .appointmentDate(request.getAppointmentDate())
                .status("Scheduled")
                .build();

        log.info("Création du rendez-vous pour le patient {} avec le praticien {}",
                appointment.getPatientId(), appointment.getPractitionerId());

        // Sauvegarde initiale en base de données
        appointment = appointmentRepository.save(appointment);

        // Création de l'événement Google Calendar
        try {
            String eventId = googleCalendarService.createEvent(appointment);
            if (eventId != null) {
                appointment.setGoogleEventId(eventId);
                appointmentRepository.save(appointment); // Mise à jour avec l'ID Google
                log.info("Événement Google Calendar créé avec succès : {}", eventId);
            }
        } catch (Exception e) {
            log.error("Erreur lors de la création de l'événement Google Calendar : {}", e.getMessage(), e);
        }

        return appointment;
    }

    /**
     * Récupère tous les rendez-vous.
     *
     * @return Une liste de rendez-vous
     */
    @Transactional(readOnly = true)
    public List<Appointment> getAllAppointments() {
        log.info("Récupération de tous les rendez-vous.");
        return appointmentRepository.findAll();
    }

    /**
     * Récupère un rendez-vous par son ID.
     *
     * @param id L'ID du rendez-vous
     * @return Un Optional contenant le rendez-vous ou vide s'il n'existe pas
     */
    @Transactional(readOnly = true)
    public Optional<Appointment> getAppointmentById(Long id) {
        log.info("Récupération du rendez-vous avec l'ID : {}", id);
        return appointmentRepository.findById(id);
    }

    /**
     * Supprime un rendez-vous.
     *
     * @param id L'ID du rendez-vous à supprimer
     */
    public void deleteAppointment(Long id) {
        log.info("Suppression du rendez-vous avec l'ID : {}", id);
        Optional<Appointment> appointment = appointmentRepository.findById(id);

        if (appointment.isPresent()) {
            try {
                // Suppression de l'événement Google Calendar si présent
                if (appointment.get().getGoogleEventId() != null) {
                    googleCalendarService.deleteEvent(appointment.get().getGoogleEventId());
                    log.info("Événement Google Calendar supprimé avec succès.");
                }
            } catch (Exception e) {
                log.error("Erreur lors de la suppression de l'événement Google Calendar : {}", e.getMessage(), e);
            }

            // Suppression du rendez-vous en base de données
            appointmentRepository.deleteById(id);
            log.info("Rendez-vous supprimé avec succès.");
        } else {
            log.warn("Aucun rendez-vous trouvé avec l'ID : {}", id);
        }
    }
}
