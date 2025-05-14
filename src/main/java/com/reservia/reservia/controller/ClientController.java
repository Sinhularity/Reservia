package com.reservia.reservia.controller;

import javafx.fxml.FXML;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import tornadofx.control.DateTimePicker;

import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ClientController {
    @FXML
    private DatePicker calendar;

    @FXML
    private Label ClientTitleLabel;

    // Remove this
    @FXML
    private DateTimePicker dateTimePicker;

    @FXML
    public void initialize() {
        // Disable dates before today in the DatePicker
        calendar.setDayCellFactory(d -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);

                setDisable(item.isBefore(LocalDate.now()));
            }
        });
    }

    public void getDate(ActionEvent event) {
        LocalDate date = calendar.getValue();
        String formattedDate = date.format(DateTimeFormatter.ofPattern("MMM-dd-yyyy"));
        System.out.println(formattedDate);
    }
}
