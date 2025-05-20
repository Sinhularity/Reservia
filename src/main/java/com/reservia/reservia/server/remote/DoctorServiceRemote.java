package com.reservia.reservia.server.remote;

import com.reservia.reservia.server.model.Doctor;

import java.rmi.Remote;
import java.util.List;

public interface DoctorServiceRemote extends Remote {
    List<Doctor> getAllDoctors() throws Exception;
    Doctor getDoctorById(int id) throws Exception;
    void addDoctor(Doctor doctor) throws Exception;
    void updateDoctor(Doctor doctor) throws Exception;
    void deleteDoctor(int id) throws Exception;
}
