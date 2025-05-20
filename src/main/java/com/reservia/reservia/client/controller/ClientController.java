package com.reservia.reservia.client.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
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
    private void loadShowPatientView() {
        loadView("/com/reservia/reservia/view/MainPatientView.fxml");
    }
    @FXML
    private void loadShowAppointmentView() {
        loadView("/com/reservia/reservia/view/ShowAppointmentView.fxml");
    }

    @FXML
    private void loadShowDoctorView() {
        loadView("/com/reservia/reservia/view/MainDoctorView.fxml");
    }

    @FXML
    public void initialize() {
        loadCreateAppointmentView();
    }
}
