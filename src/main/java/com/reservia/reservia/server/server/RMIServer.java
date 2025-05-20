package com.reservia.reservia.server.server;

import com.reservia.reservia.server.remote.*;
import com.reservia.reservia.server.service.AppointmentService;
import com.reservia.reservia.server.service.DoctorService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.nio.channels.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIServer {
    public static void main(String[] args) {

        try {
            Registry registry = LocateRegistry.createRegistry(1099);
            System.out.println("RMI registry created.");

            EntityManagerFactory emf = Persistence.createEntityManagerFactory("reserviaPU");
            EntityManager em = emf.createEntityManager();

            PatientServiceRemote patientService = new PatientServiceRemoteImpl(em);
            DoctorServiceRemote doctorService = new DoctorServiceRemoteImpl(em);
            AppointmentServiceRemote appointmentService = new AppoinmentServiceRemoteImpl(em);

            registry.rebind("PatientService", patientService);
            System.out.println("PatientService bound in registry.");
            registry.rebind("DoctorService", doctorService);
            System.out.println("DoctorService bound in registry.");
            registry.rebind("AppointmentService", appointmentService);
            System.out.println("AppointmentService bound in registry.");

        } catch (AlreadyBoundException e) {
            System.err.println("RMI registry already exists.");
        } catch (RemoteException e) {
            System.err.println("Error creating RMI registry: " + e.getMessage());
        }
    }
}
