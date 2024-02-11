package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

/**
 * Booking controller.
 */
@Validated
@Slf4j
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private static final String UserId = "X-Sharer-User-Id";
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public BookingResponseDto addBooking(@RequestHeader(UserId) Integer userId,
                                         @RequestBody BookingDto bookingDto) {
        log.info("request POST/addBooking : {}, {}", userId, bookingDto);

        BookingResponseDto responseObject = bookingService.addBooking(userId, bookingDto);
        log.info("response POST/addBooking : {}", responseObject);

        return responseObject;
    }

    @GetMapping("/{bookingId}")
    public BookingResponseDto getBookingById(@RequestHeader(UserId) Integer userId, @PathVariable Integer bookingId) {
        log.info("request GET/getBookingById : {}, {}", userId, bookingId);

        BookingResponseDto responseObject = bookingService.getBookingById(userId, bookingId);
        log.info("response GET/getBookingById : {}", responseObject);

        return responseObject;
    }

    @GetMapping
    public List<BookingResponseDto> getBookingsByUserId(@RequestHeader(UserId) Integer userId,
                                                        @RequestParam(defaultValue = "ALL") StateBooking state,
                                                        @RequestParam(value = "from", defaultValue = "0") Integer from,
                                                        @RequestParam(value = "size", defaultValue = "20") Integer size) {
        log.info("request GET/getBookingsByUserId : {}, {}, {}, {}", userId, state, from, size);

        List<BookingResponseDto> responseObject = bookingService.getBookingsByUserId(userId, state, from, size);
        log.info("response GET/getBookingsByUserId : {}", responseObject);

        return responseObject;
    }

    @GetMapping("/owner")
    public List<BookingResponseDto> getBookingsByOwnerItem(@RequestHeader(UserId) Integer userId,
                                                           @RequestParam(defaultValue = "ALL") StateBooking state,
                                                           @RequestParam(value = "from", defaultValue = "0") Integer from,
                                                           @RequestParam(value = "size", defaultValue = "20") Integer size) {
        log.info("request GET/getBookingsByOwnerItem : {}, {}, {}, {}", userId, state, from, size);

        List<BookingResponseDto> responseObject = bookingService.getBookingsByOwnerItem(userId, state, from, size);
        log.info("response GET/getBookingsByOwnerItem : {}", responseObject);

        return responseObject;
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto changeStatusBooking(@RequestHeader(UserId) Integer userId,
                                                  @PathVariable Integer bookingId,
                                                  @RequestParam boolean approved) {
        log.info("request PATCH/changeStatusBooking : {}, {}", bookingId, approved);

        BookingResponseDto responseObject = bookingService.updateStatusBooking(userId, bookingId, approved);
        log.info("response PATCH/changeStatusBooking : {}", responseObject);

        return responseObject;
    }
}
