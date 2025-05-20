package com.reservia.reservia.controller;

import com.reservia.reservia.model.Doctor;
import com.reservia.reservia.service.DoctorService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CreateDoctorController {

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField middleNameField;

    @FXML
    private TextField specialtyField;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField emailField;

    private String firstName;
    private String lastName;
    private String middleName;
    private String specialty;
    private String phone;
    private String email;

    private EntityManagerFactory emf;
    private EntityManager em;
    private DoctorService doctorService;

    @FXML
    public void initialize() {
        emf = Persistence.createEntityManagerFactory("reserviaPU");
        em = emf.createEntityManager();
        doctorService = new DoctorService(em);
    }

    @FXML
    private void saveDoctor() {
        try {
            firstName = firstNameField.getText().trim();
            lastName = lastNameField.getText().trim();
            middleName = middleNameField.getText().trim();
            specialty = specialtyField.getText().trim();
            phone = phoneField.getText().trim();
            email = emailField.getText().trim();

            if (isUserInputValid()) {
                doctorService.saveDoctor(new Doctor(firstName, lastName, middleName, specialty, phone, email));
                cleanFields();
                showDoctorConfirmation();
            } else {
                showWarningAlert();
            }
            close();
        } catch (Exception e) {
            Logger.getLogger(CreateDoctorController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private void cleanFields() {
        firstNameField.clear();
        lastNameField.clear();
        middleNameField.clear();
        specialtyField.clear();
        phoneField.clear();
        emailField.clear();
    }

    public void close() {
        if (em != null && em.isOpen()) em.close();
        if (emf != null && emf.isOpen()) emf.close();
    }

    public boolean isUserInputValid() {
        return !(firstName.isEmpty() || lastName.isEmpty() || middleName.isEmpty() || specialty.isEmpty() || phone.isEmpty() || email.isEmpty());
    }

    private void showDoctorConfirmation() {
        System.out.println("Doctor creado: \n" +
                firstName + " " + middleName + " " + lastName + " - Especialidad: " + specialty + " - Teléfono: " + phone + " - Email: " + email);
    }

    private void showWarningAlert() {
        final String WARNING_MESSAGE = "Rellena todos los campos";
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Advertencia");
        alert.setContentText(WARNING_MESSAGE);
        alert.showAndWait();
    }
}