package com.cabinet.client.presenter;

import com.cabinet.client.model.User;
import com.cabinet.client.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.application.Platform;


public class LoginPresenter {
    private final AuthService authService;
    private final ILoginView view;

    public LoginPresenter(ILoginView view) {
        this.view = view;
        authService = new AuthService();
    }

    public void login(String username, String password) {
        authService.login(username, password)
                .thenAccept(response -> {
                    try {
                        if (response.isBlank()) {
                            Platform.runLater(() -> view.showError("Invalid credentials"));
                            return;
                        }

                        ObjectMapper mapper = new ObjectMapper();
                        User user = mapper.readValue(response, User.class);

                        Platform.runLater(() -> view.openDashboard(user));
                    } catch (Exception e) {
                        e.printStackTrace();
                        Platform.runLater(() -> view.showError("Login failed"));
                    }
                }).exceptionally(ex -> {
                    ex.printStackTrace();
                    Platform.runLater(() -> view.showError("Server error"));

                    return null;
                });
    }
}