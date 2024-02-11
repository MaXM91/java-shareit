package ru.practicum.shareit.booking.dto;

import lombok.*;

@Builder
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class ItemBookingDto {
    private int id;

    private String name;

    @Override
    public String toString() {
        return "\n" +
                "   ItemBookingDto{\n" +
                "                  id = " + id + "\n" +
                "                  name = " + name + "\n" +
                "                 }";
    }
}
