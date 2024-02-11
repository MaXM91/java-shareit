package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.enums.StateBooking;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Validated
@Slf4j
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingClient bookingClient;
    private static final String UserId = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> addBooking(@Positive @RequestHeader(UserId) Integer userId,
                                             @Valid @RequestBody BookingDto bookingDto) {
        log.info("request POST/addBooking : {}, {}", userId, bookingDto);

        return bookingClient.postBooking(userId, bookingDto);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingById(@Positive @RequestHeader(UserId) Integer userId, @PathVariable Long bookingId) {
        log.info("request GET/getBookingById : {}, {}", userId, bookingId);

        return bookingClient.getBooking(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getBookingsByUserId(@Positive @RequestHeader(UserId) Integer userId,
                                                        @NotNull(message = "state must not be null") @RequestParam(defaultValue = "ALL") StateBooking state,
                                                        @RequestParam(value = "from", defaultValue = "0") @Min(0) Integer from,
                                                        @RequestParam(value = "size", defaultValue = "20") @Min(1) @Max(20) Integer size) {
        log.info("request GET/getBookingsByUserId : {}, {}, {}, {}", userId, state, from, size);

        return bookingClient.getBookingsByUserId(userId, state, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getBookingsByOwnerItem(@Positive @RequestHeader(UserId) Integer userId,
                                                           @NotNull(message = "state must not be null") @RequestParam(defaultValue = "ALL") StateBooking state,
                                                           @RequestParam(value = "from", defaultValue = "0") @Min(0) Integer from,
                                                           @RequestParam(value = "size", defaultValue = "20") @Min(1) @Max(20) Integer size) {
        log.info("request GET/getBookingsByOwnerItem : {}, {}, {}, {}", userId, state, from, size);

        return bookingClient.getBookingsByOwnerItem(userId, state, from, size);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> changeStatusBooking(@Positive @RequestHeader(UserId) Integer userId,
                                                  @PathVariable Integer bookingId,
                                                  @RequestParam boolean approved) {
        log.info("request PATCH/changeStatusBooking : {}, {}", bookingId, approved);

        return bookingClient.changeStatusBooking(userId, bookingId, approved);
    }
}