package com.cabinet.client.view;

import com.cabinet.client.model.Consultation;
import com.cabinet.client.model.MedicalRecord;
import com.cabinet.client.model.Patient;
import com.cabinet.client.service.ConsultationService;
import com.cabinet.client.service.MedicalRecordService;
import com.cabinet.client.service.PatientService;
import com.cabinet.client.util.LanguageManager;
import com.cabinet.client.util.SceneManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssistantStatisticsController {
    @FXML
    private PieChart genderChart;
    @FXML
    private PieChart diagnosisChart;
    @FXML
    private BarChart<String, Number> ageChart;

    private List<Patient> allPatients;
    private List<Consultation> allConsultations;
    private List<MedicalRecord> allMedicalRecords;

    @FXML
    public void initialize() {
        loadPatients();
        loadMedicalRecords();
        loadConsultations();
    }

    private void loadPatients() {
        PatientService service = new PatientService();

        service.getAllPatients()
                .thenAccept(patients -> {

                    allPatients = patients;

                    Platform.runLater(() -> {
                        loadGenderChart();
                        loadAgeChart();
                    });
                });
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

                    Platform.runLater(() -> {
                        loadDiagnosisChart();
                    });
                });
    }

    private void loadGenderChart() {
        if (allPatients == null) {
            return;
        }

        long maleCount = allPatients.stream()
                        .filter(patient ->
                                "M".equalsIgnoreCase(patient.getGender()))
                        .count();

        long femaleCount = allPatients.stream()
                        .filter(patient ->
                                "F".equalsIgnoreCase(patient.getGender()))
                        .count();

        ObservableList<PieChart.Data> data =
                FXCollections.observableArrayList(
                        new PieChart.Data(LanguageManager.getBundle().getString("gender.male"), maleCount),
                        new PieChart.Data(LanguageManager.getBundle().getString("gender.female"), femaleCount)
                );

        genderChart.setData(data);
    }

    private void loadDiagnosisChart() {
        if (allConsultations == null) {
            return;
        }

        Map<String, Integer> diagnosisCounts = new HashMap<>();
        for (Consultation consultation : allConsultations) {
            String diagnosis = consultation.getDiagnosis();

            if (diagnosis == null || diagnosis.isBlank()) {
                continue;
            }

            diagnosisCounts.put(
                    diagnosis,
                    diagnosisCounts.getOrDefault(
                            diagnosis,
                            0
                    ) + 1
            );
        }

        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();

        for (Map.Entry<String, Integer> entry : diagnosisCounts.entrySet()) {
            data.add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }

        diagnosisChart.setData(data);
    }

    private void loadAgeChart() {
        if (allPatients == null) {
            return;
        }

        Map<String, Integer> groups = new java.util.LinkedHashMap<>();

        groups.put("0-18", 0);
        groups.put("19-30", 0);
        groups.put("31-40", 0);
        groups.put("41-50", 0);
        groups.put("51-65", 0);
        groups.put("65+", 0);

        for (Patient patient : allPatients) {
            try {
                LocalDate birthDate = LocalDate.parse(patient.getBirthDate());
                int age = LocalDate.now().getYear() - birthDate.getYear();
                if (birthDate.plusYears(age).isAfter(LocalDate.now())) {
                    age--;
                }

                if (age <= 18) {
                    groups.put(
                            "0-18",
                            groups.get("0-18") + 1
                    );
                } else if (age <= 30) {
                    groups.put(
                            "19-30",
                            groups.get("19-30") + 1
                    );
                } else if (age <= 40) {
                    groups.put(
                            "31-40",
                            groups.get("31-40") + 1
                    );
                } else if (age <= 50) {
                    groups.put(
                            "41-50",
                            groups.get("41-50") + 1
                    );
                } else if (age <= 65) {
                    groups.put(
                            "51-65",
                            groups.get("51-65") + 1
                    );
                } else {
                    groups.put(
                            "65+",
                            groups.get("65+") + 1
                    );
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        XYChart.Series<String, Number> series = new XYChart.Series<>();

        for (Map.Entry<String, Integer> entry : groups.entrySet()) {
            if (entry.getValue() > 0) {
                series.getData().add(
                        new XYChart.Data<>(
                                entry.getKey(),
                                entry.getValue()
                        )
                );
            }
        }

        ageChart.getData().clear();
        ageChart.getData().add(series);
        CategoryAxis xAxis = (CategoryAxis) ageChart.getXAxis();

        xAxis.setCategories(FXCollections.observableArrayList(
                        "0-18",
                        "19-30",
                        "31-40",
                        "41-50",
                        "51-65",
                        "65+"
                )
        );

        ageChart.setLegendVisible(false);
    }

    @FXML
    protected void onBackClick() {
        SceneManager.switchScene("assistant-dashboard-view.fxml");
    }
}