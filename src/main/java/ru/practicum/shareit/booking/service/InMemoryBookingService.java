package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.storage.BookingStorage;

@Service
@RequiredArgsConstructor
public class InMemoryBookingService implements BookingService {
    private final BookingMapper bookingMapper;
    private final BookingStorage bookingStorage;
}
