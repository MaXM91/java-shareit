package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.booking.enums.StatusBooking;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;


@Builder
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponseDto {
    private Integer id;

    private LocalDateTime start;

    private LocalDateTime end;

    private StatusBooking status;

    private UserBookingDto booker;

    private ItemBookingDto item;

    @Override
    public String toString() {
        return "\n" +
                "    BookingRequestDto{\n" +
                "                      id = " + id + "\n" +
                "                      start = " + start.truncatedTo(ChronoUnit.SECONDS) + "\n" +
                "                      end = " + end.truncatedTo(ChronoUnit.SECONDS) + "\n" +
                "                      status = " + status + "\n" +
                "                      user = " + booker + "\n" +
                "                      item = " + item + "\n" +
                "                     }";
    }
}