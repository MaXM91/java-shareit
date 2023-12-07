package ru.practicum.shareit.item.storage.memory;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface InMemoryItemStorage {
    Item save(Item item);

    Item found(int itemId);

    List<Item> getAll();

    Item update(Item item);

    void delete(Item item);
}
