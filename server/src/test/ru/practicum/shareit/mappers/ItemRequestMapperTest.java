package ru.practicum.shareit.mappers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.*;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ContextConfiguration(classes = {ItemRequestMapper.class, ItemRequestDto.class, ItemRequest.class, User.class,
        Item.class, ShortItemDto.class, ShortUserDto.class, ShortItem.class, ShortUser.class,
        ItemRequestSimplified.class, ItemRequestSimplifiedDto.class})
public class ItemRequestMapperTest {
    @Autowired
    ItemRequestMapper itemRequestMapper;

    @Test
    void toItemRequestDto_200_Test() {
        User user1 = new User(1, "user1@mail.ru", "user1name1");

        LocalDateTime create = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        Item item1 = new Item(2, "item1name1", "item1description1", true, 5, null);
        Item item2 = new Item(3, "item2name2", "item2description1", true, 5, null);
        List<Item> items = List.of(item1, item2);

        ItemRequest itemRequest1 = new ItemRequest(1, "description1", user1, create, items);

        ItemRequestDto itemRequestDto1 = itemRequestMapper.toItemRequestDto(itemRequest1);

        assertEquals(itemRequest1.getId(), itemRequestDto1.getId());
        assertEquals(itemRequest1.getDescription(), itemRequestDto1.getDescription());
        assertEquals(itemRequest1.getRequester().getId(), itemRequestDto1.getRequester());
        assertEquals(itemRequest1.getCreated(), itemRequestDto1.getCreated());
        assertEquals(itemRequest1.getItems(), itemRequestDto1.getItems());
    }

    @Test
    void toItemRequestDto_null_items_200_Test() {
        User user1 = new User(1, "user1@mail.ru", "user1name1");

        LocalDateTime create = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        ItemRequest itemRequest1 = new ItemRequest(1, "description1", user1, create, new ArrayList<>());

        ItemRequestDto itemRequestDto1 = itemRequestMapper.toItemRequestDto(itemRequest1);

        assertEquals(itemRequest1.getId(), itemRequestDto1.getId());
        assertEquals(itemRequest1.getDescription(), itemRequestDto1.getDescription());
        assertEquals(itemRequest1.getRequester().getId(), itemRequestDto1.getRequester());
        assertEquals(itemRequest1.getCreated(), itemRequestDto1.getCreated());
        assertEquals(itemRequest1.getItems(), new ArrayList<>());
    }

    @Test
    void toItemRequest_200_Test() {
        User user = new User(3, "user1@mail.ru", "name1");
        LocalDateTime created = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        Item item1 = new Item(2, "item1name1", "item1description1", true, 5, null);
        Item item2 = new Item(3, "item2name2", "item2description1", true, 5, null);
        List<Item> items = List.of(item1, item2);

        ItemRequestDto itemRequestDto1 = new ItemRequestDto(2, "description1", 3, created, items);

        ItemRequest itemRequest1 = itemRequestMapper.toItemRequest(itemRequestDto1, user);

        assertEquals(itemRequestDto1.getId(), itemRequest1.getId());
        assertEquals(itemRequestDto1.getDescription(), itemRequest1.getDescription());
        assertEquals(itemRequestDto1.getRequester(), itemRequest1.getRequester().getId());
        assertEquals(itemRequestDto1.getCreated(), itemRequest1.getCreated());
        assertEquals(itemRequestDto1.getItems(), itemRequest1.getItems());
    }

    @Test
    void toItemRequest_items_null_200_Test() {
        User user = new User(3, "user1@mail.ru", "name1");
        LocalDateTime created = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        ItemRequestDto itemRequestDto1 = new ItemRequestDto(2, "description1", 3, created, new ArrayList<>());

        ItemRequest itemRequest1 = itemRequestMapper.toItemRequest(itemRequestDto1, user);

        assertEquals(itemRequestDto1.getId(), itemRequest1.getId());
        assertEquals(itemRequestDto1.getDescription(), itemRequest1.getDescription());
        assertEquals(itemRequestDto1.getRequester(), itemRequest1.getRequester().getId());
        assertEquals(itemRequestDto1.getCreated(), itemRequest1.getCreated());
        assertEquals(itemRequestDto1.getItems(), itemRequest1.getItems());
    }

    @Test
    void toItemRequestSimplifiedDto_200_Test() {
        ItemRequestSimplified itemRequestSimplified = new ItemRequestSimplified() {
            @Override
            public int getId() {
                return 1;
            }

            @Override
            public String getDescription() {
                return "description1";
            }

            @Override
            public ShortUser getRequester() {
                return new ShortUser() {
                    @Override
                    public int getId() {
                        return 2;
                    }
                };
            }

            @Override
            public LocalDateTime getCreated() {
                return LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(100);
            }
        };

        ItemRequestSimplifiedDto itemRequestSimplifiedDto = itemRequestMapper.toItemRequestSimplifiedDto(itemRequestSimplified);

        assertEquals(itemRequestSimplified.getId(), itemRequestSimplifiedDto.getId());
        assertEquals(itemRequestSimplified.getDescription(), itemRequestSimplifiedDto.getDescription());
        assertEquals(itemRequestSimplified.getRequester().getId(), itemRequestSimplifiedDto.getRequester().getId());
        assertEquals(itemRequestSimplified.getCreated(), itemRequestSimplifiedDto.getCreated());
    }

    @Test
    void toItemRequestSimplifiedDto_items_null_200_Test() {
        ItemRequestSimplified itemRequestSimplified = new ItemRequestSimplified() {
            @Override
            public int getId() {
                return 1;
            }

            @Override
            public String getDescription() {
                return "description1";
            }

            @Override
            public ShortUser getRequester() {
                return new ShortUser() {
                    @Override
                    public int getId() {
                        return 2;
                    }
                };
            }

            @Override
            public LocalDateTime getCreated() {
                return LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(100);
            }
        };

        ItemRequestSimplifiedDto itemRequestSimplifiedDto = itemRequestMapper.toItemRequestSimplifiedDto(itemRequestSimplified);

        assertEquals(itemRequestSimplified.getId(), itemRequestSimplifiedDto.getId());
        assertEquals(itemRequestSimplified.getDescription(), itemRequestSimplifiedDto.getDescription());
        assertEquals(itemRequestSimplified.getRequester().getId(), itemRequestSimplifiedDto.getRequester().getId());
        assertEquals(itemRequestSimplified.getCreated(), itemRequestSimplifiedDto.getCreated());
    }
}
