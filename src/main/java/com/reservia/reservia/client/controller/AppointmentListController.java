package com.reservia.reservia.client.controller;

import com.reservia.reservia.server.model.Appointment;
import com.reservia.reservia.server.model.Doctor;
import com.reservia.reservia.server.model.Patient;
import com.reservia.reservia.server.remote.AppointmentServiceRemote;
import com.reservia.reservia.server.remote.DoctorServiceRemote;
import com.reservia.reservia.server.remote.PatientServiceRemote;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.InputStream;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AppointmentListController {

    private AppointmentServiceRemote appointmentService;
    private DoctorServiceRemote doctorService;
    private PatientServiceRemote patientService;

    @FXML
    private TableView<Appointment> appointmentTable;

    @FXML
    private TableColumn<Appointment, String> colDate;

    @FXML
    private TableColumn<Appointment, String> colTime;

    @FXML
    private TableColumn<Appointment, String> colReason;

    @FXML
    private TableColumn<Appointment, String> colDoctor;

    @FXML
    private TableColumn<Appointment, String> colPatient;

    @FXML
    private TableColumn<Appointment, Void> colEdit;

    @FXML
    private TableColumn<Appointment, Void> colDelete;

    private ObservableList<Appointment> appointments;

    @FXML
    public void initialize() {
        try {
            doctorService = (DoctorServiceRemote) Naming.lookup("DoctorService");
            patientService = (PatientServiceRemote) Naming.lookup("PatientService");
            appointmentService = (AppointmentServiceRemote) Naming.lookup("AppointmentService");
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error initializing RMI services in AppointmentListController", e);
        }


        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colTime.setCellValueFactory(new PropertyValueFactory<>("time"));
        colReason.setCellValueFactory(new PropertyValueFactory<>("reason"));

        colDoctor.setCellValueFactory(cellData -> {
            if (doctorService == null) return new SimpleStringProperty("Servicio Doctor no disp.");
            int doctorId = cellData.getValue().getDoctorId();
            Doctor doctor = null;
            try {
                doctor = doctorService.getDoctorById(doctorId);
            } catch (Exception e) {
                Logger.getLogger(AppointmentListController.class.getName()).log(Level.WARNING, "Error fetching doctor by ID: " + doctorId, e);
                return new SimpleStringProperty("Error Doctor");
            }
            if (doctor != null) {
                String doctorName = doctor.getFirstName() + " " + doctor.getLastName();
                return new SimpleStringProperty(doctorName);
            }
            return new SimpleStringProperty("Doctor N/A");
        });

        colPatient.setCellValueFactory(cellData -> {
            if (patientService == null) return new SimpleStringProperty("Servicio Paciente no disp.");
            int patientId = cellData.getValue().getPatientId();
            Patient patient = null;
            try {
                patient = patientService.getPatientById(patientId);
            } catch (RemoteException e) {
                Logger.getLogger(AppointmentListController.class.getName()).log(Level.WARNING, "Error fetching patient by ID: " + patientId, e);
                return new SimpleStringProperty("Error Paciente");
            }
            if (patient != null) {
                String patientName = patient.getFirstName() + " " + patient.getLastName();
                return new SimpleStringProperty(patientName);
            }
            return new SimpleStringProperty("Paciente N/A");
        });

        addEditButtonToTable();
        addDeleteButtonToTable();
        loadAppointments();
        appointmentTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    protected void loadAppointments() {
        List<Appointment> appointmentList = null;
        if (appointmentService != null) {
            try {
                appointmentList = appointmentService.getAllAppointments();
            } catch (Exception e) {
                Logger.getLogger(AppointmentListController.class.getName()).log(Level.SEVERE, "Error loading appointments from service", e);
            }
        } else {
            Logger.getLogger(AppointmentListController.class.getName()).log(Level.WARNING, "AppointmentService is null, cannot load appointments.");
        }

        if (appointmentList == null) {
            appointmentList = new ArrayList<>();
        }
        appointments = FXCollections.observableArrayList(appointmentList);
        appointmentTable.setItems(appointments);
    }

    private void addDeleteButtonToTable() {
        colDelete.setText("");

        Callback<TableColumn<Appointment, Void>, TableCell<Appointment, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Appointment, Void> call(final TableColumn<Appointment, Void> param) {
                return new TableCell<>() {
                    private final Button btn = new Button();

                    {
                        InputStream iconStream = getClass().getResourceAsStream("/images/basura.png");
                        if (iconStream == null) {
                            System.err.println("APPOINTMENT CTL: ❌ Ícono ELIMINAR NO ENCONTRADO: /images/basura.png");
                            btn.setText("Del");
                        } else {
                            ImageView icon = new ImageView(new Image(iconStream));
                            icon.setFitWidth(16);
                            icon.setFitHeight(16);
                            btn.setGraphic(icon);
                        }
                        btn.getStyleClass().add("delete-icon-button");

                        btn.setOnAction(event -> {
                            Appointment appointment = getTableView().getItems().get(getIndex());
                            if (appointment != null && appointmentService != null) {
                                try {
                                    appointmentService.deleteAppointment(appointment.getAppointmentId());
                                    getTableView().getItems().remove(appointment);
                                } catch (Exception e) {
                                    Logger.getLogger(AppointmentListController.class.getName()).log(Level.SEVERE, "Error deleting appointment", e);
                                }
                            }
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            HBox wrapper = new HBox(btn);
                            wrapper.setAlignment(Pos.CENTER);
                            setGraphic(wrapper);

                        }
                    }
                };
            }
        };
        colDelete.setCellFactory(cellFactory);
    }

    private void addEditButtonToTable() {
        colEdit.setText("");

        Callback<TableColumn<Appointment, Void>, TableCell<Appointment, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Appointment, Void> call(final TableColumn<Appointment, Void> param) {
                return new TableCell<>() {
                    private final Button btn = new Button();

                    {
                        InputStream iconStream = getClass().getResourceAsStream("/images/lapiz.png");
                        if (iconStream == null) {
                            System.err.println("APPOINTMENT CTL: ❌ Ícono EDITAR NO ENCONTRADO: /images/lapiz.png");
                            btn.setText("Edit");
                        } else {
                            ImageView icon = new ImageView(new Image(iconStream));
                            icon.setFitWidth(16);
                            icon.setFitHeight(16);
                            btn.setGraphic(icon);
                        }
                        btn.getStyleClass().add("edit-icon-button");

                        btn.setOnAction(event -> {
                            Appointment appointment = getTableView().getItems().get(getIndex());
                            if (appointment != null) {
                                openEditAppointmentDialog(appointment);
                            }
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            HBox wrapper = new HBox(btn);
                            wrapper.setAlignment(Pos.CENTER);

                            setGraphic(wrapper);

                        }
                    }
                };
            }
        };
        colEdit.setCellFactory(cellFactory);
    }

    private void openEditAppointmentDialog(Appointment appointment) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/reservia/reservia/view/CreateAppointmentView.fxml"));
            Parent root = loader.load();

            CreateAppointmentController controller = loader.getController();
            controller.loadAppointmentDataForEdit(appointment);

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Editar Cita");
            dialogStage.setScene(new Scene(root));
            dialogStage.initModality(Modality.WINDOW_MODAL);
            if (appointmentTable.getScene() != null && appointmentTable.getScene().getWindow() != null) {
                dialogStage.initOwner(appointmentTable.getScene().getWindow());
            } else {
                Logger.getLogger(AppointmentListController.class.getName()).log(Level.WARNING, "Cannot set owner for edit dialog, table scene or window is null.");
            }
            dialogStage.showAndWait();

            loadAppointments();
        } catch (Exception e) {
            Logger.getLogger(AppointmentListController.class.getName()).log(Level.SEVERE, "Error opening edit appointment dialog", e);
        }
    }
}