package ru.practicum.shareit.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.item.storage.CommentStorage;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ContextConfiguration(classes = {ItemServiceImpl.class, ItemMapper.class})
public class ItemServiceTest {
    @SpyBean
    ItemMapper itemMapper;

    @Autowired
    ItemServiceImpl itemService;

    @MockBean
    ItemStorage itemStorage;

    @MockBean
    UserServiceImpl userService;

    @MockBean
    BookingStorage bookingStorage;

    @MockBean
    CommentStorage commentStorage;

    @MockBean
    ItemRequestServiceImpl itemRequestService;

    @Test
    void addItem_200_Test() {
        UserDto userDto1 = new UserDto(1, "name1", "email1@mail.ru");
        ItemDto itemBeforeDto1 = new ItemDto(null, "name1", "description1", true, 1);
        LocalDateTime created1 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(100);
        ItemRequest itemRequest1 = new ItemRequest(1, "description1", null, created1, null);
        Item itemAfter1 = new Item(1, "name1", "description1", true, 1, itemRequest1);
        ItemDto itemAfterDto1 = new ItemDto(1, "name1", "description1", true, 1);

        when(userService.getUserById(anyInt()))
                .thenReturn(userDto1);
        when(itemRequestService.getItemRequestEntityById(anyInt()))
                .thenReturn(itemRequest1);
        when(itemStorage.save(any(Item.class)))
                .thenReturn(itemAfter1);

        ItemDto actualItem = itemService.addItem(1, itemBeforeDto1);

        Assertions.assertEquals(itemAfterDto1.toString(), actualItem.toString());
        Assertions.assertEquals(itemMapper.toItem(itemAfterDto1).toString(), itemMapper.toItem(actualItem).toString());

        Mockito.verify(userService, Mockito.times(1)).getUserById(anyInt());
        Mockito.verify(itemRequestService, Mockito.times(1)).getItemRequestEntityById(anyInt());
        Mockito.verify(itemStorage, Mockito.times(1)).save(any(Item.class));

        Mockito.verify(itemMapper, Mockito.times(3)).toItem(any());
        Mockito.verify(itemMapper, Mockito.times(1)).toItemDto(any());

        Mockito.verifyNoMoreInteractions(userService);
        Mockito.verifyNoMoreInteractions(itemRequestService);
        Mockito.verifyNoMoreInteractions(itemMapper);
    }

    @Test
    void getItemEntityByIdTest() {

        LocalDateTime created2 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(100);
        ItemRequest itemRequest2 = new ItemRequest(1, "description2", null, created2, null);
        Item itemAfter2 = new Item(1, "name2", "description2", true, 1, itemRequest2);

        when(itemStorage.findById(anyInt()))
                .thenReturn(Optional.ofNullable(itemAfter2));

        Item actualItem = itemService.getItemEntityById(1);

        Assertions.assertEquals(itemAfter2, actualItem);

        Mockito.verify(itemStorage, Mockito.times(1)).findById(anyInt());

        Mockito.verifyNoMoreInteractions(itemStorage);
    }

    @Test
    void getItemByIdTest() {
        UserDto userDto3 = new UserDto(1, "name3", "email3@mail.ru");
        LocalDateTime created3 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(100);
        ItemRequest itemRequest3 = new ItemRequest(1, "description3", null, created3, null);
        Item itemAfter3 = new Item(1, "name3", "description3", true, 1, itemRequest3);
        LocalDateTime start3 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(50);
        LocalDateTime end3 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(100);
        BookingForItemDto bookingForItemDto3 = new BookingForItemDto(1, 2, start3, end3, 1);
        LocalDateTime created33 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        CommentDto commentDto3 = new CommentDto(1, "text3", "name3", created33);
        ItemWithBookingCommentDto itemWithBookingCommentDto1 =
                new ItemWithBookingCommentDto(1, "name3", "description3",
                        true, null, bookingForItemDto3, List.of(commentDto3));

        when(userService.getUserById(anyInt()))
                .thenReturn(userDto3);
        when(itemStorage.findById(anyInt()))
                .thenReturn(Optional.ofNullable(itemAfter3));
        when(bookingStorage.findAllByItemIdAndStatus(anyInt(), anyInt()))
                .thenReturn(List.of(bookingForItemDto3));
        when(commentStorage.findAllByItemId(anyInt()))
                .thenReturn(List.of(commentDto3));

        ItemWithBookingCommentDto actualItem = itemService.getItemById(1, 1);

        Assertions.assertEquals(itemWithBookingCommentDto1, actualItem);

        Mockito.verify(userService, Mockito.times(1)).getUserById(anyInt());
        Mockito.verify(itemStorage, Mockito.times(1)).findById(anyInt());
        Mockito.verify(bookingStorage, Mockito.times(1)).findAllByItemIdAndStatus(anyInt(), anyInt());
        Mockito.verify(commentStorage, Mockito.times(1)).findAllByItemId(anyInt());

        Mockito.verify(itemMapper, Mockito.times(1)).toItemWithBookingDto(any());
        Mockito.verify(itemMapper, Mockito.times(1)).toItemWithBookingCommentDto(any());

        Mockito.verifyNoMoreInteractions(userService);
        Mockito.verifyNoMoreInteractions(itemStorage);
        Mockito.verifyNoMoreInteractions(bookingStorage);
        Mockito.verifyNoMoreInteractions(commentStorage);
        Mockito.verifyNoMoreInteractions(itemMapper);
    }

    @Test
    void getItemsByOwnerId_200_Test() {
        UserDto userDto4 = new UserDto(5, "name4", "email4@mail.ru");

        LocalDateTime created4 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(100);

        Item itemAfter4 = new Item(2, "name4", "description4", true, 5, null);
        List<Item> items4 = List.of(itemAfter4);

        LocalDateTime startLast4 = LocalDateTime.now().minusMinutes(200);
        LocalDateTime endLast4 = LocalDateTime.now().plusMinutes(100);
        BookingForItemDto bookingForItemDtoLast1 = new BookingForItemDto(10, 3, startLast4, endLast4, 2);

        LocalDateTime startNext4 = LocalDateTime.now().plusMinutes(500);
        LocalDateTime endNext4 = LocalDateTime.now().plusMinutes(1000);
        BookingForItemDto bookingForItemDtoNext1 = new BookingForItemDto(11, 3, startNext4, endNext4, 2);
        List<BookingForItemDto> bookings4 = List.of(bookingForItemDtoLast1, bookingForItemDtoNext1);

        ItemWithBookingDto itemWithBookingDto4 =
                new ItemWithBookingDto(2, "name4", "description4",
                        true, null, null);
        List<ItemWithBookingDto> itemsWithBookingDto4 = List.of(itemWithBookingDto4);


        when(userService.getUserById(anyInt()))
                .thenReturn(userDto4);
        when(itemStorage.findByOwnerId(anyInt()))
                .thenReturn(items4);
        when(bookingStorage.findAllByItemIdAndStatus(anyInt(), anyInt()))
                .thenReturn(bookings4);

        List<ItemWithBookingDto> actualItem = itemService.getItemsByOwnerId(5, null, null);

        Assertions.assertEquals(itemsWithBookingDto4, actualItem);

        Mockito.verify(userService, Mockito.times(1)).getUserById(5);
        Mockito.verify(itemStorage, Mockito.times(1)).findByOwnerId(5);

        Mockito.verify(itemMapper, Mockito.times(1)).toItemWithBookingDto(any());

        Mockito.verifyNoMoreInteractions(userService);
        Mockito.verifyNoMoreInteractions(itemStorage);
        Mockito.verifyNoMoreInteractions(itemMapper);
    }

    @Test
    void getItemsByOwnerId_noy_null_args_200_Test() {
        UserDto userDto4 = new UserDto(1, "name4", "email4@mail.ru");

        LocalDateTime created4 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(100);
        ItemRequest itemRequest4 = new ItemRequest(1, "description4", null, created4, null);

        Item itemAfter4 = new Item(1, "name4", "description4", true, 1, itemRequest4);

        LocalDateTime start4 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(50);
        LocalDateTime end4 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(100);
        BookingForItemDto bookingForItemDto1 = new BookingForItemDto(1, 2, start4, end4, 1);
        ItemWithBookingDto itemWithBookingDto4 =
                new ItemWithBookingDto(1, "name4", "description4",
                        true, null, null);

        when(userService.getUserById(anyInt()))
                .thenReturn(userDto4);
        when(itemStorage.findByOwnerId(anyInt()))
                .thenReturn(List.of(itemAfter4));
        when(bookingStorage.findAllByItemIdAndStatus(anyInt(), anyInt()))
                .thenReturn(List.of(bookingForItemDto1));

        List<ItemWithBookingDto> actualItem = itemService.getItemsByOwnerId(1, 0, 1);

        Assertions.assertEquals(List.of(itemWithBookingDto4), actualItem);

        Mockito.verify(userService, Mockito.times(1)).getUserById(anyInt());
        Mockito.verify(itemStorage, Mockito.times(1)).findByOwnerId(anyInt());

        Mockito.verify(itemMapper, Mockito.times(1)).toItemWithBookingDto(any());

        Mockito.verifyNoMoreInteractions(userService);
        Mockito.verifyNoMoreInteractions(itemStorage);
        Mockito.verifyNoMoreInteractions(itemMapper);
    }

    @Test
    void getItemByString_200_Test() {
        UserDto userDto5 = new UserDto(1, "name5", "email5@mail.ru");
        LocalDateTime created5 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(100);
        ItemRequest itemRequest5 = new ItemRequest(1, "description5", null, created5, null);
        Item itemAfter5 = new Item(1, "name5", "description5", true, 1, itemRequest5);
        ItemDto itemAfterDto5 = new ItemDto(1, "name5", "description5", true, 1);

        when(userService.getUserById(anyInt()))
                .thenReturn(userDto5);
        when(itemStorage.search(anyString()))
                .thenReturn(List.of(itemAfter5));

        List<ItemDto> actualItem = itemService.getItemByString(1, "dsgdsg", null, null);

        Assertions.assertEquals(List.of(itemAfterDto5), actualItem);

        Mockito.verify(userService, Mockito.times(1)).getUserById(anyInt());
        Mockito.verify(itemStorage, Mockito.times(1)).search(anyString());

        Mockito.verify(itemMapper, Mockito.times(1)).toItemDtoList(any());
        Mockito.verify(itemMapper, Mockito.times(1)).toItemDto(any());

        Mockito.verifyNoMoreInteractions(userService);
        Mockito.verifyNoMoreInteractions(itemStorage);
        Mockito.verifyNoMoreInteractions(itemMapper);
    }

    @Test
    void getItemByString_empty_text_200_Test() {
        UserDto userDto5 = new UserDto(1, "name5", "email5@mail.ru");

        when(userService.getUserById(anyInt()))
                .thenReturn(userDto5);

        List<ItemDto> actualItem = itemService.getItemByString(1, "", null, null);

        Assertions.assertEquals(new ArrayList<>(), actualItem);

        Mockito.verify(userService, Mockito.times(1)).getUserById(anyInt());

        Mockito.verifyNoMoreInteractions(userService);
        Mockito.verifyNoMoreInteractions(itemMapper);
    }

    @Test
    void getItemByString_blank_text_200_Test() {
        UserDto userDto5 = new UserDto(1, "name5", "email5@mail.ru");

        when(userService.getUserById(anyInt()))
                .thenReturn(userDto5);

        List<ItemDto> actualItem = itemService.getItemByString(1, "   ", null, null);

        Assertions.assertEquals(new ArrayList<>(), actualItem);

        Mockito.verify(userService, Mockito.times(1)).getUserById(anyInt());

        Mockito.verifyNoMoreInteractions(userService);
        Mockito.verifyNoMoreInteractions(itemMapper);
    }

    @Test
    void getItemByString_not_null_args_200_Test() {
        UserDto userDto5 = new UserDto(1, "name5", "email5@mail.ru");
        LocalDateTime created5 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(100);
        ItemRequest itemRequest5 = new ItemRequest(1, "description5", null, created5, null);
        Item itemAfter5 = new Item(1, "name5", "description5", true, 1, itemRequest5);
        ItemDto itemAfterDto5 = new ItemDto(1, "name5", "description5", true, 1);

        when(userService.getUserById(anyInt()))
                .thenReturn(userDto5);
        when(itemStorage.search(anyString()))
                .thenReturn(List.of(itemAfter5));

        List<ItemDto> actualItem = itemService.getItemByString(1, "dsgdsg", 0, 1);

        Assertions.assertEquals(List.of(itemAfterDto5), actualItem);

        Mockito.verify(userService, Mockito.times(1)).getUserById(anyInt());
        Mockito.verify(itemStorage, Mockito.times(1)).search(anyString());

        Mockito.verify(itemMapper, Mockito.times(1)).toItemDto(any());

        Mockito.verifyNoMoreInteractions(userService);
        Mockito.verifyNoMoreInteractions(itemStorage);
        Mockito.verifyNoMoreInteractions(itemMapper);
    }

    @Test
    void updateTest() {
        UserDto userDto6 = new UserDto(1, "name6", "email6@mail.ru");
        LocalDateTime created6 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(100);
        ItemRequest itemRequest6 = new ItemRequest(1, "description6", null, created6, null);
        Item itemAfter6 = new Item(1, "name6", "description6", true, 1, itemRequest6);
        ItemDto itemDtoUpdate6 = new ItemDto(1, "update6", "update6", false, 1);
        Item itemUpdate6 = new Item(1, "update6", "update6", false, 1, itemRequest6);

        when(userService.getUserById(anyInt()))
                .thenReturn(userDto6);
        when(itemStorage.findById(anyInt()))
                .thenReturn(Optional.ofNullable(itemAfter6));
        when(itemStorage.save(any()))
                .thenReturn(itemUpdate6);

        ItemDto actualItem = itemService.update(1, 1, itemDtoUpdate6);

        Assertions.assertEquals(itemDtoUpdate6, actualItem);

        Mockito.verify(userService, Mockito.times(1)).getUserById(anyInt());
        Mockito.verify(itemStorage, Mockito.times(1)).findById(anyInt());
        Mockito.verify(itemStorage, Mockito.times(1)).save(any());

        Mockito.verify(itemMapper, Mockito.times(1)).toItem(any());
        Mockito.verify(itemMapper, Mockito.times(1)).toItemDto(any());

        Mockito.verifyNoMoreInteractions(userService);
        Mockito.verifyNoMoreInteractions(itemStorage);
        Mockito.verifyNoMoreInteractions(itemMapper);
    }

    @Test
    void deleteTest() {
        UserDto userDto7 = new UserDto(1, "name7", "email7@mail.ru");
        LocalDateTime created7 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(100);
        ItemRequest itemRequest7 = new ItemRequest(1, "description7", null, created7, null);
        Item itemAfter7 = new Item(1, "name7", "description7", true, 1, itemRequest7);

        when(userService.getUserById(anyInt()))
                .thenReturn(userDto7);

        itemService.delete(1, itemAfter7);

        Mockito.verify(userService, Mockito.times(1)).getUserById(anyInt());
        Mockito.verify(itemStorage, Mockito.times(1)).delete(any());

        Mockito.verifyNoMoreInteractions(userService);
        Mockito.verifyNoMoreInteractions(itemStorage);
    }
}
