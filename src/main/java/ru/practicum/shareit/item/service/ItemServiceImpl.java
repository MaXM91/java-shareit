package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.maper.ItemMapper;
import ru.practicum.shareit.item.storage.ItemStorage;

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
}
