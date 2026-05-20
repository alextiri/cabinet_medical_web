package com.cabinet.client.view;

import com.cabinet.client.model.Appointment;
import com.cabinet.client.model.Consultation;
import com.cabinet.client.service.AppointmentService;
import com.cabinet.client.service.ConsultationService;
import com.cabinet.client.service.MedicalRecordService;
import com.cabinet.client.util.AppointmentStatus;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class ConsultationDialogController {
    private Appointment appointment;

    @FXML
    private TextArea symptomsArea;
    @FXML
    private TextArea diagnosisArea;
    @FXML
    private TextArea treatmentArea;
    @FXML
    private TextArea observationsArea;

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    @FXML
    private void onSaveClick() {
        MedicalRecordService medicalRecordService = new MedicalRecordService();

        medicalRecordService.getMedicalRecordByPatientId(appointment.getPatientId())
                .thenAccept(medicalRecord -> {
                    if (medicalRecord == null) {
                        Platform.runLater(() -> {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setContentText("Medical record not found.");

                            alert.showAndWait();
                        });

                        return;
                    }

                    Consultation consultation = new Consultation();

                    consultation.setMedicalRecordId(medicalRecord.getId());
                    consultation.setDoctorId(appointment.getDoctorId());
                    consultation.setSymptoms(symptomsArea.getText());
                    consultation.setDiagnosis(diagnosisArea.getText());
                    consultation.setTreatment(treatmentArea.getText());
                    consultation.setObservations(observationsArea.getText());
                    consultation.setDate(appointment.getDate());

                    ConsultationService consultationService = new ConsultationService();
                    consultationService.createConsultation(consultation)
                            .thenAccept(savedConsultation -> {
                                if (savedConsultation == null) {
                                    Platform.runLater(() -> {
                                        Alert alert = new Alert(Alert.AlertType.ERROR);
                                        alert.setContentText("Failed to save consultation.");
                                        alert.showAndWait();
                                    });

                                    return;
                                }

                                appointment.setStatus(AppointmentStatus.COMPLETED);

                                AppointmentService appointmentService = new AppointmentService();
                                appointmentService.updateAppointment(appointment)
                                        .thenRun(() -> {
                                            Platform.runLater(() -> {
                                                Stage stage = (Stage) symptomsArea
                                                        .getScene()
                                                        .getWindow();
                                                stage.close();
                                            });
                                        });
                            });
                });
    }

    @FXML
    private void onCancelClick() {
        symptomsArea
                .getScene()
                .getWindow()
                .hide();
    }
}