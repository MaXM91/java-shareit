package ru.practicum.shareit.item.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingCommentDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.List;

@Component
public class ItemMapper {

    public ItemDto toItemDto(Item item) {
        if (item == null) {
            return null;
        }

        ItemDto.ItemDtoBuilder itemDto = ItemDto.builder();

        itemDto.id(item.getId());
        itemDto.name(item.getName());
        itemDto.description(item.getDescription());
        itemDto.available(item.getAvailable());

        if (item.getRequest() != null) {
            itemDto.requestId(item.getRequest().getId());
        }

        return itemDto.build();
    }

    public Item toItem(ItemDto itemDto) {
        if (itemDto == null) {
            return null;
        }

        Item.ItemBuilder item = Item.builder();

        if (itemDto.getId() != null) {
            item.id(itemDto.getId());
        }
        item.name(itemDto.getName());
        item.description(itemDto.getDescription());
        item.available(itemDto.getAvailable());

        return item.build();
    }

    public List<ItemDto> toItemDtoList(Iterable<Item> items) {
        if (items == null) {
            return null;
        }

        List<ItemDto> list = new ArrayList<ItemDto>();
        for (Item item : items) {
            list.add(toItemDto(item));
        }

        return list;
    }

    public ItemWithBookingDto toItemWithBookingDto(Item item) {
        if (item == null) {
            return null;
        }

        ItemWithBookingDto.ItemWithBookingDtoBuilder itemWithBookingDto = ItemWithBookingDto.builder();

        itemWithBookingDto.id(item.getId());
        itemWithBookingDto.name(item.getName());
        itemWithBookingDto.description(item.getDescription());
        itemWithBookingDto.available(item.getAvailable());

        return itemWithBookingDto.build();
    }

    public ItemWithBookingCommentDto toItemWithBookingCommentDto(ItemWithBookingDto item) {
        if (item == null) {
            return null;
        }

        ItemWithBookingCommentDto.ItemWithBookingCommentDtoBuilder itemWithBookingCommentDto = ItemWithBookingCommentDto.builder();

        itemWithBookingCommentDto.id(item.getId());
        itemWithBookingCommentDto.name(item.getName());
        itemWithBookingCommentDto.description(item.getDescription());
        itemWithBookingCommentDto.available(item.getAvailable());
        itemWithBookingCommentDto.lastBooking(item.getLastBooking());
        itemWithBookingCommentDto.nextBooking(item.getNextBooking());

        return itemWithBookingCommentDto.build();
    }
}