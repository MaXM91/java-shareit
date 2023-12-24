package ru.practicum.shareit.item.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Builder
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class ItemWithBookingCommentDto {
    private Integer id;

    @NotBlank(message = "blank/empty name")
    private String name;

    @NotBlank(message = "blank/empty description")
    private String description;

    @NotNull(message = "availability must be true/false")
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