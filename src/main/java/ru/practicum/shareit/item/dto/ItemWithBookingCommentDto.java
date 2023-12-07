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

    public ItemWithBookingCommentDto(int id, String name, String description, Boolean available) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
    }

    public ItemWithBookingCommentDto(int id, String name, String description, Boolean available,
                                     BookingForItemDto lastBooking, BookingForItemDto nextBooking) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.lastBooking = lastBooking;
        this.nextBooking = nextBooking;
    }

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