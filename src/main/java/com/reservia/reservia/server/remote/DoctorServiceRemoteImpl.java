package com.reservia.reservia.server.remote;

import com.reservia.reservia.server.model.Doctor;
import com.reservia.reservia.server.service.DoctorService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class DoctorServiceRemoteImpl extends UnicastRemoteObject implements DoctorServiceRemote {

    private DoctorService doctorService;

    public DoctorServiceRemoteImpl(EntityManager em) throws RemoteException {
        this.doctorService = new DoctorService(em);
    }

    @Override
    public List<Doctor> getAllDoctors() throws Exception {
        return doctorService.findAllDoctors();
    }

    @Override
    public Doctor getDoctorById(int id) throws Exception {
        return doctorService.findById(id);
    }

    @Override
    public void addDoctor(Doctor doctor) throws Exception {
        doctorService.saveDoctor(doctor);
    }

    @Override
    public void updateDoctor(Doctor doctor) throws Exception {

    }

    @Override
    public void deleteDoctor(int id) throws Exception {
        doctorService.deleteDoctor(doctorService.findById(id));
    }
}
