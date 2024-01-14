package ru.practicum.shareit.request.dto;

import lombok.*;

@Builder
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class ShortUserDto {
    private int id;

    @Override
    public String toString() {
        return "\n" +
                "    ShortUserDto{\n" +
                "                 id = " + id + "\n" +
                "                }";
    }
}
