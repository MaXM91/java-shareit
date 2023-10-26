package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.service.BookingService;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    BookingService bookingService;

    BookingController(@Qualifier("InMemoryBookingService") BookingService bookingService) {
        this.bookingService = bookingService;
    }
}
