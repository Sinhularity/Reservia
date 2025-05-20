package com.reservia.reservia.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Pagination;

import java.io.IOException;
import java.util.logging.Logger;

public class PatientController {

    @FXML
    private Pagination patientPagination;

    private Node createPatientView;
    private Node patientListView;

    @FXML
    public void initialize() {

        try {
            FXMLLoader createPatientLoader = new FXMLLoader(getClass().getResource("/com/reservia/reservia/view/CreatePatientView.fxml"));
            createPatientView = createPatientLoader.load();


            FXMLLoader patientListLoader = new FXMLLoader(getClass().getResource("/com/reservia/reservia/view/PatientListView.fxml"));
            patientListView = patientListLoader.load();


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
                return patientListView;
            default:
                return null;
        }
    }
}
