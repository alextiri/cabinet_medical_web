package com.cabinet.client.view;

import com.cabinet.client.model.User;
import com.cabinet.client.util.LanguageManager;
import com.cabinet.client.util.SceneManager;
import com.cabinet.client.util.Session;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.ResourceBundle;

public class DoctorController {
    @FXML
    private Label welcomeLabel;

    @FXML
    public void initialize() {
        User loggedUser = Session.getCurrentUser();

        if (loggedUser != null) {
            ResourceBundle bundle = LanguageManager.getBundle();
            welcomeLabel.setText(bundle.getString("common.welcome")
                            + ", Dr. "
                            + Session.getCurrentUser().getLastName()
            );
        }
    }

    @FXML
    protected void onAppointmentsClick() {
        try {
            SceneManager.switchScene("doctor-schedule-view.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onPatientsClick() {
        try {
            SceneManager.switchScene("doctor-patients-view.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onLogoutClick() {
        Session.clear();
        SceneManager.switchScene("login-view.fxml");
    }
}