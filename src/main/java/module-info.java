module com.reservia.reservia {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;
    requires java.logging;


    opens com.reservia.reservia to javafx.fxml;
    exports com.reservia.reservia;

    // Now fxml files can access the controller
    opens com.reservia.reservia.controller to javafx.fxml;
    exports com.reservia.reservia.controller;
}