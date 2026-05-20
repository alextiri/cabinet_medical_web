package com.cabinet.client.presenter;

import com.cabinet.client.model.Patient;

import java.util.List;

public interface IPatientView {
    void showPatients(List<Patient> patients);
    void showError(String message);
}