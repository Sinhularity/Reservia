package com.reservia.reservia.server.repository;

import com.reservia.reservia.server.model.Appointment;
import jakarta.persistence.EntityManager;

import java.util.List;

public class AppointmentRepository {
    private final EntityManager em;


    public AppointmentRepository(EntityManager em) {
        this.em = em;
    }

    public void save(Appointment appointment) {
        em.getTransaction().begin();
        em.persist(appointment);
        em.getTransaction().commit();
    }
    public List<Appointment> findAll() {
        return em.createQuery("SELECT a FROM Appointment a"
                , Appointment.class).getResultList();
    }
    public Appointment findById(int id) {
        return em.find(Appointment.class, id);
    }

    public void update(Appointment appointment) {
        em.getTransaction().begin();
        em.merge(appointment);
        em.getTransaction().commit();
    }



    public void delete(Appointment appointment) {
        em.getTransaction().begin();
        em.remove(appointment);
        em.getTransaction().commit();
    }
}
