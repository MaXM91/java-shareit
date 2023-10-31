package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * TODO Sprint add-controllers.
 */
@Data
@AllArgsConstructor
public class Item {
    private int id;

    @NotBlank(message = "blank/empty name")
    private String name;

    @NotBlank(message = "blank/empty description")
    private String description;

    @NotNull(message = "availability must be true/false")
    private Boolean available;

    @Positive(message = "ownerId must be > 0")
    private long ownerId;

    private ItemRequest request;
}
