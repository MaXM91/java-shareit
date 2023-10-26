package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
public class BookingDto {
    private long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Item item;
}
