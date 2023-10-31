package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */
@Data
@AllArgsConstructor
public class ItemDto {
    private Long id;

    @NotBlank(message = "blank/empty name")
    private String name;

    @NotBlank(message = "blank/empty description")
    private String description;

    @NotNull(message = "availability must be true/false")
    private Boolean available;
}
