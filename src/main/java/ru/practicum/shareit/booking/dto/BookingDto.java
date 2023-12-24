package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.booking.StatusBooking;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
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

    @NotNull(message = "start must be not null")
    @FutureOrPresent(message = "start must future or present")
    private LocalDateTime start;

    @NotNull(message = "end must be not null")
    @Future(message = "end must future")
    private LocalDateTime end;

    private StatusBooking status;

    private Integer bookerId;

    @Positive(message = "item id must be > 0")
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
