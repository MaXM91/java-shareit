package ru.practicum.shareit.item.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.validation.exception.ObjectNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class InMemoryItemStorage implements ItemStorage {
    private long id = 1;
    private final Map<Long, Item> items = new HashMap<>();

    @Override
    public Item save(Item item) {
        item.setId(id++);
        items.put(item.getId(), item);
        return items.get(item.getId());
    }

    @Override
    public Item found(long itemId) {
        return foundItem(itemId);
    }

    @Override
    public List<Item> getAll() {
        return new ArrayList<>(items.values());
    }

    @Override
    public Item update(Item item) {
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public void delete(Item item) {
        items.remove(item.getId());
    }

    private Item foundItem(long itemId) {
        Item foundItem = items.get(itemId);

        if (foundItem == null) {
            throw new ObjectNotFoundException("item id - " + itemId + " not found");
        } else {
            return foundItem;
        }
    }
}
