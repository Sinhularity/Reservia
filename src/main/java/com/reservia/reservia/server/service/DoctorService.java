package com.reservia.reservia.server.service;

import com.reservia.reservia.server.model.Doctor;
import com.reservia.reservia.server.repository.DoctorRepository;
import jakarta.persistence.EntityManager;

import java.util.List;


public class DoctorService {

    private final DoctorRepository repository;

    public DoctorService(EntityManager em) {
        this.repository = new DoctorRepository(em);
    }

    public List<Doctor> findAllDoctors () {
        return repository.findAll();
    }

    public Doctor findById (int id) {
        return repository.findById(id);
    }

    public void saveDoctor (Doctor doctor) {
        repository.save(doctor);
    }

    public void updateDoctor(Doctor doctor) { repository.update(doctor); }

    public void deleteDoctor (Doctor doctor) {
        repository.delete(doctor);
    }
}
