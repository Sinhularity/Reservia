package com.reservia.reservia.client.controller;

import com.reservia.reservia.server.model.Patient;
import com.reservia.reservia.server.remote.PatientServiceRemote;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import javax.swing.text.html.ImageView;
import java.awt.*;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PatientListController {

    private PatientServiceRemote patientService;

    @FXML
    private TableView<Patient> patientTable;

    @FXML
    private TableColumn<Patient, String> colFirstName;

    @FXML
    private TableColumn<Patient, String> colLastName;

    @FXML
    private TableColumn<Patient, String> colMiddleName;

    @FXML
    private TableColumn<Patient, String> colCurp;

    @FXML
    private TableColumn<Patient, String> colPhone;

    @FXML
    private TableColumn<Patient, String> colEmail;

    @FXML
    private TableColumn<Patient, Void> editColumn;

    @FXML
    private TableColumn<Patient, Void> deleteColumn;

    private ObservableList<Patient> patients;

    @FXML
    public void initialize() {
        try {
            patientService = (PatientServiceRemote) Naming.lookup("PatientService");
        } catch (Exception e) {
            Logger.getLogger(PatientListController.class.getName()).log(Level.SEVERE, null, e);
        }

        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colMiddleName.setCellValueFactory(new PropertyValueFactory<>("middleName"));
        colCurp.setCellValueFactory(new PropertyValueFactory<>("curp"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        addEditButtonToTable();
        addDeleteButtonToTable();
        try {
            loadPatients();
        } catch (RemoteException e) {
            Logger.getLogger(PatientListController.class.getName()).log(Level.SEVERE, "Error al cargar pacientes", e);
            showErrorAlert("Error de Carga", "No se pudieron cargar los datos de los pacientes.");
        }
    }

    protected void loadPatients() throws RemoteException {
        List<Patient> patientList = null;
        if (patientService != null) {
            patientList = patientService.getAllPatients();
        }
        if (patientList == null) {
            patientList = new ArrayList<>();
        }
        patients = FXCollections.observableArrayList(patientList);
        patientTable.setItems(patients);
    }

    private void addEditButtonToTable() {
        editColumn.setText("");

        Callback<TableColumn<Patient, Void>, TableCell<Patient, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Patient, Void> call(final TableColumn<Patient, Void> param) {
                return new TableCell<>() {
                    private final Button btn = new Button();

                    {
                        javafx.scene.image.ImageView icon = new javafx.scene.image.ImageView(
                                new javafx.scene.image.Image(getClass().getResourceAsStream("/images/lapiz.png"))
                        );
                        icon.setFitWidth(16);
                        icon.setFitHeight(16);
                        btn.setMaxSize(30, 30);
                        btn.setGraphic(icon);
                        btn.getStyleClass().add("edit-icon-button");

                        btn.setOnAction(event -> {
                            Patient patient = getTableView().getItems().get(getIndex());
                            if (patient != null) {
                                openEditPatientDialog(patient);
                            }
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            javafx.scene.layout.HBox wrapper = new javafx.scene.layout.HBox(btn);
                            wrapper.setAlignment(javafx.geometry.Pos.CENTER);
                            wrapper.setStyle("-fx-background-color: transparent;");
                            setGraphic(wrapper);
                            setStyle("-fx-background-color: transparent;");
                        }
                    }
                };
            }
        };
        editColumn.setCellFactory(cellFactory);
    }


    private void openEditPatientDialog(Patient patient) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/reservia/reservia/view/CreatePatientView.fxml"));
            Parent root = loader.load();

            CreatePatientController controller = loader.getController();
            controller.loadPatientDataForEdit(patient);

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Editar Paciente");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            if (patientTable.getScene() != null && patientTable.getScene().getWindow() != null) {
                dialogStage.initOwner(patientTable.getScene().getWindow());
            }
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);
            dialogStage.showAndWait();

            loadPatients();

        } catch (IOException | NullPointerException e) {
            Logger.getLogger(PatientListController.class.getName()).log(Level.SEVERE, "Error al abrir diálogo de edición de paciente", e);
            showErrorAlert("Error de Aplicación", "No se pudo abrir la ventana para editar el paciente.");
        }
    }

    private void addDeleteButtonToTable() {
        deleteColumn.setText("");

        Callback<TableColumn<Patient, Void>, TableCell<Patient, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Patient, Void> call(final TableColumn<Patient, Void> param) {
                return new TableCell<>() {
                    private final Button btn = new Button();

                    {
                        javafx.scene.image.ImageView icon = new javafx.scene.image.ImageView(
                                new javafx.scene.image.Image(getClass().getResourceAsStream("/images/basura.png"))
                        );
                        icon.setFitWidth(16);
                        icon.setFitHeight(16);
                        btn.setMaxSize(30, 30);
                        btn.setGraphic(icon);
                        btn.getStyleClass().add("delete-icon-button");

                        btn.setOnAction(event -> {
                            Patient patient = getTableView().getItems().get(getIndex());
                            if (patient != null && patientService != null) {
                                try {
                                    patientService.deletePatient(patient.getPatientId());
                                    getTableView().getItems().remove(patient);
                                } catch (Exception e) {
                                    Logger.getLogger(PatientListController.class.getName()).log(Level.SEVERE, null, e);
                                }
                            }
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            javafx.scene.layout.HBox wrapper = new javafx.scene.layout.HBox(btn);
                            wrapper.setAlignment(javafx.geometry.Pos.CENTER);
                            wrapper.setStyle("-fx-background-color: transparent;");
                            setGraphic(wrapper);
                            setStyle("-fx-background-color: transparent;");
                        }
                    }
                };
            }
        };
        deleteColumn.setCellFactory(cellFactory);
    }



    private void showErrorAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}