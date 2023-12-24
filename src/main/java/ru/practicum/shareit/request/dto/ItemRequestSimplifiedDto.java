package ru.practicum.shareit.request.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Builder
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestSimplifiedDto {
    private int id;

    private String description;

    private ShortUserDto requester;

    private LocalDateTime created;

    private List<ShortItemDto> items;

    @Override
    public String toString() {
        return "\n" +
                "    ItemRequestSimplifiedDto{\n" +
                "                             id = " + id + "\n" +
                "                             description = " + description + "\n" +
                "                             requester = " + requester + "\n" +
                "                             created = " + created.truncatedTo(ChronoUnit.SECONDS) + "\n" +
                "                             items = " + items + "\n" +
                "        }";
    }
}
