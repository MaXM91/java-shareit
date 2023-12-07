package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.booking.StatusBooking;

import java.time.LocalDateTime;


@Builder
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class BookingRequestDto {
    private Integer id;

    private LocalDateTime start;

    private LocalDateTime end;

    private StatusBooking status;

    UserBookingDto booker;

    ItemBookingDto item;

    @Override
    public String toString() {
        return "\n" +
                "    BookingRequestDto{\n" +
                "                      id = " + id + "\n" +
                "                      start = " + start + "\n" +
                "                      end = " + end + "\n" +
                "                      status = " + status + "\n" +
                "                      user = " + booker + "\n" +
                "                      item = " + item + "\n" +
                "                     }";
    }
}
