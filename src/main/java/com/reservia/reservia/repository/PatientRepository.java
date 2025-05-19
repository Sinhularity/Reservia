package com.reservia.reservia.repository;

import com.reservia.reservia.model.Patient;
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


    public Patient findByCurp(String curp) {
        return em.createQuery("SELECT p FROM Patient p WHERE p.curp = :curp", Patient.class)
                .setParameter("curp", curp)
                .getSingleResult();
    }
   public Patient findByName(String name) {
        return em.createQuery("SELECT p FROM Patient p WHERE p.firstName = :name", Patient.class)
                .setParameter("name", name)
                .getSingleResult();
   }

   public Patient findByLastName(String lastName) {
       return em.createQuery("SELECT p FROM Patient p WHERE p.lastName = :lastName", Patient.class)
               .setParameter("lastName", lastName)
               .getSingleResult();
   }

    public void delete(Patient patient) {
        em.getTransaction().begin();
        em.remove(patient);
        em.getTransaction().commit();
    }
}
