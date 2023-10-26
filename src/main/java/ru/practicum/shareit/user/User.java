package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * TODO Sprint add-controllers.
 */
@Data
@AllArgsConstructor
public class User {
    private long id;

    @NotBlank(message = "blank/empty email")
    @Email(message = "bad email")
    private String email;

    @NotBlank(message = "blank/empty name")
    private String name;
}
