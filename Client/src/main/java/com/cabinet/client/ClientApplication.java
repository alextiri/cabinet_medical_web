package com.cabinet.client;

import com.cabinet.client.util.LanguageManager;
import com.cabinet.client.util.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class ClientApplication extends Application {
    @Override
    public void start(Stage stage) {
        SceneManager.setStage(stage);
        LanguageManager.setLanguage("RO");
        stage.setTitle("Medical Cabinet");

        SceneManager.switchScene("login-view.fxml");
    }
}