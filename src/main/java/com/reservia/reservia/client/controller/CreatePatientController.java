package com.reservia.reservia.client.controller;

import com.reservia.reservia.server.model.Patient;
import com.reservia.reservia.server.remote.PatientServiceRemote;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import java.rmi.Naming;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.stage.Stage;

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

    private PatientServiceRemote patientService;
    private boolean editMode = false;
    private int patientIdToEdit;

    private static final Pattern NAME_REGEX = Pattern.compile("^[\\p{L} .'-]+$");
    private static final Pattern EMAIL_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private static final Pattern CURP_REGEX = Pattern.compile("[A-Z]{1}[AEIOU]{1}[A-Z]{2}[0-9]{2}(0[1-9]|1[0-2])(0[1-9]|[12][0-9]|3[01])[HM]{1}(AS|BC|BS|CC|CS|CH|CL|CM|DF|DG|GT|GR|HG|JC|MC|MN|MS|NT|NL|OC|PL|QT|QR|SP|SL|SR|TC|TS|TL|VZ|YN|ZS|NE)[B-DF-HJ-NP-TV-Z]{3}[0-9A-Z]{1}[0-9]{1}");
    private static final Pattern PHONE_REGEX = Pattern.compile("^\\d{10}$");

    @FXML
    public void initialize() {
        try {
            patientService = (PatientServiceRemote) Naming.lookup("PatientService");
        } catch (Exception e) {
            Logger.getLogger(CreatePatientController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @FXML
    private void savePatient() {
        firstName = firstNameField.getText().trim();
        lastName = lastNameField.getText().trim();
        middleName = middleNameField.getText().trim();
        curp = curpField.getText().trim().toUpperCase();
        phone = phoneField.getText().trim();
        email = emailField.getText().trim();

        String validationMessage = getUserInputValidationMessage();
        if (validationMessage.isEmpty()) {
            Patient patient = new Patient(firstName, lastName, middleName, curp, phone, email);
            try {
                if (editMode) {
                    patient.setPatientId(patientIdToEdit);
                    patientService.updatePatient(patient);
                    showPatientActionConfirmation("actualizado");
                } else {
                    patientService.addPatient(patient);
                    showPatientActionConfirmation("creado");
                }

                Stage stage = (Stage) firstNameField.getScene().getWindow();
                stage.close();

            } catch (Exception e) {
                Logger.getLogger(CreatePatientController.class.getName()).log(Level.SEVERE, "Error al procesar paciente", e);
                showErrorAlert("Error al Guardar", "No se pudo " + (editMode ? "actualizar" : "crear") + " el paciente: " + e.getMessage());
            }
        } else {
            showWarningAlert("Datos Inválidos", validationMessage);
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

    private String getUserInputValidationMessage() {
        StringBuilder errors = new StringBuilder();

        if (firstName.isEmpty()) {
            errors.append("El nombre no puede estar vacío.\n");
        } else if (!NAME_REGEX.matcher(firstName).matches()) {
            errors.append("El nombre solo puede contener letras, espacios y los caracteres .'- \n");
        }

        if (lastName.isEmpty()) {
            errors.append("El apellido paterno no puede estar vacío.\n");
        } else if (!NAME_REGEX.matcher(lastName).matches()) {
            errors.append("El apellido paterno solo puede contener letras, espacios y los caracteres .'- \n");
        }

        if (middleName.isEmpty()) {
            errors.append("El apellido materno no puede estar vacío.\n");
        } else if (!NAME_REGEX.matcher(middleName).matches()) {
            errors.append("El apellido materno solo puede contener letras, espacios y los caracteres .'- \n");
        }

        if (curp.isEmpty()) {
            errors.append("El CURP no puede estar vacío.\n");
        } else if (!CURP_REGEX.matcher(curp).matches()) {
            errors.append("El formato del CURP es incorrecto.\n");
        }

        if (phone.isEmpty()) {
            errors.append("El teléfono no puede estar vacío.\n");
        } else if (!PHONE_REGEX.matcher(phone).matches()) {
            errors.append("El formato del teléfono es incorrecto (debe contener 10 dígitos).\n");
        }

        if (email.isEmpty()) {
            errors.append("El email no puede estar vacío.\n");
        } else if (!EMAIL_REGEX.matcher(email).matches()) {
            errors.append("El formato del email es incorrecto.\n");
        }

        return errors.toString().trim();
    }

    private void showPatientActionConfirmation(String actionMessage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Operación Exitosa");
        alert.setHeaderText(null);
        alert.setContentText("Paciente " + firstName + " " + lastName + " ha sido " + actionMessage + " exitosamente.");
        alert.showAndWait();
    }

    private void showWarningAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showErrorAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    public void loadPatientDataForEdit(Patient patient) {
        if (patient == null) {
            showErrorAlert("Error Interno", "No se recibió información del paciente para editar.");
            return;
        }
        firstNameField.setText(patient.getFirstName());
        lastNameField.setText(patient.getLastName());
        middleNameField.setText(patient.getMiddleName() != null ? patient.getMiddleName() : "");
        curpField.setText(patient.getCurp());
        phoneField.setText(patient.getPhone());
        emailField.setText(patient.getEmail());

        this.editMode = true;
        this.patientIdToEdit = patient.getPatientId();
    }
}