package ru.practicum.shareit.booking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.storage.BookingStorage;

@Qualifier("BookingServiceImpl")
@Service
public class BookingServiceImpl implements BookingService {
    BookingMapper bookingMapper;
    BookingStorage bookingStorage;

    @Autowired
    BookingServiceImpl(BookingMapper bookingMapper,
                       @Qualifier("BookingStorageImpl") BookingStorage bookingStorage) {
        this.bookingMapper = bookingMapper;
        this.bookingStorage = bookingStorage;
    }
}
