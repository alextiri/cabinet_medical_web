module com.cabinet.client {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.dataformat.xml;

    opens com.cabinet.client to javafx.fxml;
    opens com.cabinet.client.view to javafx.fxml;

    exports com.cabinet.client;
    exports com.cabinet.client.view;
    exports com.cabinet.client.model;
    exports com.cabinet.client.dto;
    opens com.cabinet.client.model to com.fasterxml.jackson.databind, javafx.fxml;
    opens com.cabinet.client.dto to com.fasterxml.jackson.databind;
    opens com.cabinet.client.util to com.fasterxml.jackson.databind;
    exports com.cabinet.client.presenter;
    opens com.cabinet.client.presenter to javafx.fxml;
}