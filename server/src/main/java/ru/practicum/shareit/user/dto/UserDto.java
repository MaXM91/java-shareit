package ru.practicum.shareit.user.dto;

import lombok.*;

/**
 * UserDto.
 */
@Data
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Integer id;

    private String name;

    private String email;

    @Override
    public String toString() {
        return "\n" +
                "    UserDto{\n" +
                "            id = " + id + "\n" +
                "            email = " + email + "\n" +
                "            name = " + name + "\n" +
                "           }";
    }
}
