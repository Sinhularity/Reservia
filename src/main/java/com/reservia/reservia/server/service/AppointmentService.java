package com.reservia.reservia.server.service;

import com.reservia.reservia.server.model.Appointment;
import com.reservia.reservia.server.repository.AppointmentRepository;
import jakarta.persistence.EntityManager;

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

    public Appointment findById(int id) {
        return repository.findById(id);
    }

    public List<Appointment> findAllAppointments() {
        return repository.findAll();
    }

}
