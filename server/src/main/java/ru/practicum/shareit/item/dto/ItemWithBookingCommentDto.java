package ru.practicum.shareit.item.dto;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class ItemWithBookingCommentDto {
    private Integer id;

    private String name;

    private String description;

    private Boolean available;

    private BookingForItemDto lastBooking;

    private BookingForItemDto nextBooking;

    private List<CommentDto> comments;

    @Override
    public String toString() {
        return "\n" +
                "    ItemWithBookingCommentDto{\n" +
                "                             id = " + id + "\n" +
                "                             name = " + name + "\n" +
                "                             description = " + description + "\n" +
                "                             available = " + available + "\n" +
                "                             lastBooking = " + lastBooking + "\n" +
                "                             nextBooking = " + nextBooking + "\n" +
                "                             comments = " + comments + "\n" +
                "                      }";
    }
}