package ru.practicum.shareit.validation.exception;

public class EmailRegisteredException extends RuntimeException {
    public EmailRegisteredException(String message) {
        super(message);
    }
}
