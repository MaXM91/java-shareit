package ru.practicum.shareit.request.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;

@Mapper(componentModel = "spring")
public interface ItemRequestMapper {
    ItemRequestDto toItemRequestDto(ItemRequest itemRequest);

    ItemRequest toitemRequest(ItemRequestDto itemRequestDto);
}
