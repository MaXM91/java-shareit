package ru.practicum.shareit.item.dto;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor

public class BookingForItemDto {
    private int id;
    private int bookerId;
    private LocalDateTime start;
    private LocalDateTime end;

    public BookingForItemDto(Integer id, Integer bookerId, LocalDateTime start, LocalDateTime end) {
        this.id = id;
        this.bookerId = bookerId;
        this.start = start;
        this.end = end;
    }

    @Override
    public String toString() {
        return "\n" +
                "   BookingForItemDto{\n" +
                "                     id = " + id + "\n" +
                "                     bookerId = " + bookerId + "\n" +
                "                     start = " + start + "\n" +
                "                     end = " + end + "\n" +
                "                    }";
    }

}