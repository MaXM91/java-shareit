package ru.practicum.shareit.item.maper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

@Component
public class ItemMapper {

    public ItemDto toItemDto(Item item) {
        return new ItemDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable());
    }

    public Item toItem(ItemDto itemDto) {
        int id = 0;

        if (itemDto.getId() != null) {
            id = itemDto.getId();
        }

        return new Item(id, itemDto.getName(), itemDto.getDescription(), itemDto.getAvailable(), 0, null);
    }
}
