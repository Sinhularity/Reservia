module com.reservia.reservia {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.reservia.reservia to javafx.fxml;
    exports com.reservia.reservia;
}