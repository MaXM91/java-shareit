package ru.practicum.shareit.item.dto;

import lombok.*;

/**
 * itemDto.
 */
@Builder
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {
    private Integer id;

    private String name;

    private String description;

    private Boolean available;

    private Integer requestId;

    @Override
    public String toString() {
        return "\n" +
               "    ItemDto{\n" +
               "            id = " + id + "\n" +
               "            name = " + name + "\n" +
               "            description = " + description + "\n" +
               "            available = " + available + "\n" +
               "            requestId = " + requestId + "\n" +
               "           }";
    }
}
