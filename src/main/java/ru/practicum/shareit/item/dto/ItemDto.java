package ru.practicum.shareit.item.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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

    @NotBlank(message = "blank/empty name")
    private String name;

    @NotBlank(message = "blank/empty description")
    private String description;

    @NotNull(message = "availability must be true/false")
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
