package ru.practicum.shareit.mappers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ContextConfiguration(classes = {ItemMapper.class})
public class ItemMapperTest {
    @Autowired
    ItemMapper itemMapper;

    @Test
    void toItemDto_request_null_Test() {
        Item item1 = new Item(1, "name1", "description1", true, 1, null);

        ItemDto itemDto = itemMapper.toItemDto(item1);

        assertEquals(item1.getId(), itemDto.getId());
        assertEquals(item1.getName(), itemDto.getName());
        assertEquals(item1.getDescription(), itemDto.getDescription());
        assertEquals(item1.getAvailable(), itemDto.getAvailable());
        assertEquals(item1.getRequest(), itemDto.getRequestId());
    }

    @Test
    void toItemDto_request_not_null_Test() {
        User user1 = new User(1, "user1@mail.ru", "user1name1");

        LocalDateTime create = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        Item item11 = new Item(2, "item1name1", "item1description1", true, 5, null);
        ItemRequest itemRequest1 = new ItemRequest(1, "description1", user1, create, List.of(item11));

        Item item1 = new Item(1, "name1", "description1", true, 1, itemRequest1);

        ItemDto itemDto = itemMapper.toItemDto(item1);

        assertEquals(item1.getId(), itemDto.getId());
        assertEquals(item1.getName(), itemDto.getName());
        assertEquals(item1.getDescription(), itemDto.getDescription());
        assertEquals(item1.getAvailable(), itemDto.getAvailable());
        assertEquals(item1.getRequest().getId(), itemDto.getRequestId());
    }

    @Test
    void toItem_id_null_Test() {
        ItemDto itemDto1 = new ItemDto(2, "item1name1", "item1description1", true, 5);

        Item item1 = itemMapper.toItem(itemDto1);

        assertEquals(itemDto1.getId(), item1.getId());
        assertEquals(itemDto1.getName(), item1.getName());
        assertEquals(itemDto1.getDescription(), item1.getDescription());
        assertEquals(itemDto1.getAvailable(), item1.getAvailable());
    }
}
