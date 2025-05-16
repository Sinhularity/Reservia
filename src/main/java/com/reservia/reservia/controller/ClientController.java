package com.reservia.reservia.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientController {


    @FXML
    private AnchorPane actualView;

    private void loadView(String fxml) {
        try {
            actualView.getChildren().clear();

            AnchorPane view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxml)));
            actualView.getChildren().setAll(view);

            actualView.setTopAnchor(view, 0.0);
            actualView.setBottomAnchor(view, 0.0);
            actualView.setLeftAnchor(view, 0.0);
            actualView.setRightAnchor(view, 0.0);
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
}
