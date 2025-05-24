package com.reservia.reservia.server.service;

import com.reservia.reservia.server.model.Patient;
import com.reservia.reservia.server.repository.PatientRepository;
import jakarta.persistence.EntityManager;

import java.util.List;

public class PatientService {

    private final PatientRepository repository;

    public PatientService(EntityManager em) {
        this.repository = new PatientRepository(em);
    }

    public void savePatient(Patient patient) {
        repository.save(patient);
    }

    public List<Patient> findAllPatients() {
        return repository.findAll();
    }

    public Patient findById(int id) {
        return repository.findById(id);
    }

    public void updatePatient(Patient patient) {repository.update(patient);
    }

    public void deletePatient(Patient patient) {
        repository.delete(patient);
    }
}
