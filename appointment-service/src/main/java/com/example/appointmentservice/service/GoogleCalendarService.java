package com.example.appointmentservice.service;

import com.example.appointmentservice.model.Appointment;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class GoogleCalendarService {

    private final Calendar calendarService;
    private static final String CALENDAR_ID = "primary";

    @CircuitBreaker(name = "googleCalendar")
    @Retry(name = "retryGoogleCalendar")
    public String createEvent(Appointment appointment) {
        try {
            Event event = new Event()
                    .setSummary("Rendez-vous médical - Patient " + appointment.getPatientId())
                    .setDescription("Rendez-vous médical");

            DateTime startDateTime = new DateTime(appointment.getAppointmentDate().toString());
            EventDateTime start = new EventDateTime()
                    .setDateTime(startDateTime);
            event.setStart(start);

            DateTime endDateTime = new DateTime(appointment.getAppointmentDate().plusHours(1).toString());
            EventDateTime end = new EventDateTime()
                    .setDateTime(endDateTime);
            event.setEnd(end);

            Event createdEvent = calendarService.events()
                    .insert(CALENDAR_ID, event)
                    .execute();

            log.info("Event created for appointment {}: {}", appointment.getId(), createdEvent.getHtmlLink());
            return createdEvent.getId();
        } catch (Exception e) {
            log.error("Error creating Google Calendar event", e);
            throw new RuntimeException("Failed to create Google Calendar event", e);
        }
    }

    @CircuitBreaker(name = "googleCalendar")
    @Retry(name = "retryGoogleCalendar")
    public void deleteEvent(String eventId) {
        try {
            calendarService.events().delete(CALENDAR_ID, eventId).execute();
            log.info("Event {} deleted successfully", eventId);
        } catch (Exception e) {
            log.error("Error deleting Google Calendar event {}", eventId, e);
            throw new RuntimeException("Failed to delete Google Calendar event", e);
        }
    }
} 