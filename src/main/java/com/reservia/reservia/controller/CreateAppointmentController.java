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

public class CreateAppointmentController {
    @FXML
    public DatePicker calendar;

    private String selectedDate;

    @FXML
    public ComboBox<String> timeComboBox;

    private String selectedTime;


    private List<Doctor> doctors;

    @FXML
    public ListView<Doctor> doctorListView;

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

    public void getDateAndTime(ActionEvent event) {
        LocalDate date = calendar.getValue();
        selectedDate = date.format(DateTimeFormatter.ofPattern("MMM-dd-yyyy"));
        selectedTime = timeComboBox.getValue();
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
}
