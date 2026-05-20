package com.cabinet.client.presenter;

import com.cabinet.client.model.User;

public interface ILoginView {
    void showError(String message);
    void openDashboard(User user);
}