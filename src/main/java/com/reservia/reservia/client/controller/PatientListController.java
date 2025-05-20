package com.reservia.reservia.client.controller;

import com.reservia.reservia.server.model.Patient;
import com.reservia.reservia.server.remote.PatientServiceRemote;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.List;
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
    private TableColumn<Patient, Void> deleteColumn;


    private ObservableList<Patient> patients;

    @FXML
    public void initialize() {

        try {
            patientService = (PatientServiceRemote) Naming.lookup("PatientService");
        } catch (Exception e) {
            Logger.getLogger(PatientListController.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
        }

        // Configure columns based on Patient class
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colMiddleName.setCellValueFactory(new PropertyValueFactory<>("middleName"));
        colCurp.setCellValueFactory(new PropertyValueFactory<>("curp"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));


        addDeleteButtonToTable();
        try {
            loadPatients();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    protected void loadPatients() throws RemoteException {
        List<Patient> patientList = patientService.getAllPatients();
        patients = FXCollections.observableArrayList(patientList);
        patientTable.setItems(patients);
    }

    protected void addDeleteButtonToTable() {
        Callback<TableColumn<Patient, Void>, TableCell<Patient, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Patient, Void> call(final TableColumn<Patient, Void> param) {
                return new TableCell<>() {
                    private final Button btn = new Button("Eliminar");

                    {
                        btn.setOnAction(event -> {
                            Patient patient = getTableView().getItems().get(getIndex());
                            patients.remove(patient);
                            try {
                                patientService.deletePatient(patient.getPatientId());
                            } catch (RemoteException e) {
                                throw new RuntimeException(e);
                            }
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
