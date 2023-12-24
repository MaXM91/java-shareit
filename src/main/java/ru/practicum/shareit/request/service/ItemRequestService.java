package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestSimplifiedDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto addItemRequest(int userId, ItemRequestDto itemRequestDto);

    ItemRequest getItemRequestEntityById(int requestId);

    List<ItemRequestSimplifiedDto> getAllYourItemRequest(int userId);

    ItemRequestSimplifiedDto getItemRequestById(int userId, int requestId);

    List<ItemRequestSimplifiedDto> getAllItemRequestPageable(int userId, Integer from, Integer size);
}
