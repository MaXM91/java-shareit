package ru.practicum.shareit.booking.dto;

import org.springframework.beans.factory.annotation.Value;
import ru.practicum.shareit.booking.StatusBooking;

import java.time.LocalDateTime;

public interface BookingRequestPageableDto {
    int getId();

    LocalDateTime getStart();

    LocalDateTime getEnd();

    StatusBooking getStatus();

    @Value("#{target.user.id}")
    int getUser();

    ItemBookingPageableDto getItem();
}
