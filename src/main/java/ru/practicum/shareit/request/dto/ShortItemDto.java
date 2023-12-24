package ru.practicum.shareit.request.dto;

import lombok.*;

@Builder
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class ShortItemDto {
    private int id;

    private String name;

    private String description;

    private boolean available;

    private int requestId;

    @Override
    public String toString() {
        return "\n" +
                "    ShortItemDto{\n" +
                "                 id = " + id + "\n" +
                "                 name = " + name + "\n" +
                "                 description = " + description + "\n" +
                "                 available = " + available + "\n" +
                "                 requestId = " + requestId + "\n" +
                "        }";
    }
}
