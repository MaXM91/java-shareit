package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    ItemDto addItem(int userId, ItemDto itemDto);

    ItemDto getItemById(int userId, int itemId);

    List<ItemDto> getItemsByOwnerId(int userId);

    List<Item> getAllItems();

    List<ItemDto> getItemByString(int userId, String text);

    ItemDto update(int userId, int itemId, ItemDto itemDto);

    void delete(int userId, Item item);
}
