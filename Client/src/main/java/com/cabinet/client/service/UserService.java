package com.cabinet.client.service;

import com.cabinet.client.dto.CreateUserRequest;
import com.cabinet.client.dto.UpdateUserRequest;
import com.cabinet.client.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class UserService {
    private static final String BASE_URL = "http://localhost:8080/users";
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public CompletableFuture<List<User>> getAllUsers() {
        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(URI.create(BASE_URL))
                        .GET()
                        .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    try {
                        return mapper.readValue(
                                response.body(),
                                new TypeReference<List<User>>() {}
                        );
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                });
    }

    public CompletableFuture<User> createUser(CreateUserRequest requestBody) {
        try {
            String json = mapper.writeValueAsString(requestBody);

            HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create(BASE_URL))
                            .header(
                                    "Content-Type",
                                    "application/json"
                            )
                            .POST(HttpRequest.BodyPublishers.ofString(json))
                            .build();

            return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(response -> {
                        try {
                            return mapper.readValue(
                                    response.body(),
                                    User.class
                            );
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

    public CompletableFuture<User> updateUser(Integer id, UpdateUserRequest requestBody) {
        try {
            String json = mapper.writeValueAsString(requestBody);

            HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create(BASE_URL + "/" + id))
                            .header(
                                    "Content-Type",
                                    "application/json"
                            )
                            .PUT(HttpRequest.BodyPublishers.ofString(json))
                            .build();

            return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(response -> {
                        try {
                            return mapper.readValue(
                                    response.body(),
                                    User.class
                            );
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

    public CompletableFuture<Void> deleteUser(Integer id) {
        HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(BASE_URL + "/" + id))
                        .DELETE()
                        .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.discarding())
                .thenApply(response -> null);
    }
}