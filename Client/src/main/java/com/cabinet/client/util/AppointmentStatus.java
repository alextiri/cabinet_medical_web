package com.cabinet.client.util;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum AppointmentStatus {
    SCHEDULED,
    COMPLETED,
    CANCELLED;

    @JsonCreator
    public static AppointmentStatus fromString(String value) {
        return switch (value.toLowerCase()) {
            case "scheduled" ->
                    SCHEDULED;
            case "completed" ->
                    COMPLETED;
            case "cancelled" ->
                    CANCELLED;

            default ->
                    throw new IllegalArgumentException("Unknown status: " + value);
        };
    }
}