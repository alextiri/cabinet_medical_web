package com.cabinet.client.service;

import com.cabinet.client.model.Patient;
import com.cabinet.client.util.Session;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PatientService {
    public CompletableFuture<List<Patient>> getPatients(Integer doctorId) {
        String url = "http://localhost:8080/patients/doctor/" + doctorId;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        return mapper.readValue(response.body(), new TypeReference<List<Patient>>() {});
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                });
    }

    public CompletableFuture<List<Patient>> getAllPatients() {
        String url = "http://localhost:8080/patients";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    try{
                        ObjectMapper mapper = new ObjectMapper();
                        return mapper.readValue(response.body(), new TypeReference<List<Patient>>() {});
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                });
    }

    public CompletableFuture<Patient> createPatient(Patient patient) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(patient);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/patients/" + Session.getCurrentUser().getId()))
                    .header(
                            "Content-Type",
                            "application/json"
                    )
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpClient client = HttpClient.newHttpClient();

            return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(response -> {
                        try {
                            return mapper.readValue(response.body(), Patient.class);
                        } catch (Exception e) {
                            e.printStackTrace();
                            return null;
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            return CompletableFuture.completedFuture(null);
        }
    }

    public CompletableFuture<Patient> updatePatient(Patient patient) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(patient);

            HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create("http://localhost:8080/patients/" + patient.getId()))
                            .header(
                                    "Content-Type",
                                    "application/json"
                            )
                            .PUT(HttpRequest.BodyPublishers.ofString(json))
                            .build();

            HttpClient client = HttpClient.newHttpClient();

            return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(response -> {
                        try {
                            return mapper.readValue(response.body(), Patient.class);
                        } catch (Exception e) {
                            e.printStackTrace();
                            return null;
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            return CompletableFuture.completedFuture(null);
        }
    }

    public CompletableFuture<Void> deletePatient(Integer id) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/patients/" + id))
                .DELETE()
                .build();

        HttpClient client = HttpClient.newHttpClient();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> null);
    }
}