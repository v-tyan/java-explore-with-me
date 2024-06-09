package ru.practicum.ewm.controller.errors;

import lombok.Getter;

@Getter
public class ErrorResponse {
    private String error;
    private String description;

    public ErrorResponse(String error) {
        this.error = error;
    }

    public ErrorResponse(String error, String description) {
        this.error = error;
        this.description = description;
    }

}
