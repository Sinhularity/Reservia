package com.reservia.reservia.server.remote;

import com.reservia.reservia.server.model.Patient;
import com.reservia.reservia.server.service.PatientService;
import jakarta.persistence.EntityManager;


import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class PatientServiceRemoteImpl extends UnicastRemoteObject implements PatientServiceRemote {

    private PatientService patientService;

    public PatientServiceRemoteImpl(EntityManager em) throws RemoteException {
        this.patientService = new PatientService(em);
    }

    @Override
    public List<Patient> getAllPatients() throws RemoteException {
        return patientService.findAllPatients();
    }

    @Override
    public Patient getPatientById(int id) throws RemoteException {
        return patientService.findById(id);
    }

    @Override
    public void addPatient(Patient patient) throws RemoteException {
        patientService.savePatient(patient);
    }

    @Override
    public void updatePatient(Patient patient) throws RemoteException {
        try {
            patientService.updatePatient(patient);
        } catch (Exception e) {
            throw new RemoteException("Error actualizando paciente en el servidor: " + e.getMessage(), e);
        }
    }

    @Override
    public void deletePatient(int id) throws RemoteException {
        try {
            Patient patientToDelete = patientService.findById(id);
            if (patientToDelete != null) {
                patientService.deletePatient(patientToDelete);
            } else {
            }
        } catch (Exception e) {
            throw new RemoteException("Error eliminando paciente en el servidor: " + e.getMessage(), e);
        }
    }
}
