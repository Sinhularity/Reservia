package com.reservia.reservia.controller;

import com.reservia.reservia.model.Patient;
import com.reservia.reservia.service.PatientService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.util.List;

public class PatientListController {

    private PatientService patientService;

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
    private TableColumn<Patient, Void> deleteColumn;


    private ObservableList<Patient> patients;

    @FXML
    public void initialize() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("reserviaPU");
        EntityManager em = emf.createEntityManager();
        patientService = new PatientService(em);

        // Configure columns based on Patient class
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colMiddleName.setCellValueFactory(new PropertyValueFactory<>("middleName"));
        colCurp.setCellValueFactory(new PropertyValueFactory<>("curp"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));


        addDeleteButtonToTable();
        loadPatients();
    }

    private void loadPatients() {
        List<Patient> patientList = patientService.findAllPatients();
        patients = FXCollections.observableArrayList(patientList);
        patientTable.setItems(patients);
    }

    private void addDeleteButtonToTable() {
        Callback<TableColumn<Patient, Void>, TableCell<Patient, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Patient, Void> call(final TableColumn<Patient, Void> param) {
                return new TableCell<>() {
                    private final Button btn = new Button("Eliminar");

                    {
                        btn.setOnAction(event -> {
                            Patient patient = getTableView().getItems().get(getIndex());
                            patients.remove(patient);
                            patientService.deletePatient(patient);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
            }
        };

        deleteColumn.setCellFactory(cellFactory);
    }
}
