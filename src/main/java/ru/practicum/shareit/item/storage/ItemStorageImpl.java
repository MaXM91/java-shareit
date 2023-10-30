package ru.practicum.shareit.item.storage;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Qualifier("ItemStorageImpl")
@Repository
public class ItemStorageImpl implements ItemStorage {
    @Override
    public Item save(Item item) {
        return null;
    }

    @Override
    public Item found(long itemId) {
        return null;
    }

    @Override
    public List<Item> getAll() {
        return null;
    }

    @Override
    public Item update(Item item) {
        return null;
    }

    @Override
    public void delete(Item item) {

    }
}
