package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.maper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;

import java.util.List;

@Qualifier("ItemServiceImpl")
@Service
public class ItemServiceImpl implements ItemService {
    ItemMapper itemMapper;
    ItemStorage itemStorage;

    @Autowired
    ItemServiceImpl(ItemMapper itemMapper, @Qualifier("ItemStorageImpl") ItemStorage itemStorage) {
        this.itemMapper = itemMapper;
        this.itemStorage = itemStorage;
    }

    @Override
    public Item addItem(long userId, ItemDto itemDto) {
        return null;
    }

    @Override
    public Item getItemById(long userId, long itemId) {
        return null;
    }

    @Override
    public List<Item> getItemsByOwnerId(long userId) {
        return null;
    }

    @Override
    public List<Item> getAllItems() {
        return null;
    }

    @Override
    public List<Item> getItemByString(long userId, String text) {
        return null;
    }

    @Override
    public Item update(long userId, long itemId, ItemDto itemDto) {
        return null;
    }

    @Override
    public void delete(long userId, Item item){
    }
}
