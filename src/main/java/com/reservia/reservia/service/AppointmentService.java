package com.reservia.reservia.service;

import com.reservia.reservia.model.Appointment;
import com.reservia.reservia.repository.AppointmentRepository;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class AppointmentService {
    private final AppointmentRepository repository;

    public AppointmentService(EntityManager em) {
        this.repository = new AppointmentRepository(em);
    }

    public void saveAppointment(Appointment appointment) {
        repository.save(appointment);
    }

    public void deleteAppointment(Appointment appointment) {
        repository.delete(appointment);
    }

    public List<Appointment> findAllAppointments() {
        return repository.findAll();
    }

}
