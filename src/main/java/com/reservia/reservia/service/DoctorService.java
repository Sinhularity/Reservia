package com.reservia.reservia.service;

import com.reservia.reservia.model.Doctor;
import com.reservia.reservia.repository.DoctorRepository;
import jakarta.persistence.EntityManager;

import java.util.List;

// Class in charge of the business logic for the Doctor entity
public class DoctorService {

    private final DoctorRepository repository;

    public DoctorService(EntityManager em) {
        this.repository = new DoctorRepository(em);
    }

    public List<Doctor> findAllDoctors () {
        return repository.findAll();
    }
}
