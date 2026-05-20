package com.cabinet.client.service;

import com.cabinet.client.model.Consultation;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ConsultationService {
    public CompletableFuture<List<Consultation>> getConsultations(Integer doctorId) {
        String url = "http://localhost:8080/consultations/doctor/" + doctorId;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
            try {
                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(response.body(), new TypeReference<List<Consultation>>() {});
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        });
    }

    public CompletableFuture<Consultation> createConsultation(Consultation consultation) {
        String url = "http://localhost:8080/consultations";

        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(consultation);
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(response -> {
                        try {
                            return mapper.readValue(response.body(), Consultation.class);
                        } catch (Exception e) {
                            e.printStackTrace();
                            return null;
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            return CompletableFuture.failedFuture(e);
        }
    }

    public CompletableFuture<List<Consultation>> getAllConsultations() {
        String url = "http://localhost:8080/consultations";

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
                                new TypeReference<List<Consultation>>() {}
                        );
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                });
    }
}