package com.reservia.reservia.server.remote;

import com.reservia.reservia.server.model.Appointment;
import com.reservia.reservia.server.model.Doctor;
import com.reservia.reservia.server.model.Patient;
import com.reservia.reservia.server.service.AppointmentService;
import com.reservia.reservia.server.service.DoctorService;
import com.reservia.reservia.server.service.PatientService;
import jakarta.persistence.EntityManager;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class AppoinmentServiceRemoteImpl extends UnicastRemoteObject implements AppointmentServiceRemote {
    private AppointmentService appointmentService;
    private DoctorService doctorService;
    private PatientService patientService;

    public AppoinmentServiceRemoteImpl(EntityManager em) throws RemoteException {
        this.appointmentService = new AppointmentService(em);
        this.doctorService = new DoctorService(em);
        this.patientService = new PatientService(em);
    }

    @Override
    public List<Appointment> getAllAppointments() throws Exception {
        return appointmentService.findAllAppointments();
    }

    @Override
    public Appointment getAppointmentById(int id) throws Exception {
        return appointmentService.findById(id);
    }

    @Override
    public void addAppointment(Appointment appointment) throws Exception {
        appointmentService.saveAppointment(appointment);
    }

    @Override
    public void updateAppointment(Appointment appointment) throws Exception {
        appointmentService.updateAppointment(appointment);
    }

    @Override
    public void deleteAppointment(int id) throws Exception {
        appointmentService.deleteAppointment(appointmentService.findById(id));
    }

    @Override
    public List<Doctor> getAllDoctors() throws Exception {
        return doctorService.findAllDoctors();
    }
    @Override
    public List<Patient> getAllPatients() throws Exception {
        return patientService.findAllPatients();
    }
}
