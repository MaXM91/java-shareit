package ru.practicum.shareit.request.dto;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
public class ItemRequestDto {
    private long id;
    private String description;
    private LocalDateTime created = LocalDateTime.now();
}
