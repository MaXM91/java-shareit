package ru.practicum.shareit.integration;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.StatusBooking;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingCommentDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserServiceImpl;
import ru.practicum.shareit.validation.exceptions.ObjectNotFoundException;

import javax.transaction.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceImplTest {
    private final UserServiceImpl userService;
    private final ItemServiceImpl itemService;
    private final ItemStorage itemStorage;
    private final ItemRequestServiceImpl itemRequestService;
    private final BookingServiceImpl bookingService;

    @AfterEach
    void deleteAll() {
        itemStorage.deleteAll();
    }

    @Test
    void addItemRequestTest() {
        UserDto userDtoBefore1 = new UserDto(null, "user1", "user1@mail.ru");
        UserDto userDtoAfter1 = userService.addUser(userDtoBefore1);

        UserDto userDtoBefore2 = new UserDto(null, "user2", "user2@mail.ru");
        UserDto userDtoAfter2 = userService.addUser(userDtoBefore2);

        ItemRequestDto itemRequestDtoBefore1 =
                new ItemRequestDto(null, "description1", userDtoAfter1.getId(), null, null);
        ItemRequestDto itemRequestDtoAfter1 = itemRequestService.addItemRequest(userDtoAfter1.getId(), itemRequestDtoBefore1);

        ItemDto itemDtoBefore1 =
                new ItemDto(null, "name1", "description1", true, itemRequestDtoAfter1.getId());
        ItemDto itemDtoAfter1 = itemService.addItem(userDtoAfter2.getId(), itemDtoBefore1);

        Assertions.assertNotNull(userDtoAfter1.getId());

        assertThat(itemDtoAfter1)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(itemDtoBefore1);
    }

    @Test
    void getItemEntityByIdTest() {
        UserDto userDtoBefore1 = new UserDto(null, "user1", "user1@mail.ru");
        UserDto userDtoAfter1 = userService.addUser(userDtoBefore1);

        UserDto userDtoBefore2 = new UserDto(null, "user2", "user2@mail.ru");
        UserDto userDtoAfter2 = userService.addUser(userDtoBefore2);

        ItemRequestDto itemRequestDtoBefore1 =
                new ItemRequestDto(null, "description1", userDtoAfter1.getId(), null, null);
        ItemRequestDto itemRequestDtoAfter1 = itemRequestService.addItemRequest(userDtoAfter1.getId(), itemRequestDtoBefore1);

        ItemDto itemDtoBefore1 =
                new ItemDto(null, "name1", "description1", true, itemRequestDtoAfter1.getId());
        ItemDto itemDtoAfter1 = itemService.addItem(userDtoAfter2.getId(), itemDtoBefore1);

        Item foundedItem = itemService.getItemEntityById(itemDtoAfter1.getId());

        Assertions.assertNotNull(foundedItem);

        Assertions.assertEquals(itemDtoAfter1.getId(), foundedItem.getId());
        Assertions.assertEquals(itemDtoAfter1.getName(), foundedItem.getName());
        Assertions.assertEquals(itemDtoAfter1.getDescription(), foundedItem.getDescription());
        Assertions.assertEquals(itemDtoAfter1.getAvailable(), foundedItem.getAvailable());
        Assertions.assertEquals(itemDtoAfter1.getRequestId(), foundedItem.getRequest().getId());
    }

    @Test
    void getItemByIdTest() {
        UserDto userDtoBefore1 = new UserDto(null, "user1", "user1@mail.ru");
        UserDto userDtoAfter1 = userService.addUser(userDtoBefore1);

        UserDto userDtoBefore2 = new UserDto(null, "user2", "user2@mail.ru");
        UserDto userDtoAfter2 = userService.addUser(userDtoBefore2);

        ItemRequestDto itemRequestDtoBefore1 =
                new ItemRequestDto(null, "description1", userDtoAfter1.getId(), null, null);
        ItemRequestDto itemRequestDtoAfter1 = itemRequestService.addItemRequest(userDtoAfter1.getId(), itemRequestDtoBefore1);

        ItemDto itemDtoBefore1 =
                new ItemDto(null, "name1", "description1", true, itemRequestDtoAfter1.getId());
        ItemDto itemDtoAfter1 = itemService.addItem(userDtoAfter2.getId(), itemDtoBefore1);

        LocalDateTime start1 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).minusMinutes(1000);
        LocalDateTime end1 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).minusMinutes(900);
        BookingDto bookingDtoLastBefore1 =
                new BookingDto(null, start1, end1, StatusBooking.WAITING, userDtoAfter2.getId(),
                        itemDtoAfter1.getId(), itemDtoAfter1.getName());
        BookingResponseDto bookingDtoLastAfter1 = bookingService.addBooking(userDtoAfter1.getId(), bookingDtoLastBefore1);
        bookingService.updateStatusBooking(userDtoAfter2.getId(), bookingDtoLastAfter1.getId(),true);

        LocalDateTime start2 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(900);
        LocalDateTime end2 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(1000);
        BookingDto bookingDtoNextBefore1 =
                new BookingDto(null, start2, end2, StatusBooking.WAITING, userDtoAfter2.getId(),
                        itemDtoAfter1.getId(), itemDtoAfter1.getName());
        BookingResponseDto bookingDtoNextAfter1 = bookingService.addBooking(userDtoAfter1.getId(), bookingDtoNextBefore1);
        bookingService.updateStatusBooking(userDtoAfter2.getId(), bookingDtoNextAfter1.getId(),true);

        ItemWithBookingCommentDto foundedItem = itemService.getItemById(userDtoAfter2.getId(), itemDtoAfter1.getId());

        Assertions.assertNotNull(foundedItem);

        Assertions.assertEquals(itemDtoAfter1.getId(), foundedItem.getId());
        Assertions.assertEquals(itemDtoAfter1.getName(), foundedItem.getName());
        Assertions.assertEquals(itemDtoAfter1.getDescription(), foundedItem.getDescription());
        Assertions.assertEquals(itemDtoAfter1.getAvailable(), foundedItem.getAvailable());
        Assertions.assertEquals(bookingDtoNextAfter1.getId(), foundedItem.getNextBooking().getId());
        Assertions.assertEquals(bookingDtoLastAfter1.getId(), foundedItem.getLastBooking().getId());
        Assertions.assertTrue(foundedItem.getComments().isEmpty());
    }

    @Test
    void getItemsByOwnerIdTest() {
        UserDto userDtoBefore1 = new UserDto(null, "user1", "user1@mail.ru");
        UserDto userDtoAfter1 = userService.addUser(userDtoBefore1);

        UserDto userDtoBefore2 = new UserDto(null, "user2", "user2@mail.ru");
        UserDto userDtoAfter2 = userService.addUser(userDtoBefore2);

        ItemRequestDto itemRequestDtoBefore1 =
                new ItemRequestDto(null, "description1", userDtoAfter1.getId(), null, null);
        ItemRequestDto itemRequestDtoAfter1 = itemRequestService.addItemRequest(userDtoAfter1.getId(), itemRequestDtoBefore1);

        ItemDto itemDtoBefore1 =
                new ItemDto(null, "name1", "description1", true, itemRequestDtoAfter1.getId());
        ItemDto itemDtoAfter1 = itemService.addItem(userDtoAfter2.getId(), itemDtoBefore1);

        LocalDateTime start1 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).minusMinutes(1000);
        LocalDateTime end1 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).minusMinutes(900);
        BookingDto bookingDtoLastBefore1 =
                new BookingDto(null, start1, end1, StatusBooking.WAITING, userDtoAfter2.getId(),
                        itemDtoAfter1.getId(), itemDtoAfter1.getName());
        BookingResponseDto bookingDtoLastAfter1 = bookingService.addBooking(userDtoAfter1.getId(), bookingDtoLastBefore1);
        bookingService.updateStatusBooking(userDtoAfter2.getId(), bookingDtoLastAfter1.getId(),true);

        LocalDateTime start2 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(900);
        LocalDateTime end2 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(1000);
        BookingDto bookingDtoNextBefore1 =
                new BookingDto(null, start2, end2, StatusBooking.WAITING, userDtoAfter2.getId(),
                        itemDtoAfter1.getId(), itemDtoAfter1.getName());
        BookingResponseDto bookingDtoNextAfter1 = bookingService.addBooking(userDtoAfter1.getId(), bookingDtoNextBefore1);
        bookingService.updateStatusBooking(userDtoAfter2.getId(), bookingDtoNextAfter1.getId(),true);

        List<ItemWithBookingDto> foundedItems = itemService.getItemsByOwnerId(userDtoAfter2.getId(), 0, 2);

        Assertions.assertTrue(foundedItems.size() == 1);

        Assertions.assertEquals(itemDtoAfter1.getId(), foundedItems.get(0).getId());
        Assertions.assertEquals(itemDtoAfter1.getName(), foundedItems.get(0).getName());
        Assertions.assertEquals(itemDtoAfter1.getDescription(), foundedItems.get(0).getDescription());
        Assertions.assertEquals(itemDtoAfter1.getAvailable(), foundedItems.get(0).getAvailable());
        Assertions.assertEquals(bookingDtoNextAfter1.getId(), foundedItems.get(0).getNextBooking().getId());
        Assertions.assertEquals(bookingDtoLastAfter1.getId(), foundedItems.get(0).getLastBooking().getId());
    }

    @Test
    void getItemByStringTest() {
        UserDto userDtoBefore1 = new UserDto(null, "user1", "user1@mail.ru");
        UserDto userDtoAfter1 = userService.addUser(userDtoBefore1);

        UserDto userDtoBefore2 = new UserDto(null, "user2", "user2@mail.ru");
        UserDto userDtoAfter2 = userService.addUser(userDtoBefore2);

        ItemRequestDto itemRequestDtoBefore1 =
                new ItemRequestDto(null, "description1", userDtoAfter1.getId(), null, null);
        ItemRequestDto itemRequestDtoAfter1 = itemRequestService.addItemRequest(userDtoAfter1.getId(), itemRequestDtoBefore1);

        ItemDto itemDtoBefore1 =
                new ItemDto(null, "name1", "description1", true, itemRequestDtoAfter1.getId());
        ItemDto itemDtoAfter1 = itemService.addItem(userDtoAfter2.getId(), itemDtoBefore1);

        List<ItemDto> foundedItems = itemService.getItemByString(userDtoAfter1.getId(), "name1", 0, 1);

        Assertions.assertTrue(foundedItems.size() == 1);

        Assertions.assertEquals(itemDtoAfter1.getId(), foundedItems.get(0).getId());
        Assertions.assertEquals(itemDtoAfter1.getName(), foundedItems.get(0).getName());
        Assertions.assertEquals(itemDtoAfter1.getDescription(), foundedItems.get(0).getDescription());
        Assertions.assertEquals(itemDtoAfter1.getAvailable(), foundedItems.get(0).getAvailable());
        Assertions.assertEquals(itemRequestDtoAfter1.getId(), foundedItems.get(0).getRequestId());
    }

    @Test
    void updateTest() {
        UserDto userDtoBefore1 = new UserDto(null, "user1", "user1@mail.ru");
        UserDto userDtoAfter1 = userService.addUser(userDtoBefore1);

        UserDto userDtoBefore2 = new UserDto(null, "user2", "user2@mail.ru");
        UserDto userDtoAfter2 = userService.addUser(userDtoBefore2);

        ItemRequestDto itemRequestDtoBefore1 =
                new ItemRequestDto(null, "description1", userDtoAfter1.getId(), null, null);
        ItemRequestDto itemRequestDtoAfter1 = itemRequestService.addItemRequest(userDtoAfter1.getId(), itemRequestDtoBefore1);

        ItemDto itemDtoBefore1 =
                new ItemDto(null, "name1", "description1", true, itemRequestDtoAfter1.getId());
        ItemDto itemDtoAfter1 = itemService.addItem(userDtoAfter2.getId(), itemDtoBefore1);

        ItemDto itemDtoUpdateBefore1 =
                new ItemDto(null, "update1", "update1", false, itemRequestDtoAfter1.getId());
        ItemDto itemDtoUpdateAfter1 = itemService.update(userDtoAfter2.getId(), itemDtoAfter1.getId(), itemDtoUpdateBefore1);

        Assertions.assertNotNull(itemDtoUpdateAfter1);

        Assertions.assertEquals(itemDtoUpdateBefore1.getName(), itemDtoUpdateAfter1.getName());
        Assertions.assertEquals(itemDtoUpdateBefore1.getDescription(), itemDtoUpdateAfter1.getDescription());
        Assertions.assertEquals(itemDtoUpdateBefore1.getAvailable(), itemDtoUpdateAfter1.getAvailable());
        Assertions.assertEquals(itemRequestDtoAfter1.getId(), itemDtoUpdateAfter1.getRequestId());
    }

    @Test
    void deleteTest() {
        UserDto userDtoBefore1 = new UserDto(null, "user1", "user1@mail.ru");
        UserDto userDtoAfter1 = userService.addUser(userDtoBefore1);

        UserDto userDtoBefore2 = new UserDto(null, "user2", "user2@mail.ru");
        UserDto userDtoAfter2 = userService.addUser(userDtoBefore2);

        ItemRequestDto itemRequestDtoBefore1 =
                new ItemRequestDto(null, "description1", userDtoAfter1.getId(), null, null);
        ItemRequestDto itemRequestDtoAfter1 = itemRequestService.addItemRequest(userDtoAfter1.getId(), itemRequestDtoBefore1);

        ItemDto itemDtoBefore1 =
                new ItemDto(null, "name1", "description1", true, itemRequestDtoAfter1.getId());
        ItemDto itemDtoAfter1 = itemService.addItem(userDtoAfter2.getId(), itemDtoBefore1);

        itemService.delete(userDtoAfter1.getId(), itemService.getItemEntityById(itemDtoAfter1.getId()));

        ObjectNotFoundException exc = assertThrows(ObjectNotFoundException.class, new Executable() {
            @Override
            public void execute() throws ObjectNotFoundException {
                itemService.getItemEntityById(itemDtoAfter1.getId());
            }
        });

        Assertions.assertEquals("item id - " + itemDtoAfter1.getId() + " not found", exc.getMessage());
    }
}
