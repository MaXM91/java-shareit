package ru.practicum.shareit.integration;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.StateBooking;
import ru.practicum.shareit.booking.StatusBooking;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.ItemBookingDto;
import ru.practicum.shareit.booking.dto.UserBookingDto;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserServiceImpl;
import ru.practicum.shareit.user.storage.UserStorage;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingServiceImplTest {
    private final BookingServiceImpl bookingService;
    private final BookingStorage bookingStorage;
    private final ItemRequestServiceImpl itemRequestService;
    private final UserServiceImpl userService;
    private final UserStorage userStorage;
    private final ItemServiceImpl itemService;
    private final ItemStorage itemStorage;

    @AfterEach
    void deleteAll() {
        bookingStorage.deleteAll();
        userStorage.deleteAll();
        itemStorage.deleteAll();
    }

    @Test
    void addBookingTest() {
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

        UserBookingDto user1 = new UserBookingDto(userDtoAfter1.getId());
        ItemBookingDto item1 = new ItemBookingDto(itemDtoAfter1.getId(), itemDtoAfter1.getName());

        LocalDateTime start1 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).minusMinutes(500);
        LocalDateTime end1 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(500);

        BookingDto bookingDtoBefore1 =
                new BookingDto(null, start1, end1, StatusBooking.WAITING, user1.getId(), item1.getId(), item1.getName());

        BookingResponseDto bookingResponseDtoAfter1 = bookingService.addBooking(userDtoAfter1.getId(), bookingDtoBefore1);

        Assertions.assertEquals(bookingDtoBefore1.getStart(), bookingResponseDtoAfter1.getStart());
        Assertions.assertEquals(bookingDtoBefore1.getEnd(), bookingResponseDtoAfter1.getEnd());
        Assertions.assertEquals(bookingDtoBefore1.getStatus(), bookingResponseDtoAfter1.getStatus());
        Assertions.assertEquals(bookingDtoBefore1.getBookerId(), bookingResponseDtoAfter1.getBooker().getId());
        Assertions.assertEquals(bookingDtoBefore1.getItemId(), bookingResponseDtoAfter1.getItem().getId());
        Assertions.assertEquals(bookingDtoBefore1.getItemName(), bookingResponseDtoAfter1.getItem().getName());
    }

    @Test
    void getBookingById() {
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

        UserBookingDto user1 = new UserBookingDto(userDtoAfter1.getId());
        ItemBookingDto item1 = new ItemBookingDto(itemDtoAfter1.getId(), itemDtoAfter1.getName());

        LocalDateTime start1 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).minusMinutes(500);
        LocalDateTime end1 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(500);

        BookingDto bookingDtoBefore1 =
                new BookingDto(null, start1, end1, StatusBooking.WAITING, user1.getId(), item1.getId(), item1.getName());

        BookingResponseDto bookingResponseDtoAfter1 = bookingService.addBooking(userDtoAfter1.getId(), bookingDtoBefore1);
        BookingResponseDto foundedBookingResponseDtoAfter1 =
                bookingService.getBookingById(userDtoAfter1.getId(), bookingResponseDtoAfter1.getId());

        Assertions.assertEquals(bookingDtoBefore1.getStart(), foundedBookingResponseDtoAfter1.getStart());
        Assertions.assertEquals(bookingDtoBefore1.getEnd(), foundedBookingResponseDtoAfter1.getEnd());
        Assertions.assertEquals(bookingDtoBefore1.getStatus(), foundedBookingResponseDtoAfter1.getStatus());
        Assertions.assertEquals(bookingDtoBefore1.getBookerId(), foundedBookingResponseDtoAfter1.getBooker().getId());
        Assertions.assertEquals(bookingDtoBefore1.getItemId(), foundedBookingResponseDtoAfter1.getItem().getId());
        Assertions.assertEquals(bookingDtoBefore1.getItemName(), foundedBookingResponseDtoAfter1.getItem().getName());
    }

    @Test
    void getBookingsByUserIdTest() {
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

        UserBookingDto user1 = new UserBookingDto(userDtoAfter1.getId());
        ItemBookingDto item1 = new ItemBookingDto(itemDtoAfter1.getId(), itemDtoAfter1.getName());

        LocalDateTime start1 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).minusMinutes(500);
        LocalDateTime end1 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(500);

        BookingDto bookingDtoBefore1 =
                new BookingDto(null, start1, end1, StatusBooking.WAITING, user1.getId(), item1.getId(), item1.getName());

        BookingResponseDto bookingResponseDtoAfter1 = bookingService.addBooking(userDtoAfter1.getId(), bookingDtoBefore1);

        List<BookingResponseDto> foundedBookings =
                bookingService.getBookingsByUserId(userDtoAfter1.getId(), StateBooking.ALL, 0, 2);

        Assertions.assertEquals(1, foundedBookings.size());

        Assertions.assertEquals(bookingDtoBefore1.getStart(), foundedBookings.get(0).getStart());
        Assertions.assertEquals(bookingDtoBefore1.getEnd(), foundedBookings.get(0).getEnd());
        Assertions.assertEquals(bookingDtoBefore1.getStatus(), foundedBookings.get(0).getStatus());
        Assertions.assertEquals(bookingDtoBefore1.getBookerId(), foundedBookings.get(0).getBooker().getId());
        Assertions.assertEquals(bookingDtoBefore1.getItemId(), foundedBookings.get(0).getItem().getId());
        Assertions.assertEquals(bookingDtoBefore1.getItemName(), foundedBookings.get(0).getItem().getName());
    }

    @Test
    void getBookingsByOwnerItemTest() {
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

        UserBookingDto user1 = new UserBookingDto(userDtoAfter1.getId());
        ItemBookingDto item1 = new ItemBookingDto(itemDtoAfter1.getId(), itemDtoAfter1.getName());

        LocalDateTime start1 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).minusMinutes(500);
        LocalDateTime end1 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(500);

        BookingDto bookingDtoBefore1 =
                new BookingDto(null, start1, end1, StatusBooking.WAITING, user1.getId(), item1.getId(), item1.getName());

        BookingResponseDto bookingResponseDtoAfter1 = bookingService.addBooking(userDtoAfter1.getId(), bookingDtoBefore1);

        List<BookingResponseDto> foundedBookings =
                bookingService.getBookingsByUserId(userDtoAfter1.getId(), StateBooking.ALL, 0, 2);

        Assertions.assertEquals(1, foundedBookings.size());

        Assertions.assertEquals(bookingDtoBefore1.getStart(), foundedBookings.get(0).getStart());
        Assertions.assertEquals(bookingDtoBefore1.getEnd(), foundedBookings.get(0).getEnd());
        Assertions.assertEquals(bookingDtoBefore1.getStatus(), foundedBookings.get(0).getStatus());
        Assertions.assertEquals(bookingDtoBefore1.getBookerId(), foundedBookings.get(0).getBooker().getId());
        Assertions.assertEquals(bookingDtoBefore1.getItemId(), foundedBookings.get(0).getItem().getId());
        Assertions.assertEquals(bookingDtoBefore1.getItemName(), foundedBookings.get(0).getItem().getName());
    }

    @Test
    void updateStatusBookingTest() {
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

        UserBookingDto user1 = new UserBookingDto(userDtoAfter1.getId());
        ItemBookingDto item1 = new ItemBookingDto(itemDtoAfter1.getId(), itemDtoAfter1.getName());

        LocalDateTime start1 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).minusMinutes(500);
        LocalDateTime end1 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(500);

        BookingDto bookingDtoBefore1 =
                new BookingDto(null, start1, end1, StatusBooking.WAITING, user1.getId(), item1.getId(), item1.getName());

        BookingResponseDto bookingResponseDtoAfter1 = bookingService.addBooking(userDtoAfter1.getId(), bookingDtoBefore1);
        BookingResponseDto bookingApprowedStatusDtoAfter1 =
                bookingService.updateStatusBooking(userDtoAfter2.getId(), bookingResponseDtoAfter1.getId(), true);

        Assertions.assertEquals(StatusBooking.APPROVED, bookingApprowedStatusDtoAfter1.getStatus());
    }
}