package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class UserDto {

    @NotBlank(message = "blank/empty email")
    @Email(message = "bad email")
    private String email;
    @NotBlank(message = "blank/empty name")
    private String name;
}
