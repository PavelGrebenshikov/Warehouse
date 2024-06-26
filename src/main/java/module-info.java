module com.example.warehouse {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens com.example.warehouse to javafx.fxml;
    exports com.example.warehouse;
}