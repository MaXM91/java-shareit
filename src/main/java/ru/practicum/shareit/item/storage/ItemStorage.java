package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {
    Item save(Item item);

    Item found(int itemId);

    List<Item> getAll();

    Item update(Item item);

    void delete(Item item);
}