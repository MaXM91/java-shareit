package ru.practicum.shareit.request.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.storage.ItemRequestStorage;

@Qualifier("ItemRequestServiceImpl")
@Service
public class ItemRequestServiceImpl implements ItemRequestService {
    ItemRequestMapper itemRequestMapper;
    ItemRequestStorage itemRequestStorage;

    @Autowired
    ItemRequestServiceImpl(ItemRequestMapper itemRequestMapper,
                           @Qualifier("ItemRequestStorageImpl") ItemRequestStorage itemRequestStorage) {
        this.itemRequestMapper = itemRequestMapper;
        this.itemRequestStorage = itemRequestStorage;
    }
}
