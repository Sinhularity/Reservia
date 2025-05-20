package com.reservia.reservia.controller;

import com.reservia.reservia.model.Patient;
import com.reservia.reservia.service.PatientService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.fxml.FXML;
import javafx.scene.control.*;

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
    private PatientService patientService;

    @FXML
    public void initialize() {
        emf = Persistence.createEntityManagerFactory("reserviaPU");
        em = emf.createEntityManager();
        patientService = new PatientService(em);
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
                patientService.savePatient(new Patient(firstName, lastName, middleName, curp, phone, email));
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