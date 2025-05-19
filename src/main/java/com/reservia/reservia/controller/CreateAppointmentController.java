package com.reservia.reservia.controller;

import com.reservia.reservia.model.Doctor;
import com.reservia.reservia.service.DoctorService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.awt.event.ActionEvent;
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

    private LocalDate selectedDate;
    private String selectedTime;
    private Doctor selectedDoctor;

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

    public void getDateAndTime() {
        try {
            if (calendar.getValue() != null && timeComboBox.getValue() != null) {
                selectedDate = calendar.getValue();
                selectedTime = timeComboBox.getValue();
            }
        } catch (Exception e) {
            Logger.getLogger(CreateAppointmentController.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
        }
    }

    public void getDoctor() {
        selectedDoctor = doctorListView.getSelectionModel().getSelectedItem();
        if (selectedDoctor != null) {
            System.out.println("Doctor seleccionado: " + selectedDoctor.getFirstName() + " " + selectedDoctor.getLastName());
        }
    }

    @FXML
    public void initialize() {
        // Disable dates before today in the DatePicker
        calendar.setDayCellFactory(d -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);

                setDisable(item.isBefore(LocalDate.now().plusDays(1)));
            }
        });

        initializeTimeComboBox();

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("reserviaPU");
        EntityManager em = emf.createEntityManager();

        DoctorService doctorService = new DoctorService(em);
        doctors = doctorService.findAllDoctors();

        // Cargar la ListView con los datos
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


        System.out.println("Doctor list size: " + doctors.size());
        em.close();
        emf.close();
    }

    @FXML
    public void createAppointment() {
        getDoctor();
        getDateAndTime();
        if (selectedDate != null && selectedTime != null && selectedDoctor != null) {
            System.out.println("Cita creada para el doctor: " + selectedDoctor.getFirstName() + " " + selectedDoctor.getLastName());
            System.out.println("Fecha: " + selectedDate);
            System.out.println("Hora: " + selectedTime);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Advertencia");
            alert.setContentText("Rellena todos los campos");
            alert.showAndWait();
        }
    }
}
