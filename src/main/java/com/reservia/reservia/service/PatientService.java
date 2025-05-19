package com.reservia.reservia.service;

import com.reservia.reservia.model.Patient;
import com.reservia.reservia.repository.PatientRepository;
import jakarta.persistence.EntityManager;

public class PatientService {

    private final PatientRepository repository;

    public PatientService(EntityManager em) {
        this.repository = new PatientRepository(em);
    }

    public void savePatient(int id, String firstName, String middleName, String lastName, String curp, String phoneNumber, String email) {
        Patient patient = new Patient(id, firstName, middleName, lastName, curp, phoneNumber, email);
        repository.save(patient);
    }

    public void deletePatientByCURP(String curp) {
        Patient patient = repository.findByCurp(curp);
        if (patient != null) {
            repository.delete(patient);
        }
    }
}
