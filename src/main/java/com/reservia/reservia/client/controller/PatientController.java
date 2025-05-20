package com.reservia.reservia.client.controller;

import com.reservia.reservia.server.remote.PatientServiceRemote;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Pagination;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.logging.Logger;

public class PatientController {

    private PatientServiceRemote patientService;

    @FXML
    private Pagination patientPagination;

    private Node createPatientView;
    private Node patientListView;

    private PatientListController patientListController;

    @FXML
    public void initialize() {

        try {
            FXMLLoader createPatientLoader = new FXMLLoader(getClass().getResource("/com/reservia/reservia/view/CreatePatientView.fxml"));
            createPatientView = createPatientLoader.load();


            FXMLLoader patientListLoader = new FXMLLoader(getClass().getResource("/com/reservia/reservia/view/PatientListView.fxml"));
            patientListView = patientListLoader.load();
            patientListController = patientListLoader.getController();

        } catch (IOException e) {
            Logger.getLogger(PatientController.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
        }

        patientPagination.setPageCount(2);
        patientPagination.setCurrentPageIndex(0);

        patientPagination.setPageFactory(this::createPage);
    }

    private Node createPage(int pageIndex) {
        switch (pageIndex) {
            case 0:
                return createPatientView;
            case 1:
                try {
                    patientListController.loadPatients();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
                return patientListView;
            default:
                return null;
        }
    }
}
