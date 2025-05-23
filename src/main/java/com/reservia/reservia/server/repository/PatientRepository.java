package com.reservia.reservia.server.repository;

import com.reservia.reservia.server.model.Patient;
import jakarta.persistence.EntityManager;

import java.util.List;

public class PatientRepository {
    private EntityManager em;

    public PatientRepository(EntityManager em) {
        this.em = em;
    }

    public void save(Patient patient) {
        em.getTransaction().begin();
        em.persist(patient);
        em.getTransaction().commit();
    }

    public List<Patient> findAll() {
        return em.createQuery("SELECT p FROM Patient p", Patient.class).getResultList();
    }

    public Patient findById(int id) {
        return em.find(Patient.class, id);
    }

    public void update(Patient patient) {
        em.getTransaction().begin();
        em.merge(patient);
        em.getTransaction().commit();
    }

    public void delete(Patient patient) {
        em.getTransaction().begin();
        if (!em.contains(patient)) {
            Patient managedPatient = em.find(Patient.class, patient.getPatientId());
            if (managedPatient != null) {
                em.remove(managedPatient);
            }
        } else {
            em.remove(patient);
        }
        em.getTransaction().commit();
    }
}
