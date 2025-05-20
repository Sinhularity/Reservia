package com.reservia.reservia.client.controller;

import com.reservia.reservia.server.model.Doctor;
import com.reservia.reservia.server.remote.DoctorServiceRemote;
import com.reservia.reservia.server.service.DoctorService;
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

import java.rmi.Naming;
import java.util.List;
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
    private TableColumn<Doctor, String> colSpecialty;

    @FXML
    private TableColumn<Doctor, String> colEmail;

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
        colSpecialty.setCellValueFactory(new PropertyValueFactory<>("specialty"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

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
        doctors = FXCollections.observableArrayList(doctorList);
        doctorTable.setItems(doctors);
    }

    private void addDeleteButtonToTable() {
        Callback<TableColumn<Doctor, Void>, TableCell<Doctor, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Doctor, Void> call(final TableColumn<Doctor, Void> param) {
                return new TableCell<>() {
                    private final Button btn = new Button("Eliminar");

                    {
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
