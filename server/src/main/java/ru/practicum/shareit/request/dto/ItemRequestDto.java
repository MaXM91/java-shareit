package ru.practicum.shareit.request.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Item-requests entity.
 */
@Data
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestDto {
    private Integer id;

    private String description;

    private Integer requester;

    private LocalDateTime created = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

    @Override
    public String toString() {
        return "\n" +
                "    ItemRequestDto{\n" +
                "                   id = " + id + "\n" +
                "                   description = " + description + "\n" +
                "                   requester = " + requester + "\n" +
                "                   created = " + created.truncatedTo(ChronoUnit.SECONDS) + "\n" +
    //            "                   items = " + items + "\n" +
                "        }";
    }
}