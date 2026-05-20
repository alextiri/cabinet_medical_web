package com.cabinet.client.presenter;

import com.cabinet.client.model.Patient;
import com.cabinet.client.service.PatientService;

public class PatientPresenter {
    private final PatientService patientService;
    private final IPatientView view;

    public PatientPresenter(IPatientView view) {
        this.view = view;
        patientService = new PatientService();
    }

    public void createPatient(Patient patient) {
        patientService.createPatient(patient)
                .thenAccept(createdPatient -> {
                    if (createdPatient != null) {
                        loadAllPatients();
                    } else {
                        view.showError("Could not create patient");
                    }
                });
    }

    public void updatePatient(Patient patient) {
        patientService.updatePatient(patient)
                .thenAccept(updatedPatient -> {
                    if (updatedPatient != null) {
                        loadAllPatients();
                    } else {
                        view.showError("Could not update patient");
                    }
                });
    }

    public void deletePatient(Integer patientId) {
        patientService.deletePatient(patientId)
                .thenRun(() -> {
                    loadAllPatients();
                })
                .exceptionally(ex -> {
                    ex.printStackTrace();
                    view.showError("Could not delete patient");

                    return null;
                });
    }

    public void loadAllPatients() {
        patientService.getAllPatients()
                .thenAccept(patients -> {
                    if (patients == null) {
                        view.showError("Could not load patients");
                        return;
                    }
                    view.showPatients(patients);
                })
                .exceptionally(ex -> {
                    ex.printStackTrace();
                    view.showError("Server error");

                    return null;
                });
    }

    public void loadPatientsByDoctor(Integer doctorId) {
        patientService.getPatients(doctorId)
                .thenAccept(patients -> {
                    if (patients == null) {
                        view.showError("Could not load patients");
                        return;
                    }
                    view.showPatients(patients);
                })
                .exceptionally(ex -> {
                    ex.printStackTrace();
                    view.showError("Server error");

                    return null;
                });
    }
}