package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.booking.StatusBooking;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * bookingsDto.
 */
@Builder
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto {
    private Integer id;

    private LocalDateTime start;

    private LocalDateTime end;

    private StatusBooking status;

    private Integer bookerId;

    private Integer itemId;

    private String itemName;

    @Override
    public String toString() {
        return "\n" +
                "    bookingDto{\n" +
                "               id = " + id + "\n" +
                "               start = " + start.truncatedTo(ChronoUnit.SECONDS) + "\n" +
                "               end = " + end.truncatedTo(ChronoUnit.SECONDS) + "\n" +
                "               status = " + status + "\n" +
                "               bookerId = " + bookerId + "\n" +
                "               itemId = " + itemId + "\n" +
                "               itemName = " + itemName + "\n" +
                "              }";
    }
}
