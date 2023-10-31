package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item addItem(int userId, ItemDto itemDto);

    Item getItemById(int userId, int itemId);

    List<Item> getItemsByOwnerId(int userId);

    List<Item> getAllItems();

    List<Item> getItemByString(int userId, String text);

    Item update(int userId, int itemId, ItemDto itemDto);

    void delete(int userId, Item item);
}
