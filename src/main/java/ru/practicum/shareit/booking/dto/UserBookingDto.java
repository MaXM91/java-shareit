package ru.practicum.shareit.booking.dto;

import lombok.*;

@Builder
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class UserBookingDto {
    private int id;

    @Override
    public String toString() {
        return "\n" +
                "   UserBookingDto{\n" +
                "                  id = " + id + "\n" +
                "                 }";
    }
}
