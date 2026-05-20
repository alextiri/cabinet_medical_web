package com.cabinet.client.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class AuthService {
    public CompletableFuture<String> login(String username, String password) {
        String url = "http://localhost:8080/login" +
                "?username=" + username +
                "&password=" + password;

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).
                thenApply(HttpResponse::body);
    }
}