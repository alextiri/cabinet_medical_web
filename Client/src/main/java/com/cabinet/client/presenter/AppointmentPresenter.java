package com.cabinet.client.presenter;

import com.cabinet.client.model.Appointment;
import com.cabinet.client.service.AppointmentService;

public class AppointmentPresenter {
    private final AppointmentService appointmentService;
    private final IAppointmentView view;

    public AppointmentPresenter(IAppointmentView view) {
        this.view = view;
        appointmentService = new AppointmentService();
    }

    public void loadAppointments(Integer doctorId) {
        appointmentService.getAppointments(doctorId).thenAccept(appointments -> {
            if (appointments == null) {
                view.showError("Could not load appointments");
                return;
            }
            view.showAppointments(appointments);
        }).exceptionally(ex -> {
            ex.printStackTrace();
            view.showError("Server error");
            return null;
        });
    }

    public void loadAllAppointments() {
        appointmentService.getAllAppointments().thenAccept(appointments -> {
                    if (appointments == null) {
                        view.showError("Could not load appointments");
                        return;
                    }
                    view.showAppointments(appointments);
                }).exceptionally(ex -> {
                    ex.printStackTrace();
                    view.showError("Server error");
                    return null;
                });
    }

    public void createAppointment(Appointment appointment) {
        appointmentService.createAppointment(appointment)
                .thenAccept(saved -> {
                    loadAppointments(appointment.getDoctorId());
                })
                .exceptionally(ex -> {
                    ex.printStackTrace();
                    view.showError("Failed to create appointment");
                    return null;
                });
    }

    public void updateAppointment(Appointment appointment) {
        appointmentService.updateAppointment(appointment)
                .thenAccept(updated -> {
                    loadAppointments(appointment.getDoctorId());
                })
                .exceptionally(ex -> {
                    ex.printStackTrace();
                    view.showError("Failed to update appointment");
                    return null;
                });
    }

    public void deleteAppointment(Integer id) {
        appointmentService.deleteAppointment(id)
                .thenRun(this::loadAllAppointments)
                .exceptionally(ex -> {
                    ex.printStackTrace();
                    view.showError("Failed to delete appointment");
                    return null;
                });
    }
}