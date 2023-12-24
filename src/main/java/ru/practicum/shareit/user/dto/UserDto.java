package ru.practicum.shareit.user.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

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

    @NotBlank(message = "blank/empty name")
    private String name;

    @NotBlank(message = "blank/empty email")
    @Email(message = "bad email")
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
