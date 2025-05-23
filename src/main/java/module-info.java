module com.reservia.reservia {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.logging;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires java.rmi;
    requires javafx.graphics;

    opens com.reservia.reservia to javafx.fxml;
    exports com.reservia.reservia;

    // Now fxml files can access to the classes inside the package
    opens com.reservia.reservia.client.controller to javafx.fxml;
    exports com.reservia.reservia.client.controller;

    opens com.reservia.reservia.server.model to javafx.base, org.hibernate.orm.core;
    exports com.reservia.reservia.server.model;

    opens com.reservia.reservia.server.repository to javafx.fxml;
    exports com.reservia.reservia.server.repository;

    opens com.reservia.reservia.server.service to javafx.fxml;
    exports com.reservia.reservia.server.service;

    opens com.reservia.reservia.server.remote to javafx.fxml;
    exports com.reservia.reservia.server.remote;

    opens com.reservia.reservia.server.server to javafx.fxml;
    exports com.reservia.reservia.server.server;
}