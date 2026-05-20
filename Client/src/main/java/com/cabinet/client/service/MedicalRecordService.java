package com.cabinet.client.service;

import com.cabinet.client.model.MedicalRecord;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MedicalRecordService {
    public CompletableFuture<List<MedicalRecord>> getMedicalRecords(Integer doctorId) {
        String url = "http://localhost:8080/medical-records/doctor/" + doctorId;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        return mapper.readValue(
                                response.body(),
                                new TypeReference<List<MedicalRecord>>() {}
                        );
                    } catch (Exception e) {
                        e.printStackTrace();
                        return List.of();
                    }
                });
    }

    public CompletableFuture<List<MedicalRecord>> getAllMedicalRecords() {
        String url = "http://localhost:8080/medical-records";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        return mapper.readValue(
                                response.body(),
                                new TypeReference<List<MedicalRecord>>() {}
                        );
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                });
    }

    public CompletableFuture<MedicalRecord> getMedicalRecordByPatientId(Integer patientId) {
        String url = "http://localhost:8080/medical-records/patient/" + patientId;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        return mapper.readValue(
                                response.body(),
                                MedicalRecord.class
                        );

                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                });
    }
}