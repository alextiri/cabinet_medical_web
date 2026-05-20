package com.cabinet.client.view;

import com.cabinet.client.model.User;
import com.cabinet.client.presenter.ILoginView;
import com.cabinet.client.presenter.LoginPresenter;

import com.cabinet.client.util.LanguageManager;
import com.cabinet.client.util.SceneManager;
import com.cabinet.client.util.Session;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;

import java.util.Locale;

public class LoginController implements ILoginView {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label messageLabel;
    @FXML
    private ComboBox<String> languageCombo;

    private LoginPresenter presenter;

    @FXML
    public void initialize() {
        presenter = new LoginPresenter(this);
        languageCombo.getItems().addAll(
                "English",
                "Română",
                "Deutsch"
        );

        Locale locale = LanguageManager.getCurrentLocale();

        if (locale.getLanguage().equals("ro")) {
            languageCombo.setValue("Română");
        } else if (locale.getLanguage().equals("de")) {
            languageCombo.setValue("Deutsch");
        } else {
            languageCombo.setValue("English");
        }
    }
    @FXML
    protected void onLoginClick() {
        messageLabel.setText("");
        presenter.login(usernameField.getText(), passwordField.getText());
    }

    @Override
    public void showError(String message) {
        Platform.runLater(() -> {
            messageLabel.setText(message);
        });
    }

    @Override
    public void openDashboard(User user) {
        Platform.runLater(() -> {
            try {
                Session.setCurrentUser(user);
                String fxmlFile;

                switch (user.getRole()) {
                    case DOCTOR:
                        fxmlFile = "doctor-dashboard-view.fxml";
                        break;
                    case ASSISTANT:
                        fxmlFile = "assistant-dashboard-view.fxml";
                        break;
                    case ADMIN:
                        fxmlFile = "admin-dashboard-view.fxml";
                        break;
                    default:
                        showError("Unknown role");
                        return;
                }

                SceneManager.switchScene(fxmlFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    protected void onLanguageChange() {
        String language = languageCombo.getValue();

        switch (language) {
            case "Română":
                LanguageManager.setLanguage("RO");
                break;
            case "Deutsch":
                LanguageManager.setLanguage("DE");
                break;
            default:
                LanguageManager.setLanguage("EN");
        }

        SceneManager.switchScene("login-view.fxml");
    }
}