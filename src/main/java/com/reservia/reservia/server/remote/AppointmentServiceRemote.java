package com.reservia.reservia.server.remote;

import com.reservia.reservia.server.model.Appointment;
import com.reservia.reservia.server.model.Doctor;
import com.reservia.reservia.server.model.Patient;

import java.rmi.Remote;
import java.util.List;

public interface AppointmentServiceRemote extends Remote {
    List<Appointment> getAllAppointments() throws Exception;
    Appointment getAppointmentById(int id) throws Exception;
    void addAppointment(Appointment appointment) throws Exception;
    void updateAppointment(Appointment appointment) throws Exception;
    void deleteAppointment(int id) throws Exception;

    List<Doctor> getAllDoctors() throws Exception;
    List<Patient> getAllPatients() throws Exception;
}
