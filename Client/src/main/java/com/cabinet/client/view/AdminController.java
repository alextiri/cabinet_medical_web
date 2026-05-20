package com.cabinet.client.view;

import com.cabinet.client.util.SceneManager;
import com.cabinet.client.util.Session;
import javafx.fxml.FXML;

public class AdminController {
    @FXML
    public void onManageUsersClick() {
        SceneManager.switchScene("admin-users-view.fxml");
    }

    @FXML
    public void onLogoutClick() {
        Session.clear();
        SceneManager.switchScene("login-view.fxml");
    }
}