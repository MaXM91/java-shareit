package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.maper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.validation.exception.ValidateException;

import java.util.ArrayList;
import java.util.List;

@Qualifier("InMemoryItemService")
@Service
public class InMemoryItemService implements ItemService {
    ItemMapper itemMapper;
    ItemStorage itemStorage;
    UserService userService;

    @Autowired
    InMemoryItemService(ItemMapper itemMapper, @Qualifier("InMemoryItemStorage") ItemStorage itemStorage,
                        @Qualifier("InMemoryUserService") UserService userService) {
        this.itemMapper = itemMapper;
        this.itemStorage = itemStorage;
        this.userService = userService;
    }

    @Override
    public Item addItem(long userId, ItemDto itemDto) {
        checkUsers(userId);

        Item newItem = itemMapper.toItem(itemDto);
        newItem.setOwnerId(userId);

        return itemStorage.save(newItem);
    }

    @Override
    public Item getItemById(long userId, long itemId) {
        checkUsers(userId);

        return itemStorage.found(itemId);
    }

    @Override
    public List<Item> getItemsByOwnerId(long userId) {
        checkUsers(userId);

        List<Item> foundItems = new ArrayList<>();
        List<Item> allItems = getAllItems();

        for (Item item : allItems) {
            if (item.getOwnerId() == userId) {
                foundItems.add(item);
            }
        }

        return foundItems;
    }

    @Override
    public List<Item> getAllItems() {
        return itemStorage.getAll();
    }

    @Override
    public List<Item> getItemByString(long userId, String text) {
        checkUsers(userId);

        List<Item> foundAllItems = getAllItems();
        List<Item> response = new ArrayList<>();

        if (text.isBlank() || text.isEmpty()) {
            return new ArrayList<>();
        }

        for (Item item : foundAllItems) {
            if ((item.getDescription().toLowerCase().contains(text.toLowerCase()) ||
                    item.getName().toLowerCase().contains(text.toLowerCase())) && item.getAvailable()) {
                response.add(item);
            }
        }

        return response;
    }

    @Override
    public Item update(long userId, long itemId, ItemDto itemDto) {
        checkUsers(userId);

        Item newItem = itemMapper.toItem(itemDto);
        newItem.setOwnerId(userId);
        newItem.setId(itemId);

        Item saveItem = getItemById(userId, itemId);
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

        return itemStorage.update(saveItem);
    }

    @Override
    public void delete(long userId, Item item) {
        checkUsers(userId);

        itemStorage.delete(item);
    }

    private void checkUsers(long userId) {
        User user = userService.getUserById(userId);
    }
}