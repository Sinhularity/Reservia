package com.reservia.reservia.server.remote;

import com.reservia.reservia.server.model.Patient;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface PatientServiceRemote extends Remote {


    List<Patient> getAllPatients() throws RemoteException;
    Patient getPatientById(int id) throws RemoteException;
    void addPatient(Patient patient) throws RemoteException;
    void updatePatient(Patient patient) throws RemoteException;
    void deletePatient(int id) throws RemoteException;
}
