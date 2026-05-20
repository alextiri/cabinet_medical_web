package com.cabinet.client.view;

import com.cabinet.client.model.Appointment;
import com.cabinet.client.model.Doctor;
import com.cabinet.client.model.Patient;
import com.cabinet.client.presenter.AppointmentPresenter;
import com.cabinet.client.presenter.IAppointmentView;
import com.cabinet.client.service.DoctorService;
import com.cabinet.client.service.PatientService;
import com.cabinet.client.util.AppointmentStatus;
import com.cabinet.client.util.LanguageManager;
import com.cabinet.client.util.SceneManager;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class AssistantAppointmentsController implements IAppointmentView {
    @FXML
    private TableView<Appointment> appointmentsTable;
    @FXML
    private TableColumn<Appointment, String> patientColumn;
    @FXML
    private TableColumn<Appointment, String> doctorColumn;
    @FXML
    private TableColumn<Appointment, String> dateTimeColumn;
    @FXML
    private TableColumn<Appointment, String> statusColumn;
    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<Doctor> doctorFilterCombo;
    @FXML
    private ComboBox<AppointmentStatus> statusFilterCombo;
    @FXML
    private DatePicker dateFilterPicker;
    @FXML
    private ComboBox<Patient> patientCombo;
    @FXML
    private ComboBox<Doctor> doctorCombo;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField timeField;
    @FXML
    private ComboBox<AppointmentStatus> statusCombo;

    private AppointmentPresenter presenter;
    private List<Appointment> allAppointments;
    private List<Patient> allPatients;
    private List<Doctor> allDoctors;

    @FXML
    public void initialize() {
        presenter = new AppointmentPresenter(this);
        patientColumn.setCellValueFactory(cellData -> {
            Integer patientId = cellData.getValue().getPatientId();
            Patient patient = allPatients.stream()
                    .filter(p -> p.getId().equals(patientId))
                    .findFirst()
                    .orElse(null);

            if (patient == null) {
                return new SimpleStringProperty("Unknown");
            }

            return new SimpleStringProperty(patient.getLastName() + " " + patient.getFirstName());
        });

        doctorColumn.setCellValueFactory(cellData -> {
            Integer doctorId = cellData.getValue().getDoctorId();
            Doctor doctor = allDoctors.stream()
                    .filter(d -> d.getId().equals(doctorId))
                    .findFirst()
                    .orElse(null);

            if (doctor == null) {
                return new SimpleStringProperty("Unknown");
            }

            return new SimpleStringProperty("Dr. " + doctor.getLastName() + " " + doctor.getFirstName());
        });

        dateTimeColumn.setCellValueFactory(cellData -> {
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
                case SCHEDULED ->
                        bundle.getString("status.scheduled");
                case COMPLETED ->
                        bundle.getString("status.completed");

                case CANCELLED ->
                        bundle.getString("status.cancelled");
            };

            return new SimpleStringProperty(
                    translatedStatus
            );
        });

        appointmentsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        statusCombo.setItems(FXCollections.observableArrayList(AppointmentStatus.values()));
        statusCombo.setCellFactory(param ->
                new ListCell<>() {
                    @Override
                    protected void updateItem(AppointmentStatus item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty || item == null) {
                            setText(null);
                        } else {
                            ResourceBundle bundle = LanguageManager.getBundle();
                            switch (item) {
                                case SCHEDULED ->
                                        setText(bundle.getString("status.scheduled"));
                                case COMPLETED ->
                                        setText(bundle.getString("status.completed"));
                                case CANCELLED ->
                                        setText(bundle.getString("status.cancelled"));
                            }
                        }
                    }
                }
        );

        statusCombo.setButtonCell(statusCombo.getCellFactory().call(null));
        statusFilterCombo.getItems().addAll(AppointmentStatus.values());
        statusFilterCombo.setCellFactory(param ->
                new ListCell<>() {
                    @Override
                    protected void updateItem(AppointmentStatus item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty || item == null) {
                            setText(null);
                        } else {
                            ResourceBundle bundle = LanguageManager.getBundle();
                            switch (item) {
                                case SCHEDULED ->
                                        setText(bundle.getString("status.scheduled"));
                                case COMPLETED ->
                                        setText(bundle.getString("status.completed"));
                                case CANCELLED ->
                                        setText(bundle.getString("status.cancelled"));
                            }
                        }
                    }
                }
        );

        statusFilterCombo.setButtonCell(statusFilterCombo.getCellFactory().call(null));

        patientCombo.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Patient patient, boolean empty) {
                super.updateItem(patient, empty);

                if (empty || patient == null) {
                    setText(null);
                } else {
                    setText(patient.getLastName() + " " + patient.getFirstName());
                }
            }
        });

        patientCombo.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Patient patient, boolean empty) {
                super.updateItem(patient, empty);

                if (empty || patient == null) {
                    setText(null);
                } else {
                    setText(patient.getLastName() + " " + patient.getFirstName());
                }
            }
        });

        doctorCombo.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Doctor doctor, boolean empty) {
                super.updateItem(doctor, empty);

                if (empty || doctor == null) {
                    setText(null);
                } else {
                    setText("Dr. " + doctor.getLastName() + " " + doctor.getFirstName());
                }
            }
        });

        doctorCombo.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Doctor doctor, boolean empty) {
                super.updateItem(doctor, empty);

                if (empty || doctor == null) {
                    setText(null);
                } else {
                    setText("Dr. " + doctor.getLastName() + " " + doctor.getFirstName());
                }
            }
        });

        doctorFilterCombo.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Doctor doctor, boolean empty) {
                super.updateItem(doctor, empty);

                if (empty || doctor == null) {
                    setText(null);
                } else {
                    setText("Dr. " + doctor.getLastName() + " " + doctor.getFirstName());
                }
            }
        });

        doctorFilterCombo.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Doctor doctor, boolean empty) {
                super.updateItem(doctor, empty);

                if (empty || doctor == null) {
                    setText(null);
                } else {
                    setText("Dr. " + doctor.getLastName() + " " + doctor.getFirstName()
                    );
                }
            }
        });

        appointmentsTable.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldAppointment, selectedAppointment) -> {
                    if (selectedAppointment == null) {
                        return;
                    }

                    Patient patient = allPatients.stream()
                            .filter(p ->
                                    p.getId().equals(
                                            selectedAppointment.getPatientId()
                                    ))
                            .findFirst()
                            .orElse(null);

                    Doctor doctor = allDoctors.stream()
                            .filter(d ->
                                    d.getId().equals(
                                            selectedAppointment.getDoctorId()
                                    ))
                            .findFirst()
                            .orElse(null);

                    patientCombo.setValue(patient);
                    doctorCombo.setValue(doctor);

                    try {
                        LocalDateTime dateTime = LocalDateTime.parse(selectedAppointment.getDate());
                        datePicker.setValue(dateTime.toLocalDate());
                        timeField.setText(dateTime.toLocalTime().toString());
                    } catch (Exception e) {
                        datePicker.setValue(null);
                        timeField.clear();
                    }

                    statusCombo.setValue(selectedAppointment.getStatus());
                });

        searchField.textProperty().addListener((obs, oldValue, newValue) -> {filterAppointments();});
        doctorFilterCombo.valueProperty().addListener((obs, oldValue, newValue) -> {filterAppointments();});
        statusFilterCombo.valueProperty().addListener((obs, oldValue, newValue) -> {filterAppointments();});
        dateFilterPicker.valueProperty().addListener((obs, oldValue, newValue) -> {filterAppointments();});

        loadPatients();
        loadDoctors();
        presenter.loadAllAppointments();
    }

    private void loadPatients() {
        PatientService patientService = new PatientService();

        patientService.getAllPatients()
                .thenAccept(patients -> {
                    Platform.runLater(() -> {
                        allPatients = patients;
                        patientCombo.setItems(FXCollections.observableArrayList(patients));
                    });
                });
    }

    private void loadDoctors() {
        DoctorService doctorService = new DoctorService();
        doctorService.getAllDoctors()
                .thenAccept(doctors -> {
                    Platform.runLater(() -> {
                        allDoctors = doctors;
                        doctorCombo.setItems(FXCollections.observableArrayList(doctors));
                        doctorFilterCombo.setItems(FXCollections.observableArrayList(doctors));
                    });
                });
    }

    private void filterAppointments() {
        if (allAppointments == null ||
                allPatients == null ||
                allDoctors == null) {
            return;
        }

        String search = searchField.getText().toLowerCase();
        Doctor selectedDoctor = doctorFilterCombo.getValue();
        AppointmentStatus selectedStatus = statusFilterCombo.getValue();
        LocalDate selectedDate = dateFilterPicker.getValue();

        List<Appointment> filtered = allAppointments.stream()
                .filter(appointment -> {
                    Patient patient = allPatients.stream()
                            .filter(p ->
                                    p.getId().equals(
                                            appointment.getPatientId()
                                    ))
                            .findFirst()
                            .orElse(null);
                    if (patient == null) {
                        return false;
                    }

                    String patientName =
                            (patient.getLastName() + " " + patient.getFirstName()).toLowerCase();

                    boolean matchesSearch = patientName.contains(search);
                    boolean matchesDoctor = selectedDoctor == null || appointment.getDoctorId().equals(selectedDoctor.getId());
                    boolean matchesStatus = selectedStatus == null || appointment.getStatus().equals(selectedStatus);
                    boolean matchesDate = true;

                    if (selectedDate != null) {
                        try {
                            LocalDate appointmentDate = LocalDateTime.parse(appointment.getDate()).toLocalDate();
                            matchesDate = appointmentDate.equals(selectedDate);
                        } catch (Exception e) {
                            matchesDate = false;
                        }
                    }

                    return matchesSearch &&
                            matchesDoctor &&
                            matchesStatus &&
                            matchesDate;
                })
                .toList();
        appointmentsTable.setItems(FXCollections.observableArrayList(filtered));
    }

    @FXML
    protected void onAddAppointmentClick() {
        try {
            Patient patient = patientCombo.getValue();
            Doctor doctor = doctorCombo.getValue();

            if (patient == null || doctor == null) {
                return;
            }

            LocalDate date = datePicker.getValue();
            LocalTime time = LocalTime.parse(timeField.getText());
            LocalDateTime dateTime = LocalDateTime.of(date, time);

            Appointment appointment = new Appointment();

            appointment.setPatientId(patient.getId());
            appointment.setDoctorId(doctor.getId());
            appointment.setDate(dateTime.toString());
            appointment.setStatus(statusCombo.getValue());

            presenter.createAppointment(appointment);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onUpdateAppointmentClick() {
        Appointment selectedAppointment = appointmentsTable.getSelectionModel().getSelectedItem();

        if (selectedAppointment == null) {
            return;
        }

        try {
            Patient patient = patientCombo.getValue();
            Doctor doctor = doctorCombo.getValue();

            if (patient == null || doctor == null) {
                return;
            }

            LocalDate date = datePicker.getValue();
            LocalTime time = LocalTime.parse(timeField.getText());
            LocalDateTime dateTime = LocalDateTime.of(date, time);

            selectedAppointment.setPatientId(patient.getId());
            selectedAppointment.setDoctorId(doctor.getId());
            selectedAppointment.setDate(dateTime.toString());
            selectedAppointment.setStatus(statusCombo.getValue());

            presenter.updateAppointment(selectedAppointment);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onDeleteAppointmentClick() {
        Appointment selectedAppointment = appointmentsTable.getSelectionModel().getSelectedItem();

        if (selectedAppointment == null) {
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        alert.setTitle("Delete Appointment");
        alert.setHeaderText("Delete selected appointment?");

        alert.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                presenter.deleteAppointment(selectedAppointment.getId());
            }
        });
    }

    @FXML
    protected void onResetFieldsClick() {
        patientCombo.setValue(null);
        doctorCombo.setValue(null);
        datePicker.setValue(null);
        timeField.clear();
        statusCombo.setValue(null);

        appointmentsTable.getSelectionModel().clearSelection();
    }

    @FXML
    protected void onResetFiltersClick() {
        searchField.clear();
        doctorFilterCombo.setValue(null);
        statusFilterCombo.setValue(null);
        dateFilterPicker.setValue(null);

        appointmentsTable.setItems(FXCollections.observableArrayList(allAppointments));
    }

    @FXML
    protected void onBackClick() {
        SceneManager.switchScene(
                "assistant-dashboard-view.fxml"
        );
    }

    @Override
    public void showAppointments(List<Appointment> appointments) {
        Platform.runLater(() -> {
            allAppointments = appointments;
            appointmentsTable.setItems(FXCollections.observableArrayList(appointments));
        });
    }

    @Override
    public void showError(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);

            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
}