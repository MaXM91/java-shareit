package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.booking.StatusBooking;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

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

    @NotNull
    private LocalDateTime start;

    @NotNull
    private LocalDateTime end;

    private StatusBooking status;

    private Integer bookerId;

    @Positive
    private Integer itemId;

    private String itemName;

    @Override
    public String toString() {
        return "\n" +
                "    bookingDto{\n" +
                "               id = " + id + "\n" +
                "               start = " + start + "\n" +
                "               end = " + end + "\n" +
                "               status = " + status + "\n" +
                "               bookerId = " + bookerId + "\n" +
                "               itemId = " + itemId + "\n" +
                "               itemName = " + itemName + "\n" +
                "              }";
    }
}
