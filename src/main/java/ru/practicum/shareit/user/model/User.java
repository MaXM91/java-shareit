package ru.practicum.shareit.user.model;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * Basic entity User
 */
@Builder
@Getter
@Setter
@EqualsAndHashCode
public class User {
    private int id;

    @NotBlank(message = "blank/empty email")
    @Email(message = "bad email")
    private String email;

    @NotBlank(message = "blank/empty name")
    private String name;

    @Override
    public String toString() {
        return "\n" +
            "    User{\n" +
            "         id = " + id + "\n" +
            "         email = " + email + "\n" +
            "         name = " + name + "\n" +
            "        }";
    }
}

