package com.reservia.reservia.client.controller;

import com.reservia.reservia.server.model.Doctor;
import com.reservia.reservia.server.remote.DoctorServiceRemote;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.rmi.Naming;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DoctorListController {

    @FXML
    private TableView<Doctor> doctorTable;

    @FXML
    private TableColumn<Doctor, String> colFirstName;

    @FXML
    private TableColumn<Doctor, String> colLastName;

    @FXML
    private TableColumn<Doctor, String> colMiddleName;

    @FXML
    private TableColumn<Doctor, String> colLicenseNumber;

    @FXML
    private TableColumn<Doctor, String> colSpecialty;

    @FXML
    private TableColumn<Doctor, String> colEmail;

    @FXML
    private TableColumn<Doctor, Void> editColumn;

    @FXML
    private TableColumn<Doctor, Void> deleteColumn;

    private ObservableList<Doctor> doctors;

    private DoctorServiceRemote doctorService;

    @FXML
    public void initialize() {
        try {
            doctorService = (DoctorServiceRemote) Naming.lookup("DoctorService");
        } catch (Exception e) {
            Logger.getLogger(PatientListController.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
        }

        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colMiddleName.setCellValueFactory(new PropertyValueFactory<>("middleName"));
        colLicenseNumber.setCellValueFactory(new PropertyValueFactory<>("licenseNumber"));
        if (colSpecialty != null) {
            colSpecialty.setCellValueFactory(new PropertyValueFactory<>("specialty"));
        }
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        addEditButtonToTable();
        addDeleteButtonToTable();
        loadDoctors();
    }

    protected void loadDoctors() {
        List<Doctor> doctorList = null;
        try {
            doctorList = doctorService.getAllDoctors();
        } catch (Exception e) {
            Logger.getLogger(DoctorListController.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
        }
        if (doctorList == null) {
            doctorList = new ArrayList<>();
        }
        doctors = FXCollections.observableArrayList(doctorList);
        doctorTable.setItems(doctors);
    }

    private void showErrorAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void addDeleteButtonToTable() {
        deleteColumn.setText("");

        Callback<TableColumn<Doctor, Void>, TableCell<Doctor, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Doctor, Void> call(final TableColumn<Doctor, Void> param) {
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
                            Doctor doctor = getTableView().getItems().get(getIndex());
                            doctors.remove(doctor);
                            try {
                                doctorService.deleteDoctor(doctor.getDoctorId());
                            } catch (Exception e) {
                                Logger.getLogger(DoctorListController.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
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
                            wrapper.setPrefWidth(Region.USE_COMPUTED_SIZE);
                            setGraphic(wrapper);
                            setStyle("-fx-background-color: transparent;");
                        }
                    }
                };
            }
        };
        deleteColumn.setCellFactory(cellFactory);
    }

    private void addEditButtonToTable() {
        editColumn.setText("");

        Callback<TableColumn<Doctor, Void>, TableCell<Doctor, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Doctor, Void> call(final TableColumn<Doctor, Void> param) {
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
                            Doctor doctor = getTableView().getItems().get(getIndex());
                            openEditDoctorDialog(doctor);
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

    private void openEditDoctorDialog(Doctor doctor) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/reservia/reservia/view/CreateDoctorView.fxml"));
            Parent root = loader.load();

            CreateDoctorController controller = loader.getController();
            controller.loadDoctorDataForEdit(doctor);

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Editar Doctor");
            dialogStage.initModality(Modality.WINDOW_MODAL);

            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            dialogStage.showAndWait();

            loadDoctors();

        } catch (IOException e) {
            Logger.getLogger(DoctorListController.class.getName()).log(Level.SEVERE, "Error al abrir diálogo de edición", e);
            showErrorAlert("Error de Carga", "No se pudo abrir la ventana de edición.");
        }
    }


}
