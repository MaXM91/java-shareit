package ru.practicum.shareit.item.maper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    public ItemDto toItemDto(Item item);

    public Item toItem(ItemDto itemDto);
}
