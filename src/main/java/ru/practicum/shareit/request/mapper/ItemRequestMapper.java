package ru.practicum.shareit.request.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.request.dto.*;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Component
public class ItemRequestMapper {
    public ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        ItemRequestDto itemRequestDto = new ItemRequestDto();

        itemRequestDto.setId(itemRequest.getId());
        itemRequestDto.setDescription(itemRequest.getDescription());
        itemRequestDto.setRequester(itemRequest.getRequester().getId());
        itemRequestDto.setCreated(itemRequest.getCreated());

        if (itemRequest.getItems() == null || itemRequest.getItems().isEmpty()) {
            itemRequestDto.setItems(new ArrayList<>());
        } else {
            itemRequestDto.setItems(itemRequest.getItems());
        }

        return itemRequestDto;
    }

    public ItemRequest toItemRequest(ItemRequestDto itemRequestDto, User user) {
        ItemRequest itemRequest = new ItemRequest();

        if (itemRequestDto.getId() != null) {
            itemRequest.setId(itemRequestDto.getId());
        } else {
            itemRequest.setId(0);
        }

        itemRequest.setDescription(itemRequestDto.getDescription());
        itemRequest.setRequester(user);
        itemRequest.setCreated(itemRequestDto.getCreated());

        if (itemRequestDto.getItems() == null || itemRequestDto.getItems().isEmpty()) {
            itemRequest.setItems(new ArrayList<>());
        } else {
            itemRequest.setItems(itemRequestDto.getItems());
        }

        return itemRequest;
    }

    public ItemRequestSimplifiedDto toItemRequestSimplifiedDto(ItemRequestSimplified itemRequestSimplified) {
        ItemRequestSimplifiedDto itemRequestSimplifiedDto = new ItemRequestSimplifiedDto();

        itemRequestSimplifiedDto.setId(itemRequestSimplified.getId());
        itemRequestSimplifiedDto.setDescription(itemRequestSimplified.getDescription());
        itemRequestSimplifiedDto.setRequester(toShortUserDto(itemRequestSimplified.getRequester()));
        itemRequestSimplifiedDto.setCreated(itemRequestSimplified.getCreated());

        if (itemRequestSimplified.getItems() == null) {
            itemRequestSimplifiedDto.setItems(null);
        } else {
            itemRequestSimplifiedDto.setItems(toListShortItem(itemRequestSimplified.getItems()));
        }
        return itemRequestSimplifiedDto;
    }

    private List<ShortItemDto> toListShortItem(List<ShortItem> shortItem) {
        return shortItem.stream()
                .map(this::toShortItemDto)
                .collect(Collectors.toList());
    }

    private ShortItemDto toShortItemDto(ShortItem shortItem) {
        return new ShortItemDto(shortItem.getId(), shortItem.getName(), shortItem.getDescription(),
                shortItem.getAvailable(), shortItem.getRequestId());
    }

    private ShortUserDto toShortUserDto(ShortUser shortUser) {
        return new ShortUserDto(shortUser.getId());
    }
}
