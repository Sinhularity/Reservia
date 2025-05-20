package com.reservia.reservia.controller;

import com.reservia.reservia.model.Doctor;
import com.reservia.reservia.service.DoctorService;
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

public class DoctorListController {

    private DoctorService doctorService;

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

    @FXML
    public void initialize() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("reserviaPU");
        EntityManager em = emf.createEntityManager();
        doctorService = new DoctorService(em);

        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colMiddleName.setCellValueFactory(new PropertyValueFactory<>("middleName"));
        colSpecialty.setCellValueFactory(new PropertyValueFactory<>("specialty"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        addDeleteButtonToTable();
        loadDoctors();
    }

    private void loadDoctors() {
        List<Doctor> doctorList = doctorService.findAllDoctors();
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
                            doctorService.deleteDoctor(doctor.getDoctorId());
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
