package com.cabinet.client.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {
    private static Stage stage;

    public static void setStage(Stage primaryStage) {
        stage = primaryStage;
    }

    public static void switchScene(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(
                            SceneManager.class.getResource("/com/cabinet/client/" + fxmlFile),
                            LanguageManager.getBundle()
                    );

            Scene scene = new Scene(
                            loader.load(),
                            1400,
                            900
                    );

            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}