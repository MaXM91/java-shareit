package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.StateBooking;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;

import java.util.List;

public interface BookingService {
    BookingRequestDto addBooking(Integer userId, BookingDto bookingDto);

    BookingRequestDto getBookingById(int userId, int bookingId);

    List<BookingRequestDto> getBookingsByUserId(int userId, StateBooking state, Integer from, Integer size);

    BookingRequestDto updateStatusBooking(int userId, int bookingId, boolean approved);

    List<BookingRequestDto> getBookingsByOwnerItem(int userId, StateBooking state, Integer from, Integer size);

    void checkBookingByUserIdAndItemId(int userId, int itemId);
}

