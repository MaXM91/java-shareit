package ru.practicum.shareit.request.dto;

import org.springframework.beans.factory.annotation.Value;

public interface ShortRequest {
    @Value("#{target.id}")
    int getId();
}
