package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.storage.ItemRequestStorage;

@Service
@RequiredArgsConstructor
public class InMemoryItemRequestService implements  ItemRequestService {
    private final ItemRequestMapper itemRequestMapper;
    private final ItemRequestStorage itemRequestStorage;

}
