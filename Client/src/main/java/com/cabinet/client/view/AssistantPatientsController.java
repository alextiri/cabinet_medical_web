package com.cabinet.client.view;

import com.cabinet.client.model.Consultation;
import com.cabinet.client.model.Doctor;
import com.cabinet.client.model.MedicalRecord;
import com.cabinet.client.model.Patient;
import com.cabinet.client.presenter.IPatientView;
import com.cabinet.client.presenter.PatientPresenter;
import com.cabinet.client.service.ConsultationService;
import com.cabinet.client.service.DoctorService;
import com.cabinet.client.service.MedicalRecordService;
import com.cabinet.client.service.ExportService;
import com.cabinet.client.util.ExportType;
import com.cabinet.client.util.LanguageManager;
import com.cabinet.client.util.SceneManager;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AssistantPatientsController implements IPatientView {
    @FXML
    private TableView<Patient> patientTable;

    @FXML
    private TableColumn<Patient, String> nameColumn;
    @FXML
    private TableColumn<Patient, String> genderColumn;
    @FXML
    private TableColumn<Patient, String> birthDateColumn;
    @FXML
    private TableColumn<Patient, String> cnpColumn;
    @FXML
    private TableColumn<Patient, String> phoneColumn;
    @FXML
    private TableColumn<Patient, String> emailColumn;
    @FXML
    private TableColumn<Patient, String> addressColumn;

    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<Doctor> doctorFilterCombo;
    @FXML
    private TextField diagnosisFilterField;
    @FXML
    private TextField ageFilterField;

    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField cnpField;
    @FXML
    private ComboBox<String> genderCombo;
    @FXML
    private DatePicker birthDatePicker;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField addressField;
    @FXML
    private ComboBox<String> exportFormatCombo;
    @FXML
    private ComboBox<ExportType> exportTypeCombo;

    private List<Doctor> allDoctors;
    private PatientPresenter presenter;
    private List<Consultation> allConsultations;
    private List<MedicalRecord> allMedicalRecords;
    private List<Patient> allPatients;

    @FXML
    public void initialize() {
        presenter = new PatientPresenter(this);
        searchField.textProperty().addListener((obs, oldValue, newValue) -> {
            filterPatients();
        });

        doctorFilterCombo.valueProperty().addListener((obs, oldValue, newDoctor) -> {
            if (newDoctor == null) {
                presenter.loadAllPatients();
                return;
            }
            presenter.loadPatientsByDoctor(newDoctor.getId());
        });

        diagnosisFilterField.textProperty().addListener((obs, oldValue, newValue) -> {
            filterPatients();
        });

        ageFilterField.textProperty().addListener((obs, oldValue, newValue) -> {
            filterPatients();
        });

        nameColumn.setCellValueFactory(cellData -> {
            Patient patient = cellData.getValue();

            return new SimpleStringProperty(patient.getLastName() + " " + patient.getFirstName());
        });

        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));

        birthDateColumn.setCellValueFactory(cellData -> {
            String rawDate = cellData.getValue().getBirthDate();

            try {
                DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                return new SimpleStringProperty(LocalDate.parse(rawDate, inputFormatter).format(outputFormatter));
            } catch (Exception e) {
                return new SimpleStringProperty(rawDate);
            }
        });

        cnpColumn.setCellValueFactory(new PropertyValueFactory<>("cnp"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        patientTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        genderCombo.setItems(FXCollections.observableArrayList(
                "M",
                "F"
        ));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        birthDatePicker.setConverter(new javafx.util.StringConverter<>() {
            @Override
            public String toString(LocalDate date) {
                if (date == null) {
                    return "";
                }

                return formatter.format(date);
            }

            @Override
            public LocalDate fromString(String string) {
                if (string == null || string.isBlank()) {
                    return null;
                }

                return LocalDate.parse(string, formatter);
            }
        });

        patientTable.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldPatient, selectedPatient) -> {

                    if (selectedPatient == null) {
                        return;
                    }

                    firstNameField.setText(selectedPatient.getFirstName());
                    lastNameField.setText(selectedPatient.getLastName());
                    cnpField.setText(selectedPatient.getCnp());
                    genderCombo.setValue(selectedPatient.getGender());
                    try {
                        birthDatePicker.setValue(LocalDate.parse(selectedPatient.getBirthDate()));
                    } catch (Exception e) {
                        birthDatePicker.setValue(null);
                    }
                    phoneField.setText(selectedPatient.getPhone());
                    emailField.setText(selectedPatient.getEmail());
                    addressField.setText(selectedPatient.getAddress());
                });

        exportTypeCombo.setItems(
                FXCollections.observableArrayList(
                        ExportType.PATIENTS,
                        ExportType.DOCTORS
                )
        );

        exportTypeCombo.setValue(ExportType.PATIENTS);
        exportTypeCombo.setCellFactory(param ->
                new ListCell<>() {
                    @Override
                    protected void updateItem(ExportType item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty || item == null) {
                            setText(null);
                        } else {
                            switch (item) {
                                case PATIENTS ->
                                        setText(LanguageManager
                                                .getBundle()
                                                .getString("common.patients")
                                        );
                                case DOCTORS ->
                                        setText(LanguageManager
                                                .getBundle()
                                                .getString("common.doctors")
                                        );
                            }
                        }
                    }
                }
        );

        exportTypeCombo.setButtonCell(exportTypeCombo.getCellFactory().call(null));

        exportFormatCombo.setItems(
                FXCollections.observableArrayList(
                        "CSV",
                        "JSON",
                        "TXT",
                        "XML"
                )
        );

        exportFormatCombo.setValue("CSV");

        loadDoctors();
        presenter.loadAllPatients();
        loadMedicalRecords();
        loadConsultations();
    }

    private void loadDoctors() {
        DoctorService doctorService = new DoctorService();

        doctorService.getAllDoctors()
                .thenAccept(doctors -> {
                    Platform.runLater(() -> {
                        allDoctors = doctors;
                        doctorFilterCombo.setItems(FXCollections.observableArrayList(doctors));
                    });
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
                    setText("Dr. " + doctor.getLastName() + " " + doctor.getFirstName());
                }
            }
        });
    }

    private void filterPatients() {
        if (allPatients == null || allMedicalRecords == null || allConsultations == null) {
            return;
        }

        String search = searchField.getText().toLowerCase();
        String diagnosisSearch = diagnosisFilterField.getText().toLowerCase();
        String ageSearch = ageFilterField.getText().trim();

        final Integer targetAge;

        try {
            targetAge = ageSearch.isBlank() ? null : Integer.parseInt(ageSearch);
        } catch (Exception e) {
            return;
        }

        List<Patient> filtered = allPatients.stream()
                .filter(patient -> {
                    String firstName = patient.getFirstName() == null ? "" : patient.getFirstName().toLowerCase();
                    String lastName = patient.getLastName() == null ? "" : patient.getLastName().toLowerCase();

                    boolean matchesSearch = firstName.contains(search) || lastName.contains(search);
                    boolean matchesDiagnosis = true;

                    if (!diagnosisSearch.isBlank()) {
                        matchesDiagnosis = allMedicalRecords.stream()
                                .filter(record ->
                                        record.getPatientId().equals(patient.getId()))
                                .anyMatch(record ->
                                        allConsultations.stream()
                                                .filter(consultation ->
                                                        consultation.getMedicalRecordId().equals(record.getId()))
                                                .anyMatch(consultation -> {
                                                    String diagnosis = consultation.getDiagnosis() == null
                                                                    ? ""
                                                                    : consultation.getDiagnosis()
                                                                    .toLowerCase();
                                                    return diagnosis.contains(diagnosisSearch);
                                                })
                                );
                    }
                    boolean matchesAge = true;

                    if (targetAge != null) {
                        try {
                            LocalDate birthDate = LocalDate.parse(patient.getBirthDate());
                            int age = LocalDate.now().getYear() - birthDate.getYear();
                            if (birthDate.plusYears(age).isAfter(LocalDate.now())) {
                                age--;
                            }

                            matchesAge = age >= targetAge;
                        } catch (Exception e) {
                            matchesAge = false;
                        }
                    }
                    return matchesSearch && matchesDiagnosis && matchesAge;
                })
                .toList();
        System.out.println(
                "Filtered patients: " + filtered.size()
        );
        patientTable.setItems(FXCollections.observableArrayList(filtered));
    }

    @FXML
    protected void onResetFiltersClick() {
        searchField.clear();
        diagnosisFilterField.clear();
        ageFilterField.clear();
        doctorFilterCombo.setValue(null);

        presenter.loadAllPatients();
    }

    @FXML
    protected void onResetFieldsClick() {
        firstNameField.clear();
        lastNameField.clear();
        cnpField.clear();
        genderCombo.setValue(null);
        birthDatePicker.setValue(null);
        phoneField.clear();
        emailField.clear();
        addressField.clear();

        patientTable.getSelectionModel().clearSelection();
    }

    private void loadMedicalRecords() {
        MedicalRecordService service = new MedicalRecordService();
        service.getAllMedicalRecords()
                .thenAccept(records -> {
                    allMedicalRecords = records;
                });
    }

    private void loadConsultations() {
        ConsultationService service = new ConsultationService();
        service.getAllConsultations()
                .thenAccept(consultations -> {
                    allConsultations = consultations;
                });
    }

    @FXML
    protected void onAddPatientClick() {
        try {
            Patient patient = new Patient();

            patient.setFirstName(firstNameField.getText());
            patient.setLastName(lastNameField.getText());
            patient.setCnp(cnpField.getText());
            patient.setGender(genderCombo.getValue());

            if (birthDatePicker.getValue() != null) {
                patient.setBirthDate(
                        birthDatePicker.getValue().toString()
                );
            }

            patient.setPhone(phoneField.getText());
            patient.setEmail(emailField.getText());
            patient.setAddress(addressField.getText());

            presenter.createPatient(patient);
            onResetFieldsClick();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onUpdatePatientClick() {
        Patient selectedPatient = patientTable.getSelectionModel().getSelectedItem();
        if (selectedPatient == null) {
            return;
        }

        try {
            selectedPatient.setFirstName(firstNameField.getText());
            selectedPatient.setLastName(lastNameField.getText());
            selectedPatient.setCnp(cnpField.getText());
            selectedPatient.setGender(genderCombo.getValue());
            if (birthDatePicker.getValue() != null) {
                selectedPatient.setBirthDate(birthDatePicker.getValue().toString());
            }
            selectedPatient.setPhone(phoneField.getText());
            selectedPatient.setEmail(emailField.getText());
            selectedPatient.setAddress(addressField.getText());

            presenter.updatePatient(selectedPatient);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onDeletePatientClick() {
        Patient selectedPatient = patientTable.getSelectionModel().getSelectedItem();

        if (selectedPatient == null) {
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        alert.setTitle("Delete Patient");
        alert.setHeaderText("Delete selected patient?");
        alert.setContentText(selectedPatient.getLastName() + " " + selectedPatient.getFirstName());

        alert.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                presenter.deletePatient(selectedPatient.getId());
            }
        });
    }

    @FXML
    protected void onBackClick() {
        SceneManager.switchScene("assistant-dashboard-view.fxml");
    }

    @Override
    public void showPatients(List<Patient> patients) {
        Platform.runLater(() -> {
            System.out.println(
                    "Patients received: " + patients.size()
            );
            allPatients = patients;
            filterPatients();
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

    @FXML
    protected void onExportClick() {
        ExportType exportType = exportTypeCombo.getValue();
        String format = exportFormatCombo.getValue();

        if (exportType == null || format == null) {
            return;
        }

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Export Data");
        if (format.equals("CSV")) {
            chooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter(
                            "CSV Files",
                            "*.csv"
                    )
            );

        } else if (format.equals("JSON")) {
            chooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter(
                            "JSON Files",
                            "*.json"
                    )
            );
        } else if (format.equals("TXT")) {
            chooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter(
                            "Text Files",
                            "*.txt"
                    )
            );

        } else if (format.equals("XML")) {
            chooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter(
                            "XML Files",
                            "*.xml"
                    )
            );
        }

        File file = chooser.showSaveDialog(patientTable.getScene().getWindow());

        if (file == null) {
            return;
        }

        ExportService exportService = new ExportService();
        try {
            if (exportType == ExportType.PATIENTS) {
                List<Patient> patients = List.copyOf(patientTable.getItems());
                switch (format) {
                    case "CSV" -> exportService.exportToCSV(
                            patients,
                            file
                    );
                    case "JSON" -> exportService.exportToJSON(
                            patients,
                            file
                    );
                    case "TXT" -> exportService.exportToTXT(
                            patients,
                            file
                    );
                    case "XML" -> exportService.exportToXML(
                            patients,
                            file
                    );
                }

            } else if (exportType == ExportType.DOCTORS) {
                switch (format) {
                    case "CSV" -> exportService.exportToCSV(
                            allDoctors,
                            file
                    );
                    case "JSON" -> exportService.exportToJSON(
                            allDoctors,
                            file
                    );
                    case "TXT" -> exportService.exportToTXT(
                            allDoctors,
                            file
                    );
                    case "XML" -> exportService.exportToXML(
                            allDoctors,
                            file
                    );
                }
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);

            alert.setTitle("Export Complete");
            alert.setHeaderText(null);
            alert.setContentText("Data exported successfully.");

            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);

            alert.setTitle("Export Error");
            alert.setHeaderText(null);
            alert.setContentText("Could not export data.");

            alert.showAndWait();
        }
    }
}