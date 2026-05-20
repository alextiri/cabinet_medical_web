package com.cabinet.client.view;

import com.cabinet.client.model.Appointment;
import com.cabinet.client.model.Doctor;
import com.cabinet.client.model.Patient;
import com.cabinet.client.model.User;
import com.cabinet.client.service.AppointmentService;
import com.cabinet.client.service.DoctorService;
import com.cabinet.client.service.PatientService;
import com.cabinet.client.util.AppointmentStatus;
import com.cabinet.client.util.LanguageManager;
import com.cabinet.client.util.SceneManager;
import com.cabinet.client.util.Session;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class DoctorScheduleController {
    @FXML
    private TextField mondayField;
    @FXML
    private TextField tuesdayField;
    @FXML
    private TextField wednesdayField;
    @FXML
    private TextField thursdayField;
    @FXML
    private TextField fridayField;
    @FXML
    private Label mondayCurrentLabel;
    @FXML
    private Label tuesdayCurrentLabel;
    @FXML
    private Label wednesdayCurrentLabel;
    @FXML
    private Label thursdayCurrentLabel;
    @FXML
    private Label fridayCurrentLabel;

    @FXML
    private ComboBox<String> filterCombo;
    @FXML
    private TableView<Appointment> appointmentTable;
    @FXML
    private TableColumn<Appointment, String> patientColumn;
    @FXML
    private TableColumn<Appointment, String> dateColumn;
    @FXML
    private TableColumn<Appointment, String> statusColumn;

    private Doctor currentDoctor;

    private List<Appointment> allAppointments;
    private List<Patient> allPatients;

    @FXML
    public void initialize() {
        User loggedUser = Session.getCurrentUser();

        if (loggedUser == null) {
            SceneManager.switchScene("login-view.fxml");
            return;
        }

        patientColumn.setCellValueFactory(cellData -> {
            Integer patientId = cellData.getValue().getPatientId();

            if (allPatients == null) {
                return new SimpleStringProperty("Loading...");
            }

            Patient patient = allPatients.stream()
                    .filter(p -> p.getId().equals(patientId))
                    .findFirst()
                    .orElse(null);

            if (patient == null) {
                return new SimpleStringProperty("Unknown");
            }

            return new SimpleStringProperty(patient.getLastName() + " " + patient.getFirstName());
        });

        dateColumn.setCellValueFactory(cellData -> {
            String rawDate = cellData.getValue().getDate();

            try {
                LocalDateTime dateTime = LocalDateTime.parse(rawDate);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

                return new SimpleStringProperty(dateTime.format(formatter));
            } catch (Exception e) {
                return new SimpleStringProperty(rawDate);
            }
        });

        statusColumn.setCellValueFactory(cellData -> {
            AppointmentStatus status = cellData.getValue().getStatus();
            ResourceBundle bundle = LanguageManager.getBundle();
            String translatedStatus = switch (status) {
                case SCHEDULED -> bundle.getString("status.scheduled");
                case COMPLETED -> bundle.getString("status.completed");
                case CANCELLED -> bundle.getString("status.cancelled");
            };

            return new SimpleStringProperty(translatedStatus);
        });
        appointmentTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        appointmentTable.setRowFactory(table -> {
            TableRow<Appointment> row = new TableRow<>();

            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    Appointment appointment = row.getItem();
                    if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);

                        alert.setHeaderText(null);
                        alert.setContentText("Consultation already completed.");
                        alert.showAndWait();

                        return;
                    }

                    openConsultationDialog(appointment);
                }
            });

            return row;
        });

        ResourceBundle bundle = LanguageManager.getBundle();

        filterCombo.setItems(FXCollections.observableArrayList(
                bundle.getString("doctor.current.week"),
                bundle.getString("doctor.current.month")
        ));

        filterCombo.setValue( LanguageManager.getBundle()
                .getString("doctor.current.week"));
        filterCombo.valueProperty().addListener((obs, oldValue, newValue) -> {
            filterAppointments();
        });

        loadDoctor(loggedUser.getId());
        loadPatients(loggedUser.getId());
        loadAppointments(loggedUser.getId());
    }

    private void loadDoctor(Integer doctorId) {
        DoctorService doctorService = new DoctorService();

        doctorService.getDoctor(doctorId)
                .thenAccept(doctor -> {
                    Platform.runLater(() -> {
                        currentDoctor = doctor;
                        String officeHours = doctor.getOfficeHours();

                        if (officeHours == null || officeHours.isBlank()) {
                            return;
                        }

                        String[] days = officeHours.split(";");

                        for (String day : days) {
                            String[] parts = day.split("=");

                            if (parts.length != 2) {
                                continue;
                            }

                            String dayName = parts[0].trim();
                            String value = parts[1].trim();

                            switch (dayName) {
                                case "Monday" -> {
                                    mondayField.setText(value);
                                    mondayCurrentLabel.setText(value);
                                }
                                case "Tuesday" -> {
                                    tuesdayField.setText(value);
                                    tuesdayCurrentLabel.setText(value);
                                }
                                case "Wednesday" -> {
                                    wednesdayField.setText(value);
                                    wednesdayCurrentLabel.setText(value);
                                }
                                case "Thursday" -> {
                                    thursdayField.setText(value);
                                    thursdayCurrentLabel.setText(value);
                                }
                                case "Friday" -> {
                                    fridayField.setText(value);
                                    fridayCurrentLabel.setText(value);
                                }
                            }
                        }
                    });
                });
    }

    private void loadPatients(Integer doctorId) {
        PatientService patientService = new PatientService();

        patientService.getPatients(doctorId)
                .thenAccept(patients -> {
                    allPatients = patients;
                });
    }

    private void loadAppointments(Integer doctorId) {
        AppointmentService appointmentService = new AppointmentService();
        appointmentService.getAppointments(doctorId)
                .thenAccept(appointments -> {
                    Platform.runLater(() -> {
                        allAppointments = appointments;
                        filterAppointments();
                    });
                });
    }

    private void filterAppointments() {
        if (allAppointments == null) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();

        List<Appointment> filtered = allAppointments.stream()
                .filter(appointment -> {
                    try {
                        LocalDateTime appointmentDate = LocalDateTime.parse(appointment.getDate());
                        if (filterCombo.getValue().equals(LanguageManager.getBundle().getString("doctor.current.week"))) {
                            return appointmentDate.getYear() == now.getYear()
                                    && appointmentDate.get(java.time.temporal.WeekFields.ISO.weekOfWeekBasedYear()) ==
                                    now.get(java.time.temporal.WeekFields.ISO.weekOfWeekBasedYear());
                        }

                        return appointmentDate.getMonth() == now.getMonth() && appointmentDate.getYear() == now.getYear();
                    } catch (Exception e) {
                        return false;
                    }
                })
                .toList();

        appointmentTable.setItems(FXCollections.observableArrayList(filtered));
    }

    private void openConsultationDialog(Appointment appointment) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                            "/com/cabinet/client/consultation-dialog-view.fxml"),
                            LanguageManager.getBundle()
            );

            Parent root = loader.load();
            ConsultationDialogController controller = loader.getController();
            controller.setAppointment(appointment);

            Stage stage = new Stage();

            stage.setTitle("Consultation");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setScene(new Scene(root));

            stage.showAndWait();

            loadAppointments(Session.getCurrentUser().getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onSaveOfficeHoursClick() {
        if (currentDoctor == null) {
            return;
        }

        String officeHours =
                "Monday = " + mondayField.getText() + ";" +
                "Tuesday = " + tuesdayField.getText() + ";" +
                "Wednesday = " + wednesdayField.getText() + ";" +
                "Thursday = " + thursdayField.getText() + ";" +
                "Friday = " + fridayField.getText();

        currentDoctor.setOfficeHours(officeHours);

        DoctorService doctorService = new DoctorService();
        doctorService.updateDoctor(currentDoctor)
                .exceptionally(ex -> {
                    ex.printStackTrace();
                    return null;
                });
        mondayCurrentLabel.setText(mondayField.getText());
        tuesdayCurrentLabel.setText(tuesdayField.getText());
        wednesdayCurrentLabel.setText(wednesdayField.getText());
        thursdayCurrentLabel.setText(thursdayField.getText());
        fridayCurrentLabel.setText(fridayField.getText());
    }

    @FXML
    protected void onBackClick() {
        SceneManager.switchScene("doctor-dashboard-view.fxml");
    }
}