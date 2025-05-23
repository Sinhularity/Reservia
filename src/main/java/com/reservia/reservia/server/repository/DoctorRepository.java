package com.reservia.reservia.server.repository;

import com.reservia.reservia.server.model.Doctor;
import jakarta.persistence.EntityManager;

import java.util.List;

public class DoctorRepository {
    private final EntityManager em;

    public DoctorRepository(EntityManager em) {
        this.em = em;
    }

    public void save(Doctor doctor) {
        em.getTransaction().begin();
        em.persist(doctor);
        em.getTransaction().commit();
    }

    public List<Doctor> findAll() {
        return em.createQuery("SELECT d FROM Doctor d", Doctor.class).getResultList();
    }

    public Doctor findById(int id) {
        return em.find(Doctor.class, id);
    }

    public void update(Doctor doctor) {
        em.getTransaction().begin();
        em.merge(doctor);
        em.getTransaction().commit();
    }

    public void delete(Doctor doctor) {
        em.getTransaction().begin();
        if (!em.contains(doctor)) {
            Doctor managedDoctor = em.find(Doctor.class, doctor.getDoctorId());
            if (managedDoctor != null) {
                em.remove(managedDoctor);
            }
        } else {
            em.remove(doctor);
        }
        em.getTransaction().commit();
    }
}
