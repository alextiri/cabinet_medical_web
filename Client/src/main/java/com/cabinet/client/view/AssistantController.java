package com.cabinet.client.view;

import com.cabinet.client.model.User;
import com.cabinet.client.util.LanguageManager;
import com.cabinet.client.util.SceneManager;
import com.cabinet.client.util.Session;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.ResourceBundle;

public class AssistantController {
    @FXML
    private Label welcomeLabel;

    @FXML
    public void initialize() {
        User user = Session.getCurrentUser();

        if (user != null) {
            ResourceBundle bundle = LanguageManager.getBundle();
            welcomeLabel.setText(bundle.getString("common.welcome")
                    + ", "
                    + Session.getCurrentUser().getLastName()
                    + " "
                    + Session.getCurrentUser().getFirstName()
            );
        }
    }

    @FXML
    protected void onPatientsClick() {
        SceneManager.switchScene("assistant-patients-view.fxml");
    }
    @FXML
    protected void onAppointmentsClick() {
        SceneManager.switchScene("assistant-appointments-view.fxml");
    }
    @FXML
    protected void onStatisticsClick() {
        SceneManager.switchScene("assistant-statistics-view.fxml");
    }
    @FXML
    protected void onLogoutClick() {
        Session.clear();
        SceneManager.switchScene("login-view.fxml");
    }
}