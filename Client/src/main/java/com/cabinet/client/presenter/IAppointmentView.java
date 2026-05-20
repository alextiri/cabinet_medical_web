package com.cabinet.client.presenter;

import com.cabinet.client.model.Appointment;
import java.util.List;

public interface IAppointmentView {
    void showAppointments(List<Appointment> appointments);
    void showError(String message);
}