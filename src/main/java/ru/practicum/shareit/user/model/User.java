package ru.practicum.shareit.user.model;

/**
 * TODO Sprint add-controllers.
 */

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * Basic entity User
 */
@Data
@Builder
public class User {
    private int id;

    @NotBlank(message = "blank/empty email")
    @Email(message = "bad email")
    private String email;

    @NotBlank(message = "blank/empty name")
    private String name;
}

