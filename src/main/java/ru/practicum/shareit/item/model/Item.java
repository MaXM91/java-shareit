package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.request.ItemRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * Item entity.
 */
@Builder
@Getter
@Setter
@EqualsAndHashCode
public class Item {
    private int id;

    @NotBlank(message = "blank/empty name")
    private String name;

    @NotBlank(message = "blank/empty description")
    private String description;

    @NotNull(message = "availability must be true/false")
    private Boolean available;

    @Positive(message = "ownerId must be > 0")
    private int ownerId;

    private ItemRequest request;

    @Override
    public String toString() {
        return "\n" +
                "    Item{\n" +
                "         id = " + id + "\n" +
                "         name = " + name + "\n" +
                "         description = " + description + "\n" +
                "         available = " + available + "\n" +
                "         ownerId = " + ownerId + "\n" +
                "         request = " + request + "\n" +
                "        }";
    }
}
