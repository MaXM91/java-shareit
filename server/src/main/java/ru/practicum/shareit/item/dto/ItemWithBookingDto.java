package ru.practicum.shareit.item.dto;

import lombok.*;

@Builder
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class ItemWithBookingDto {
    private Integer id;

    private String name;

    private String description;

    private Boolean available;

    private BookingForItemDto lastBooking;

    private BookingForItemDto nextBooking;

    @Override
    public String toString() {
        return "\n" +
                "    ItemWithBookingDto{\n" +
                "                       id = " + id + "\n" +
                "                       name = " + name + "\n" +
                "                       description = " + description + "\n" +
                "                       available = " + available + "\n" +
                "                       lastBooking = " + lastBooking + "\n" +
                "                       nextBooking = " + nextBooking + "\n" +
                "                      }";
    }
}
