package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item addItem(long userId, ItemDto itemDto);

    Item getItemById(long userId, long itemId);

    List<Item> getItemsByOwnerId(long userId);

    List<Item> getAllItems();

    List<Item> getItemByString(long userId, String text);

    Item update(long userId, long itemId, ItemDto itemDto);

    void delete(long userId, Item item);
}
