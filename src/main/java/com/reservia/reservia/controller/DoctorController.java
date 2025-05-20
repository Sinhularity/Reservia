package com.reservia.reservia.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Pagination;

import java.io.IOException;
import java.util.logging.Logger;

public class DoctorController {

    @FXML
    private Pagination doctorPagination;

    private Node createDoctorView;
    private Node doctorListView;

    @FXML
    public void initialize() {

        try {
            FXMLLoader createDoctorLoader = new FXMLLoader(getClass().getResource("/com/reservia/reservia/view/CreateDoctorView.fxml"));
            createDoctorView = createDoctorLoader.load();

            FXMLLoader doctorListLoader = new FXMLLoader(getClass().getResource("/com/reservia/reservia/view/DoctorListView.fxml"));
            doctorListView = doctorListLoader.load();

        } catch (IOException e) {
            Logger.getLogger(DoctorController.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
        }

        doctorPagination.setPageCount(2);
        doctorPagination.setCurrentPageIndex(0);
        doctorPagination.setPageFactory(this::createPage);
    }

    private Node createPage(int pageIndex) {
        switch (pageIndex) {
            case 0:
                return createDoctorView;
            case 1:
                return doctorListView;
            default:
                return null;
        }
    }
}