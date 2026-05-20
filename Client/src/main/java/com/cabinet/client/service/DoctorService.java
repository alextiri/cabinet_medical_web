package com.cabinet.client.service;

import com.cabinet.client.model.Doctor;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DoctorService {
    private static final String BASE_URL = "http://localhost:8080/doctors";

    private final HttpClient client;
    private final ObjectMapper mapper;

    public DoctorService() {
        client = HttpClient.newHttpClient();
        mapper = new ObjectMapper();
    }

    public CompletableFuture<Doctor> getDoctor(Integer id) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id))
                .GET()
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(body -> {
                    try {
                        return mapper.readValue(body, Doctor.class);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    public CompletableFuture<List<Doctor>> getAllDoctors() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .GET()
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(body -> {
                    try {
                        return Arrays.asList(
                                mapper.readValue(body, Doctor[].class)
                        );
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    public CompletableFuture<Void> updateDoctor(Doctor doctor) {
        try {
            String json = mapper.writeValueAsString(doctor);
            System.out.println(json);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/" + doctor.getId()))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(response -> {
                        System.out.println(response.statusCode());
                        System.out.println(response.body());
                    });
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}