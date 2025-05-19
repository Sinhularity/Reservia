package com.reservia.reservia.service;

import com.reservia.reservia.model.Appointment;
import com.reservia.reservia.repository.AppointmentRepository;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;
import java.util.Date;

public class AppointmentService {
    private final AppointmentRepository repository;

    public AppointmentService(EntityManager em) {
        this.repository = new AppointmentRepository(em);
    }

    public void saveAppointment(int appointmentId, LocalDate date, String time, String reason, int doctorId, int patientId) {
        repository.save(new Appointment(appointmentId, date, time, reason, doctorId, patientId));
    }

    public void deleteAppointment(int appointmentId) {
        Appointment appointment = repository.findById(appointmentId);
        if (appointment != null) {
            repository.delete(appointment);
        }
    }

    public void findAllAppointments() {
        repository.findAll();
    }

}
