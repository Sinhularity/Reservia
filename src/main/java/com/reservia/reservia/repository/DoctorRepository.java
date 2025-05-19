package com.reservia.reservia.repository;

import com.reservia.reservia.model.Doctor;
import jakarta.persistence.EntityManager;

import java.util.List;
// Class in charge of the database operations for the Doctor entity
public class DoctorRepository {
    private final EntityManager em;

    public DoctorRepository(EntityManager em) {
        this.em = em;
    }

    public List<Doctor> findAll() {
        return em.createQuery("SELECT d FROM Doctor d", Doctor.class).getResultList();
    }
}
