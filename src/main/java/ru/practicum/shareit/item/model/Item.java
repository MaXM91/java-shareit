package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

/**
 * TODO Sprint add-controllers.
 */
@Data
@AllArgsConstructor
public class Item {
    private final long id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    private Boolean isAvailable;
    @Positive
    private long ownerId;
    private ItemRequest request;
}
