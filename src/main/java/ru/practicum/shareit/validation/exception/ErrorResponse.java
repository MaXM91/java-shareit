package ru.practicum.shareit.validation.exception;

import lombok.Data;

@Data
public class ErrorResponse {
    String error;

    public ErrorResponse(String error) {
        this.error = error;
    }
}
