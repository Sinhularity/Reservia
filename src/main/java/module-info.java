module com.reservia.reservia {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;
    requires java.logging;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;

    opens com.reservia.reservia to javafx.fxml;
    exports com.reservia.reservia;

    // Now fxml files can access to the classes inside the package
    opens com.reservia.reservia.controller to javafx.fxml;
    exports com.reservia.reservia.controller;

    opens com.reservia.reservia.model to javafx.base, org.hibernate.orm.core;
    exports com.reservia.reservia.model;

    opens com.reservia.reservia.repository to javafx.fxml;
    exports com.reservia.reservia.repository;

    opens com.reservia.reservia.service to javafx.fxml;
    exports com.reservia.reservia.service;
}