package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingCommentDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    public ItemDto toItemDto(Item item);

    public Item toItem(ItemDto itemDto);

    public List<ItemDto> toItemDtoList(Iterable<Item> items);

    public ItemWithBookingDto toItemWithBookingDto(Item item);

    ItemWithBookingCommentDto toItemWithBookingCommentDto(ItemWithBookingDto item);
}