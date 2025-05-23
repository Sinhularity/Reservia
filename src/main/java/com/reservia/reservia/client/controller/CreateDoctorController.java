package com.reservia.reservia.client.controller;

import com.reservia.reservia.server.model.Doctor;
import com.reservia.reservia.server.remote.DoctorServiceRemote;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.regex.Pattern;
import javafx.stage.Stage;
import java.rmi.Naming;
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
    private TextField licenseNumberField;

    @FXML
    private TextField emailField;

    private static final Pattern EMAIL_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private static final Pattern NAME_REGEX = Pattern.compile("^[\\p{L} .'-]+$");

    private String firstName;
    private String lastName;
    private String middleName;
    private String specialty;
    private String licenseNumber;
    private String email;

    private EntityManagerFactory emf;
    private EntityManager em;
    private DoctorServiceRemote doctorService;
    private boolean editMode = false;
    private int doctorIdToEdit;

    @FXML
    public void initialize() {
        try {
            doctorService = (DoctorServiceRemote) Naming.lookup("DoctorService");
        } catch (Exception e) {
            Logger.getLogger(PatientListController.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
        }
    }
    public void loadDoctorDataForEdit(Doctor doctor) {
        if (doctor == null) {
            System.err.println("Error: Se intentó cargar datos de un doctor nulo para editar.");
            return;
        }
        firstNameField.setText(doctor.getFirstName());
        lastNameField.setText(doctor.getLastName());
        middleNameField.setText(doctor.getMiddleName() != null ? doctor.getMiddleName() : "");
        specialtyField.setText(doctor.getSpecialty());
        licenseNumberField.setText(doctor.getLicenseNumber());
        emailField.setText(doctor.getEmail());

        this.editMode = true;
        this.doctorIdToEdit = doctor.getDoctorId();
    }

    @FXML
    private void saveDoctor() {
        firstName = firstNameField.getText().trim();
        lastName = lastNameField.getText().trim();
        middleName = middleNameField.getText().trim();
        specialty = specialtyField.getText().trim();
        licenseNumber = licenseNumberField.getText().trim();
        email = emailField.getText().trim();

        if (isUserInputValid()) {
            Doctor doctor = new Doctor(firstName, lastName, middleName, licenseNumber, specialty, email);
            try {
                if (editMode) {
                    doctor.setDoctorId(doctorIdToEdit);
                    doctorService.updateDoctor(doctor);

                    showDoctorActionConfirmation("actualizado");
                } else {
                    doctorService.addDoctor(doctor);
                    showDoctorActionConfirmation("creado");
                }

                Stage stage = (Stage) firstNameField.getScene().getWindow();
                stage.close();

            } catch (Exception e) {
                Logger.getLogger(CreateDoctorController.class.getName()).log(Level.SEVERE, "Error al procesar doctor", e);
            }
        } else {
            showWarningAlert();
        }
    }

    private void cleanFields() {
        firstNameField.clear();
        lastNameField.clear();
        middleNameField.clear();
        specialtyField.clear();
        licenseNumberField.clear();
        emailField.clear();
    }

    public void close() {
        if (em != null && em.isOpen()) em.close();
        if (emf != null && emf.isOpen()) emf.close();
    }

    public boolean isUserInputValid() {
        if (firstName.isEmpty() || lastName.isEmpty() || middleName.isEmpty() ||
                specialty.isEmpty() || licenseNumber.isEmpty() || email.isEmpty()) {
            showWarningAlert();
            return false;
        }

        if (!NAME_REGEX.matcher(firstName).matches()) {
            showCustomWarningAlert("Formato de Nombre Inválido", "El nombre solo puede contener letras y espacios.");
            return false;
        }

        if (!NAME_REGEX.matcher(lastName).matches()) {
            showCustomWarningAlert("Formato de Apellido Inválido", "El apellido paterno solo puede contener letras y espacios.");
            return false;
        }

        if (!middleName.isEmpty() && !NAME_REGEX.matcher(middleName).matches()) {
            showCustomWarningAlert("Formato de Apellido Inválido", "El apellido materno solo puede contener letras y espacios.");
            return false;
        }

        if (!EMAIL_REGEX.matcher(email).matches()) {
            showCustomWarningAlert("Formato de Email Inválido", "El formato del correo electrónico ingresado no es válido.");
            return false;
        }
        return true;
    }

    private void showDoctorActionConfirmation(String actionMessage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Operación Exitosa");
        alert.setHeaderText(null);
        alert.setContentText("El doctor " + firstName + " " + lastName + " ha sido " + actionMessage + " exitosamente.");
        alert.showAndWait();
    }

    private void showWarningAlert() {
        final String WARNING_MESSAGE = "Rellena todos los campos";
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Advertencia");
        alert.setContentText(WARNING_MESSAGE);
        alert.showAndWait();
    }

    private void showCustomWarningAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}