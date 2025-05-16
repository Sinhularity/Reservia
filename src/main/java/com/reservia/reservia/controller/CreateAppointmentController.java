package com.reservia.reservia.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;

import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class CreateAppointmentController {
    @FXML
    private DatePicker calendar;

    private String selectedDate;

    @FXML
    private ComboBox<String> timeComboBox;

    private String selectedTime;


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

    public void getDate(ActionEvent event) {
        LocalDate date = calendar.getValue();
        selectedDate = date.format(DateTimeFormatter.ofPattern("MMM-dd-yyyy"));
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
    }
}
