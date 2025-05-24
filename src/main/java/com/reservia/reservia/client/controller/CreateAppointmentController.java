package com.reservia.reservia.client.controller;

import com.reservia.reservia.server.model.Appointment;
import com.reservia.reservia.server.model.Doctor;
import com.reservia.reservia.server.model.Patient;
import com.reservia.reservia.server.remote.AppointmentServiceRemote;
import com.reservia.reservia.server.service.AppointmentService;
import com.reservia.reservia.server.service.DoctorService;
import com.reservia.reservia.server.service.PatientService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.rmi.Naming;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Logger;

public class CreateAppointmentController {
    @FXML
    public DatePicker calendar;

    @FXML
    public ComboBox<String> timeComboBox;

    @FXML
    public Button createAppointmentButton;

    private List<Doctor> doctors;

    @FXML
    public ListView<Doctor> doctorListView;

    @FXML
    public ListView<Patient> patientListView;

    private List<Patient> patients;

    @FXML
    public TextArea reasonTextArea;

    private LocalDate selectedDate;
    private String selectedTime;
    private Doctor selectedDoctor;
    private Patient selectedPatient;
    private String reason;

    private AppointmentServiceRemote appointmentService;
    private boolean editMode = false;
    private int appointmentIdToEdit = -1;

    private void initializeTimeComboBox() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = LocalTime.of(18, 0);

        ObservableList<String> hours = FXCollections.observableArrayList();

        int interval = 60;
        LocalTime time = startTime;
        while (!time.isAfter(endTime)) {
            hours.add(time.format(formatter));
            time = time.plusMinutes(interval);
        }

        timeComboBox.setItems(hours);
        timeComboBox.setPromptText("Selecciona una hora");
    }
    @FXML
    public void initialize() {
        try {
            appointmentService = (AppointmentServiceRemote) Naming.lookup("AppointmentService");
        } catch (Exception e) {
            Logger.getLogger(CreateAppointmentController.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
        }

        calendar.setDayCellFactory(d -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);

                setDisable(item.isBefore(LocalDate.now().plusDays(1)));
            }
        });

        initializeTimeComboBox();
        List<Doctor> doctors = null;
        List<Patient> patients = null;
        try {
            doctors = appointmentService.getAllDoctors();
            patients = appointmentService.getAllPatients();
        } catch (Exception e) {
            Logger.getLogger(CreateAppointmentController.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
        }
        ObservableList<Doctor> observableDoctors = FXCollections.observableArrayList(doctors);
        doctorListView.setItems(observableDoctors);

        doctorListView.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(Doctor doctor, boolean empty) {
                super.updateItem(doctor, empty);
                if (empty || doctor == null) {
                    setText(null);
                } else {
                    setText(doctor.getFirstName() + " " + doctor.getLastName()+ " - "+ doctor.getSpecialty());
                }
            }
        });

        ObservableList<Patient> observablePatients = FXCollections.observableArrayList(patients);
        patientListView.setItems(observablePatients);

        patientListView.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(Patient patient, boolean empty) {
                super.updateItem(patient, empty);
                if (empty || patient == null) {
                    setText(null);
                } else {
                    setText(patient.getFirstName() + " "+patient.getMiddleName() +" " + patient.getLastName());
                }
            }
        });

    }

    @FXML
    public void createAppointment() {

        obtainSelectedValues();

        if (areRequiredFieldsComplete()) {
            saveAppointment();
            showAppointmentConfirmation();
        } else {
            showWarningAlert();
        }

    }

    private void showAppointmentConfirmation() {
        final String CONFIRMATION_MESSAGE = "Cita creada con éxito";
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Cita creada");
        alert.setContentText(CONFIRMATION_MESSAGE);
        alert.setHeaderText("Detalles de la cita:");
        alert.setContentText("Fecha: " + selectedDate + "\n" +
                "Hora: " + selectedTime + "\n" +
                "Doctor: " + selectedDoctor.getFirstName() + " " + selectedDoctor.getLastName() + "\n" +
                "Paciente: " + selectedPatient.getFirstName() + " "+selectedPatient.getMiddleName() +" "+ selectedPatient.getLastName() + "\n" +
                "Motivo: " + reason);
        alert.showAndWait();
    }

    private void showWarningAlert() {
        final String WARNING_MESSAGE = "Rellena todos los campos";
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Advertencia");
        alert.setContentText(WARNING_MESSAGE);
        alert.showAndWait();
    }

    private void obtainSelectedValues() {
        try {
            selectedDate = calendar.getValue();
            selectedTime = timeComboBox.getValue();
            selectedDoctor = doctorListView.getSelectionModel().getSelectedItem();
            selectedPatient = patientListView.getSelectionModel().getSelectedItem();
            reason = reasonTextArea.getText();
        } catch (Exception e) {
            System.out.println("Empty fields...");
        } finally {
            System.out.println("-------------------------------");
            System.out.println("Selected date: " + selectedDate);
            System.out.println("Selected time: " + selectedTime);
            System.out.println("Selected doctor: " + selectedDoctor);
            System.out.println("Selected patient: " + selectedPatient);
            System.out.println("Reason: " + reason);
            System.out.println("------------------------------");
        }
    }

    private boolean areRequiredFieldsComplete() {
        return selectedDate != null &&
                selectedTime != null &&
                selectedDoctor != null &&
                selectedPatient != null &&
                reason != null;
    }

    public void saveAppointment() {
        try {
            Appointment newAppointment = new Appointment(
                    selectedDate,
                    selectedTime,
                    reason,
                    selectedDoctor.getDoctorId(),
                    selectedPatient.getPatientId()
            );

            if (editMode) {
                newAppointment.setAppointmentId(appointmentIdToEdit);
                appointmentService.updateAppointment(newAppointment);
            } else {
                appointmentService.addAppointment(newAppointment);
            }

        } catch (Exception e) {
            Logger.getLogger(CreateAppointmentController.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
        }
    }


    public void loadAppointmentDataForEdit(Appointment appointment) {
        if (appointment == null) return;

        editMode = true;
        appointmentIdToEdit = appointment.getAppointmentId();
        calendar.setValue(appointment.getDate());
        timeComboBox.setValue(appointment.getTime());
        reasonTextArea.setText(appointment.getReason());

        for (Doctor doctor : doctorListView.getItems()) {
            if (doctor.getDoctorId() == appointment.getDoctorId()) {
                doctorListView.getSelectionModel().select(doctor);
                break;
            }
        }

        for (Patient patient : patientListView.getItems()) {
            if (patient.getPatientId() == appointment.getPatientId()) {
                patientListView.getSelectionModel().select(patient);
                break;
            }
        }

        createAppointmentButton.setText("Guardar Cambios");
    }
}
