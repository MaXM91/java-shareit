package ru.practicum.shareit.request.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.*;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;

@Component
public class ItemRequestMapper {
    public ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        ItemRequestDto itemRequestDto = new ItemRequestDto();

        itemRequestDto.setId(itemRequest.getId());
        itemRequestDto.setDescription(itemRequest.getDescription());
        itemRequestDto.setRequester(itemRequest.getRequester().getId());
        itemRequestDto.setCreated(itemRequest.getCreated());

        return itemRequestDto;
    }

    public ItemRequest toItemRequest(ItemRequestDto itemRequestDto, User user) {
        ItemRequest itemRequest = new ItemRequest();

        if (itemRequestDto.getId() != null) {
            itemRequest.setId(itemRequestDto.getId());
        }

        itemRequest.setDescription(itemRequestDto.getDescription());
        itemRequest.setRequester(user);
        itemRequest.setCreated(itemRequestDto.getCreated());

        return itemRequest;
    }

    public ItemRequestSimplifiedDto toItemRequestSimplifiedDto(ItemRequestSimplified itemRequestSimplified) {
        ItemRequestSimplifiedDto itemRequestSimplifiedDto = new ItemRequestSimplifiedDto();

        itemRequestSimplifiedDto.setId(itemRequestSimplified.getId());
        itemRequestSimplifiedDto.setDescription(itemRequestSimplified.getDescription());
        itemRequestSimplifiedDto.setRequester(toShortUserDto(itemRequestSimplified.getRequester()));
        itemRequestSimplifiedDto.setCreated(itemRequestSimplified.getCreated());
        itemRequestSimplifiedDto.setItems(new ArrayList<>());

        return itemRequestSimplifiedDto;
    }

    public ShortItemDto toShortItemDto(ShortItem shortItem) {
        return new ShortItemDto(shortItem.getId(), shortItem.getName(), shortItem.getDescription(),
                shortItem.getAvailable(), shortItem.getRequestId());
    }

    public ShortItemDto toShortItemDto(Item item) {
        return new ShortItemDto(item.getId(), item.getName(), item.getDescription(),
                item.getAvailable(), item.getRequest().getId());
    }

    private ShortUserDto toShortUserDto(ShortUser shortUser) {
        return new ShortUserDto(shortUser.getId());
    }
}
