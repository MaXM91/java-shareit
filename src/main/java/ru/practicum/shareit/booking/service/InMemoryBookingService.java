package ru.practicum.shareit.booking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.storage.BookingStorage;

@Qualifier("InMemoryBookingService")
@Service
public class InMemoryBookingService implements BookingService {
    BookingMapper bookingMapper;
    BookingStorage bookingStorage;

    @Autowired
    InMemoryBookingService(BookingMapper bookingMapper,
                           @Qualifier("InMemoryBookingStorage") BookingStorage bookingStorage) {
        this.bookingMapper = bookingMapper;
        this.bookingStorage = bookingStorage;
    }
}
