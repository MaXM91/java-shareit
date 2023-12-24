package ru.practicum.shareit.item.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Builder
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class BookingForItemDto {
    private int id;
    private int bookerId;
    private LocalDateTime start;
    private LocalDateTime end;
    private int itemId;

    @Override
    public String toString() {
        return "\n" +
                "   BookingForItemDto{\n" +
                "                     id = " + id + "\n" +
                "                     bookerId = " + bookerId + "\n" +
                "                     start = " + start.truncatedTo(ChronoUnit.SECONDS) + "\n" +
                "                     end = " + end.truncatedTo(ChronoUnit.SECONDS) + "\n" +
                "                     itemId = " + itemId + "\n" +
                "                    }";
    }

}