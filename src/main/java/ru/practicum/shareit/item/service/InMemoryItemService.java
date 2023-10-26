package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.maper.ItemMapper;
import ru.practicum.shareit.item.storage.ItemStorage;

@Qualifier("InMemoryItemService")
@Service
public class InMemoryItemService implements ItemService {
    ItemMapper itemMapper;
    ItemStorage itemStorage;

    @Autowired
    InMemoryItemService(ItemMapper itemMapper, @Qualifier("InMemoryItemStorage") ItemStorage itemStorage) {
        this.itemMapper = itemMapper;
        this.itemStorage = itemStorage;
    }
}
