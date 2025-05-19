package com.reservia.reservia.controller;

import com.reservia.reservia.model.Doctor;
import com.reservia.reservia.service.DoctorService;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

import javafx.scene.control.ScrollPane;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientController {

    @FXML
    private ScrollPane actualView;

    private void loadView(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
            actualView.setContent(root);
        } catch (Exception e) {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @FXML
    private void loadCreateAppointmentView() {
        loadView("/com/reservia/reservia/view/CreateAppointmentView.fxml");
    }
    @FXML
    private void loadAccountView() {
        loadView("/com/reservia/reservia/view/AccountView.fxml");
    }
    @FXML
    private void loadShowAppointmentView() {
        loadView("/com/reservia/reservia/view/ShowAppointmentView.fxml");
    }

    @FXML
    public void initialize() {
        loadCreateAppointmentView();
    }
}
