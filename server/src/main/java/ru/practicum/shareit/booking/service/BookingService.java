package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.StateBooking;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

import java.util.List;

public interface BookingService {
    BookingResponseDto addBooking(Integer userId, BookingDto bookingDto);

    BookingResponseDto getBookingById(int userId, int bookingId);

    List<BookingResponseDto> getBookingsByUserId(int userId, StateBooking state, Integer from, Integer size);

    BookingResponseDto updateStatusBooking(int userId, int bookingId, boolean approved);

    List<BookingResponseDto> getBookingsByOwnerItem(int userId, StateBooking state, Integer from, Integer size);

    void checkBookingByUserIdAndItemId(int userId, int itemId);
}

