package com.reservia.reservia.client.controller;

import com.reservia.reservia.server.model.Appointment;
import com.reservia.reservia.server.model.Doctor;
import com.reservia.reservia.server.model.Patient;
import com.reservia.reservia.server.remote.AppointmentServiceRemote;
import com.reservia.reservia.server.remote.DoctorServiceRemote;
import com.reservia.reservia.server.remote.PatientServiceRemote;
import com.reservia.reservia.server.service.AppointmentService;
import com.reservia.reservia.server.service.DoctorService;
import com.reservia.reservia.server.service.PatientService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Logger;

public class AppointmentListController {

    private AppointmentServiceRemote appointmentService;
    private DoctorServiceRemote doctorService;
    private PatientServiceRemote patientService;

    @FXML
    private TableView<Appointment> appointmentTable;

    @FXML
    private TableColumn<Appointment, String> colDate;

    @FXML
    private TableColumn<Appointment, String> colTime;

    @FXML
    private TableColumn<Appointment, String> colReason;

    @FXML
    private TableColumn<Appointment, String> colDoctor;

    @FXML
    private TableColumn<Appointment, String> colPatient;

    @FXML
    private TableColumn<Appointment, Void> colDelete;

    private ObservableList<Appointment> appointments;

    @FXML
    public void initialize() {
        try {
            doctorService = (DoctorServiceRemote) Naming.lookup("DoctorService");
            patientService = (PatientServiceRemote) Naming.lookup("PatientService");
            appointmentService = (AppointmentServiceRemote) Naming.lookup("AppointmentService");
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).severe("Error initializing AppointmentListController: " + e.getMessage());
        }

        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colTime.setCellValueFactory(new PropertyValueFactory<>("time"));
        colReason.setCellValueFactory(new PropertyValueFactory<>("reason"));

        
        colDoctor.setCellValueFactory(cellData -> {
            int doctorId = cellData.getValue().getDoctorId();
            Doctor doctor = null;
            try {
                doctor = doctorService.getDoctorById(doctorId);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            String doctorName = doctor.getFirstName()+" " + doctor.getLastName();
            return new SimpleStringProperty(doctorName);
        });

        colPatient.setCellValueFactory(cellData -> {
            int patientId = cellData.getValue().getPatientId();
            Patient patient = null;
            try {
                patient = patientService.getPatientById(patientId);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            String patientName =patient.getFirstName() + " " + patient.getLastName();
            return new SimpleStringProperty(patientName);
        });

        addDeleteButtonToTable();
        loadAppointments();
    }

    private void loadAppointments() {
        List<Appointment> appointmentList = null;
        try {
            appointmentList = appointmentService.getAllAppointments();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        appointments = FXCollections.observableArrayList(appointmentList);
        appointmentTable.setItems(appointments);
    }

    private void addDeleteButtonToTable() {
        Callback<TableColumn<Appointment, Void>, TableCell<Appointment, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Appointment, Void> call(final TableColumn<Appointment, Void> param) {
                return new TableCell<>() {
                    private final Button btn = new Button("Eliminar");

                    {
                        btn.setOnAction(event -> {
                            Appointment appointment = getTableView().getItems().get(getIndex());
                            appointments.remove(appointment);
                            try {
                                appointmentService.deleteAppointment(appointment.getAppointmentId());
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
            }
        };
        colDelete.setCellFactory(cellFactory);
    }
}
