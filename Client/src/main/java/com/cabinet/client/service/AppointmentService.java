package com.cabinet.client.service;

import com.cabinet.client.model.Appointment;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class AppointmentService {
    public CompletableFuture<List<Appointment>> getAppointments(Integer doctorId) {
        String url = "http://localhost:8080/appointments/" + doctorId;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).
                thenApply(response -> {
            try {
                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(response.body(), new TypeReference<List<Appointment>>() {});
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        });
    }

    public CompletableFuture<Appointment> createAppointment(Appointment appointment) {
        try {
            ObjectMapper mapper = new ObjectMapper();

            String json = mapper.writeValueAsString(appointment);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/appointments"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpClient client = HttpClient.newHttpClient();

            return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(response -> {
                        try {
                            return mapper.readValue(response.body(), Appointment.class);
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

    public CompletableFuture<Appointment> updateAppointment(Appointment appointment) {
        try {
            ObjectMapper mapper = new ObjectMapper();

            String json = mapper.writeValueAsString(appointment);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/appointments/" + appointment.getId()))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpClient client = HttpClient.newHttpClient();

            return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(response -> {
                        try {
                            return mapper.readValue(response.body(), Appointment.class);
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

    public CompletableFuture<List<Appointment>> getAllAppointments() {
        String url = "http://localhost:8080/appointments";

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
                                new TypeReference<List<Appointment>>() {}
                        );
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                });
    }

    public CompletableFuture<Void> deleteAppointment(Integer id) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/appointments/" + id))
                .DELETE()
                .build();

        HttpClient client = HttpClient.newHttpClient();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(response -> {});
    }
}