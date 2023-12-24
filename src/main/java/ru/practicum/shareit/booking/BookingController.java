package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

/**
 * TODO Sprint add-bookings.
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
    public BookingResponseDto addBooking(@Positive @RequestHeader(UserId) Integer userId,
                                         @Valid @RequestBody BookingDto bookingDto) {
        log.info("request POST/addBooking : {}, {}", userId, bookingDto);

        BookingResponseDto responseObject = bookingService.addBooking(userId, bookingDto);
        log.info("response POST/addBooking : {}", responseObject);

        return responseObject;
    }

    @GetMapping("/{bookingId}")
    public BookingResponseDto getBookingById(@Positive @RequestHeader(UserId) Integer userId, @PathVariable Integer bookingId) {
        log.info("request GET/getBookingById : {}, {}", userId, bookingId);

        BookingResponseDto responseObject = bookingService.getBookingById(userId, bookingId);
        log.info("response GET/getBookingById : {}", responseObject);

        return responseObject;
    }

    @GetMapping
    public List<BookingResponseDto> getBookingsByUserId(@Positive @RequestHeader(UserId) Integer userId,
                                                        @NotNull(message = "state must not be null") @RequestParam(defaultValue = "ALL") StateBooking state,
                                                        @RequestParam(value = "from", required = false, defaultValue = "0") @Min(0) Integer from,
                                                        @RequestParam(value = "size", required = false, defaultValue = "20") @Min(1) @Max(20) Integer size) {
        log.info("request GET/getBookingsByUserId : {}, {}, {}, {}", userId, state, from, size);

        List<BookingResponseDto> responseObject = bookingService.getBookingsByUserId(userId, state, from, size);
        log.info("response GET/getBookingsByUserId : {}", responseObject);

        return responseObject;
    }

    @GetMapping("/owner")
    public List<BookingResponseDto> getBookingsByOwnerItem(@Positive @RequestHeader(UserId) Integer userId,
                                                           @NotNull(message = "state must not be null") @RequestParam(defaultValue = "ALL") StateBooking state,
                                                           @RequestParam(value = "from", required = false, defaultValue = "0") @Min(0) Integer from,
                                                           @RequestParam(value = "size", required = false, defaultValue = "20") @Min(1) @Max(20) Integer size) {
        log.info("request GET/getBookingsByOwnerItem : {}, {}, {}, {}", userId, state, from, size);

        List<BookingResponseDto> responseObject = bookingService.getBookingsByOwnerItem(userId, state, from, size);
        log.info("response GET/getBookingsByOwnerItem : {}", responseObject);

        return responseObject;
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto changeStatusBooking(@Positive @RequestHeader(UserId) Integer userId,
                                                  @PathVariable Integer bookingId,
                                                  @RequestParam boolean approved) {
        log.info("request PATCH/changeStatusBooking : {}, {}", bookingId, approved);

        BookingResponseDto responseObject = bookingService.updateStatusBooking(userId, bookingId, approved);
        log.info("response PATCH/changeStatusBooking : {}", responseObject);

        return responseObject;
    }
}
