package com.reservia.reservia.client.controller;

import com.reservia.reservia.server.model.Patient;
import com.reservia.reservia.server.remote.PatientServiceRemote;
import com.reservia.reservia.server.service.PatientService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;

import java.rmi.Naming;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CreatePatientController {

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField middleNameField;

    @FXML
    private TextField curpField;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField emailField;

    String firstName;
    String lastName;
    String middleName;
    String curp;
    String phone;
    String email;

    private EntityManagerFactory emf;
    private EntityManager em;
    private PatientServiceRemote patientService;

    private PatientListController patientListController;

    @FXML
    public void initialize() {
        try {
            patientService = (PatientServiceRemote) Naming.lookup("PatientService");
        } catch (Exception e) {
            Logger.getLogger(PatientListController.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
        }
    }

    @FXML
    private void savePatient() {
        try {
             firstName = firstNameField.getText().trim();
             lastName = lastNameField.getText().trim();
             middleName = middleNameField.getText().trim();
             curp = curpField.getText().trim();
             phone = phoneField.getText().trim();
             email = emailField.getText().trim();
            if(isUserInputValid()) {
                patientService.addPatient(new Patient(firstName, lastName, middleName, curp, phone, email));
                cleanFields();
                showPatientConfirmation();
            } else {
                showWarningAlert();
            }
            close();
        } catch (Exception e) {
            Logger.getLogger(CreatePatientController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private void cleanFields() {
        firstNameField.clear();
        lastNameField.clear();
        middleNameField.clear();
        curpField.clear();
        phoneField.clear();
        emailField.clear();
    }

    public void close() {
        if (em != null && em.isOpen()) em.close();
        if (emf != null && emf.isOpen()) emf.close();
    }

    public boolean isUserInputValid() {
        if (firstName.isEmpty() || lastName.isEmpty() || middleName.isEmpty() || curp.isEmpty() || phone.isEmpty() || email.isEmpty()) {
            return false;
        }
        return true;
    }

    private void showPatientConfirmation() {
        System.out.println("Paciente creado: \n" +
                firstName + " " + middleName + " " + lastName + " - CURP: " + curp + " - Teléfono: " + phone + " - Email: " + email);
    }

    private void showWarningAlert() {
        final String WARNING_MESSAGE = "Rellena todos los campos";
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Advertencia");
        alert.setContentText(WARNING_MESSAGE);
        alert.showAndWait();
    }
}