package ru.practicum.shareit.integration;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestSimplifiedDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.request.storage.ItemRequestStorage;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserServiceImpl;
import ru.practicum.shareit.user.storage.UserStorage;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestServiceImplTest {
    private final ItemRequestServiceImpl itemRequestService;
    private final ItemRequestStorage itemRequestStorage;
    private final UserServiceImpl userService;
    private final UserStorage userStorage;
    private final ItemServiceImpl itemService;
    private final ItemStorage itemStorage;

    @AfterEach
    void deleteAll() {
        userStorage.deleteAll();
        itemStorage.deleteAll();
        itemRequestStorage.deleteAll();
    }

    @Test
    void addItemRequestTest() {
        UserDto userDtoBefore1 = new UserDto(null, "user1", "user1@mail.ru");
        UserDto userDtoAfter1 = userService.addUser(userDtoBefore1);

        ItemRequestDto itemRequestDtoBefore1 =
                new ItemRequestDto(null, "description1", userDtoAfter1.getId(), null, null);
        ItemRequestDto itemRequestDtoAfter1 = itemRequestService.addItemRequest(userDtoAfter1.getId(), itemRequestDtoBefore1);

        Assertions.assertEquals(itemRequestDtoBefore1.getDescription(), itemRequestDtoAfter1.getDescription());
        Assertions.assertEquals(itemRequestDtoBefore1.getRequester(), itemRequestDtoAfter1.getRequester());
        Assertions.assertEquals(itemRequestDtoBefore1.getCreated(), itemRequestDtoAfter1.getCreated());
        Assertions.assertEquals(new ArrayList<>(), itemRequestDtoAfter1.getItems());
    }

    @Test
    void getItemRequestEntityByIdTest() {
        UserDto userDtoBefore1 = new UserDto(null, "user1", "user1@mail.ru");
        UserDto userDtoAfter1 = userService.addUser(userDtoBefore1);

        ItemRequestDto itemRequestDtoBefore1 =
                new ItemRequestDto(null, "description1", userDtoAfter1.getId(), null, null);
        ItemRequestDto itemRequestDtoAfter1 = itemRequestService.addItemRequest(userDtoAfter1.getId(), itemRequestDtoBefore1);

        ItemRequest foundedItemRequest1 = itemRequestService.getItemRequestEntityById(itemRequestDtoAfter1.getId());

        Assertions.assertEquals(itemRequestDtoBefore1.getDescription(), foundedItemRequest1.getDescription());
        Assertions.assertEquals(itemRequestDtoBefore1.getRequester(), foundedItemRequest1.getRequester().getId());
        Assertions.assertEquals(itemRequestDtoBefore1.getCreated(), foundedItemRequest1.getCreated());
        Assertions.assertEquals(new ArrayList<>(), foundedItemRequest1.getItems());
    }

    @Test
    void getAllYourItemRequestTest() {
        UserDto userDtoBefore1 = new UserDto(null, "user1", "user1@mail.ru");
        UserDto userDtoAfter1 = userService.addUser(userDtoBefore1);

        ItemRequestDto itemRequestDtoBefore1 =
                new ItemRequestDto(null, "description1", userDtoAfter1.getId(), null, null);
        ItemRequestDto itemRequestDtoAfter1 = itemRequestService.addItemRequest(userDtoAfter1.getId(), itemRequestDtoBefore1);

        ItemDto itemDtoBefore1 =
                new ItemDto(null, "name1", "description1", true, itemRequestDtoAfter1.getId());
        ItemDto itemDtoAfter1 = itemService.addItem(userDtoAfter1.getId(), itemDtoBefore1);

        List<ItemRequestSimplifiedDto> foundedItemRequests = itemRequestService.getAllYourItemRequest(userDtoAfter1.getId());

        Assertions.assertFalse(foundedItemRequests.isEmpty());

        Assertions.assertEquals(itemRequestDtoBefore1.getDescription(), foundedItemRequests.get(0).getDescription());
        Assertions.assertEquals(itemRequestDtoBefore1.getRequester(), foundedItemRequests.get(0).getRequester().getId());
        Assertions.assertEquals(itemRequestDtoBefore1.getCreated(), foundedItemRequests.get(0).getCreated());
        Assertions.assertEquals(itemDtoAfter1.getId(), foundedItemRequests.get(0).getItems().get(0).getId());
        Assertions.assertEquals(itemDtoAfter1.getName(), foundedItemRequests.get(0).getItems().get(0).getName());
    }

    @Test
    void getItemRequestByIdTest() {
        UserDto userDtoBefore1 = new UserDto(null, "user1", "user1@mail.ru");
        UserDto userDtoAfter1 = userService.addUser(userDtoBefore1);

        ItemRequestDto itemRequestDtoBefore1 =
                new ItemRequestDto(null, "description1", userDtoAfter1.getId(), null, null);
        ItemRequestDto itemRequestDtoAfter1 = itemRequestService.addItemRequest(userDtoAfter1.getId(), itemRequestDtoBefore1);

        ItemDto itemDtoBefore1 =
                new ItemDto(null, "name1", "description1", true, itemRequestDtoAfter1.getId());
        ItemDto itemDtoAfter1 = itemService.addItem(userDtoAfter1.getId(), itemDtoBefore1);

        ItemRequestSimplifiedDto foundedItemRequest =
                itemRequestService.getItemRequestById(userDtoAfter1.getId(), itemRequestDtoAfter1.getId());

        Assertions.assertEquals(itemRequestDtoBefore1.getDescription(), foundedItemRequest.getDescription());
        Assertions.assertEquals(itemRequestDtoBefore1.getRequester(), foundedItemRequest.getRequester().getId());
        Assertions.assertEquals(itemRequestDtoBefore1.getCreated(), foundedItemRequest.getCreated());
        Assertions.assertEquals(itemDtoAfter1.getId(), foundedItemRequest.getItems().get(0).getId());
        Assertions.assertEquals(itemDtoAfter1.getName(), foundedItemRequest.getItems().get(0).getName());
    }

    @Test
    void getAllItemRequestPageableTest() {
        UserDto userDtoBefore1 = new UserDto(null, "user1", "user1@mail.ru");
        UserDto userDtoAfter1 = userService.addUser(userDtoBefore1);

        UserDto userDtoBefore2 = new UserDto(null, "user2", "user2@mail.ru");
        UserDto userDtoAfter2 = userService.addUser(userDtoBefore2);

        ItemRequestDto itemRequestDtoBefore1 =
                new ItemRequestDto(null, "description1", userDtoAfter1.getId(), null, null);
        ItemRequestDto itemRequestDtoAfter1 = itemRequestService.addItemRequest(userDtoAfter2.getId(), itemRequestDtoBefore1);

        ItemDto itemDtoBefore1 =
                new ItemDto(null, "name1", "description1", true, itemRequestDtoAfter1.getId());
        ItemDto itemDtoAfter1 = itemService.addItem(userDtoAfter1.getId(), itemDtoBefore1);

        List<ItemRequestSimplifiedDto> foundedItemRequest =
                itemRequestService.getAllItemRequestPageable(userDtoAfter1.getId(), 0, 2);

        Assertions.assertEquals(itemRequestDtoBefore1.getDescription(), foundedItemRequest.get(0).getDescription());
        Assertions.assertEquals(itemRequestDtoBefore1.getCreated(), foundedItemRequest.get(0).getCreated());
        Assertions.assertEquals(itemDtoAfter1.getId(), foundedItemRequest.get(0).getItems().get(0).getId());
        Assertions.assertEquals(itemDtoAfter1.getName(), foundedItemRequest.get(0).getItems().get(0).getName());
    }
}
