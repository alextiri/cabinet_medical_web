package com.cabinet.client.view;

import com.cabinet.client.model.Consultation;
import com.cabinet.client.model.MedicalRecord;
import com.cabinet.client.model.Patient;
import com.cabinet.client.model.User;
import com.cabinet.client.service.ConsultationService;
import com.cabinet.client.service.MedicalRecordService;
import com.cabinet.client.service.PatientService;
import com.cabinet.client.util.LanguageManager;
import com.cabinet.client.util.SceneManager;
import com.cabinet.client.util.Session;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DoctorPatientController {
    @FXML
    private TableView<Patient> patientTable;
    @FXML
    private TableColumn<Patient, String> nameColumn;
    @FXML
    private TableColumn<Patient, String> sexColumn;
    @FXML
    private TableColumn<Patient, String> birthDateColumn;
    @FXML
    private TableColumn<Patient, String> cnpColumn;
    @FXML
    private TextField searchPatientField;
    @FXML
    private TextField searchConsultationField;
    @FXML
    private TableView<Consultation> consultationTable;
    @FXML
    private TableColumn<Consultation, String> dateColumn;
    @FXML
    private TableColumn<Consultation, String> symptomsColumn;
    @FXML
    private TableColumn<Consultation, String> diagnosisColumn;
    @FXML
    private TableColumn<Consultation, String> treatmentColumn;
    @FXML
    private TableColumn<Consultation, String> observationsColumn;

    private List<Patient> allPatients;
    private List<MedicalRecord> medicalRecords;
    private List<Consultation> allConsultations;

    @FXML
    public void initialize() {
        User loggedUser = Session.getCurrentUser();

        if (loggedUser == null) {
            SceneManager.switchScene("login-view.fxml");
            return;
        }

        nameColumn.setCellValueFactory(cellData -> {
            Patient patient = cellData.getValue();
            String fullName = patient.getLastName() + " " + patient.getFirstName();

            return new SimpleStringProperty(fullName);
        });

        sexColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));

        birthDateColumn.setCellValueFactory(cellData -> {
            String rawDate = cellData.getValue().getBirthDate();

            try {
                DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                return new SimpleStringProperty(java.time.LocalDate
                                .parse(rawDate, inputFormatter)
                                .format(outputFormatter)
                );
            } catch (Exception e) {
                return new SimpleStringProperty(rawDate);
            }
        });

        cnpColumn.setCellValueFactory(new PropertyValueFactory<>("cnp"));

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

        symptomsColumn.setCellValueFactory(new PropertyValueFactory<>("symptoms"));
        diagnosisColumn.setCellValueFactory(new PropertyValueFactory<>("diagnosis"));
        treatmentColumn.setCellValueFactory(new PropertyValueFactory<>("treatment"));
        observationsColumn.setCellValueFactory(new PropertyValueFactory<>("observations"));
        patientTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        consultationTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        consultationTable.setPlaceholder(new Label(LanguageManager.getBundle().getString("table.empty")));

        searchPatientField.textProperty().addListener((obs, oldValue, newValue) -> {
            filterPatients();
        });

        searchConsultationField.textProperty().addListener((obs, oldValue, newValue) -> {
            filterPatients();
        });

        patientTable.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldPatient, selectedPatient) -> {
                    if (selectedPatient != null) {
                        loadPatientConsultations(selectedPatient);
                    }
                });

        loadData(loggedUser.getId());
    }

    private void loadData(Integer doctorId) {
        PatientService patientService = new PatientService();

        patientService.getPatients(doctorId)
                .thenAccept(patients -> {
                    Platform.runLater(() -> {
                        allPatients = patients;
                        patientTable.setItems(FXCollections.observableArrayList(patients));
                    });
                });

        MedicalRecordService medicalRecordService = new MedicalRecordService();
        medicalRecordService.getMedicalRecords(doctorId)
                .thenAccept(records -> {
                    medicalRecords = records;
                });

        ConsultationService consultationService = new ConsultationService();
        consultationService.getConsultations(doctorId)
                .thenAccept(consultations -> {
                    allConsultations = consultations;
                });
    }

    private void loadPatientConsultations(Patient patient) {
        if (medicalRecords == null || allConsultations == null) {
            return;
        }

        MedicalRecord medicalRecord = medicalRecords.stream()
                .filter(record ->
                        record.getPatientId().equals(patient.getId())
                )
                .findFirst()
                .orElse(null);

        if (medicalRecord == null) {
            consultationTable.setItems(FXCollections.observableArrayList());
            return;
        }

        List<Consultation> consultations = allConsultations.stream()
                .filter(consultation ->
                        consultation.getMedicalRecordId().equals(medicalRecord.getId()))
                .toList();

        consultationTable.setItems(FXCollections.observableArrayList(consultations));
    }

    private void filterPatients() {
        if (allPatients == null || medicalRecords == null || allConsultations == null) {
            return;
        }

        String patientSearch = searchPatientField.getText() == null ? "" : searchPatientField.getText().toLowerCase();
        String consultationSearch = searchConsultationField.getText() == null ? "" : searchConsultationField.getText().toLowerCase();

        List<Patient> filtered = allPatients.stream()
                .filter(patient -> {
                    String firstName = patient.getFirstName() == null ? "" : patient.getFirstName().toLowerCase();
                    String lastName = patient.getLastName() == null ? "" : patient.getLastName().toLowerCase();

                    String cnp = patient.getCnp() == null ? "" : patient.getCnp().toLowerCase();

                    boolean matchesPatientSearch =
                            firstName.contains(patientSearch) || lastName.contains(patientSearch) || cnp.contains(patientSearch);

                    if (!matchesPatientSearch) {
                        return false;
                    }

                    if (consultationSearch.isBlank()) {
                        return true;
                    }

                    MedicalRecord medicalRecord = medicalRecords.stream()
                            .filter(record ->
                                    record.getPatientId().equals(patient.getId()))
                            .findFirst()
                            .orElse(null);

                    if (medicalRecord == null) {
                        return false;
                    }

                    return allConsultations.stream()
                            .filter(consultation ->
                                    consultation.getMedicalRecordId().equals(medicalRecord.getId()))
                            .anyMatch(consultation -> {

                                String diagnosis = consultation.getDiagnosis() == null ? "" : consultation.getDiagnosis()
                                                .toLowerCase();
                                String treatment = consultation.getTreatment() == null ? "" : consultation.getTreatment()
                                                .toLowerCase();

                                return diagnosis.contains(consultationSearch) || treatment.contains(consultationSearch);
                            });
                })
                .toList();

        patientTable.setItems(FXCollections.observableArrayList(filtered));
    }

    @FXML
    protected void onBackClick() {
        SceneManager.switchScene("doctor-dashboard-view.fxml");
    }

    @FXML
    protected void onResetFiltersClick() {
        searchPatientField.clear();
        searchConsultationField.clear();

        patientTable.setItems(FXCollections.observableArrayList(allPatients));

        patientTable.getSelectionModel().clearSelection();

        consultationTable.setItems(FXCollections.observableArrayList());
    }
}