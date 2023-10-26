package ru.practicum.shareit.request.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.storage.ItemRequestStorage;

@Qualifier("InMemoryItemRequestService")
@Service
public class InMemoryItemRequestService implements  ItemRequestService {
    ItemRequestMapper itemRequestMapper;
    ItemRequestStorage itemRequestStorage;

    @Autowired
    InMemoryItemRequestService(ItemRequestMapper itemRequestMapper,
                               @Qualifier("InMemoryItemRequestStorage") ItemRequestStorage itemRequestStorage) {
        this.itemRequestMapper = itemRequestMapper;
        this.itemRequestStorage = itemRequestStorage;
    }
}
