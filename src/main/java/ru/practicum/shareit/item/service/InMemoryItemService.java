package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.validation.exceptions.ValidateException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InMemoryItemService implements ItemService {
    private final ItemMapper itemMapper;
    private final ItemStorage itemStorage;
    private final UserService userService;

    @Autowired
    InMemoryItemService(ItemMapper itemMapper, ItemStorage itemStorage, UserService userService) {
        this.itemMapper = itemMapper;
        this.itemStorage = itemStorage;
        this.userService = userService;
    }

    @Override
    public ItemDto addItem(int userId, ItemDto itemDto) {
        checkUsers(userId);

        Item newItem = itemMapper.toItem(itemDto);
        newItem.setOwnerId(userId);

        return itemMapper.toItemDto(itemStorage.save(newItem));
    }

    @Override
    public ItemDto getItemById(int userId, int itemId) {
        checkUsers(userId);

        return itemMapper.toItemDto(itemStorage.found(itemId));
    }

    @Override
    public List<ItemDto> getItemsByOwnerId(int userId) {
        checkUsers(userId);

        List<Item> allItems = getAllItems();

        return allItems.stream()
                .filter(item -> item.getOwnerId() == userId)
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> getAllItems() {
        return itemStorage.getAll();
    }

    @Override
    public List<ItemDto> getItemByString(int userId, String text) {
        checkUsers(userId);

        if (text.isBlank() || text.isEmpty()) {
            return new ArrayList<>();
        }

        List<Item> foundAllItems = getAllItems();

        return foundAllItems.stream()
                .filter(item -> (item.getDescription().toLowerCase().contains(text.toLowerCase()) ||
                        item.getName().toLowerCase().contains(text.toLowerCase())) && item.getAvailable())
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto update(int userId, int itemId, ItemDto itemDto) {
        checkUsers(userId);

        Item newItem = itemMapper.toItem(itemDto);
        newItem.setOwnerId(userId);
        newItem.setId(itemId);

        Item saveItem = itemStorage.found(itemId);
        Item lastItem = saveItem;

        if (newItem.getOwnerId() != saveItem.getOwnerId()) {
            throw new ValidateException("item id - " + itemId + " have other ownerId - " + saveItem.getOwnerId());
        }

        if (newItem.getName() != null && !newItem.getName().isEmpty() && !newItem.getName().isBlank()
                && !newItem.getName().equals(saveItem.getName())) {
            saveItem.setName(newItem.getName());
        }

        if (newItem.getDescription() != null && !newItem.getDescription().isEmpty() && !newItem.getDescription().isBlank()
                && !newItem.getDescription().equals(saveItem.getDescription())) {
            saveItem.setDescription(newItem.getDescription());
        }

        if (newItem.getAvailable() != null && !newItem.getAvailable().equals(saveItem.getAvailable())) {
            saveItem.setAvailable(newItem.getAvailable());
        }

        delete(userId, lastItem);

        return itemMapper.toItemDto(itemStorage.update(saveItem));
    }

    @Override
    public void delete(int userId, Item item) {
        checkUsers(userId);

        itemStorage.delete(item);
    }

    private void checkUsers(int userId) {
        UserDto user = userService.getUserById(userId);
    }
}
