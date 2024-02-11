package ru.practicum.shareit.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.shareit.booking.StateBooking;
import ru.practicum.shareit.booking.StatusBooking;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.ItemBookingDto;
import ru.practicum.shareit.booking.dto.UserBookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceImpl;
import ru.practicum.shareit.validation.exceptions.UnsupportedException;
import ru.practicum.shareit.validation.exceptions.ValidException;
import ru.practicum.shareit.validation.exceptions.ValidateException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@SpringBootTest
@ContextConfiguration(classes = {BookingServiceImpl.class, BookingMapper.class})
public class BookingServiceTest {
    @SpyBean
    BookingMapper bookingMapper;

    @Autowired
    BookingServiceImpl bookingService;

    @MockBean
    BookingStorage bookingStorage;

    @MockBean
    UserServiceImpl userService;

    @MockBean
    ItemServiceImpl itemService;

    @Test
    void addBookingTest() {
        UserDto userDtoForCheck1 = new UserDto(1, "name1", "email1@mail.ru");
        User user1 = new User(1, "name1", "email1@mail.ru");
        Item item1 = new Item(2, "name1", "description1", true, 4, null);
        LocalDateTime start1 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(50);
        LocalDateTime end1 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(100);
        BookingDto bookingDtoBefore1 =
                new BookingDto(null, start1, end1, StatusBooking.WAITING, 1, 1, "wkger");
        BookingDto bookingDtoBefore2 =
                new BookingDto(null, start1, end1, StatusBooking.WAITING, 1, 1, "wkger");
        UserBookingDto userBookingDto1 = new UserBookingDto(1);
        ItemBookingDto itemBookingDto1 = new ItemBookingDto(2, "name1");
        BookingResponseDto bookingResponseDto1 =
                new BookingResponseDto(1, start1, end1, StatusBooking.WAITING, userBookingDto1, itemBookingDto1);
        Booking booking1 = new Booking(1, start1, end1, StatusBooking.WAITING, item1, user1);

        when(userService.getUserById(anyInt()))
                .thenReturn(userDtoForCheck1);
        when(userService.getUserEntityById(anyInt()))
                .thenReturn(user1);
        when(itemService.getItemEntityById(anyInt()))
                .thenReturn(item1);
        when(bookingStorage.checkFreeDateBooking(anyInt(), any(), any()))
                .thenReturn(new ArrayList<>());
        when(bookingStorage.save(any(Booking.class)))
                .thenReturn(booking1);

        BookingResponseDto actualBooking = bookingService.addBooking(1, bookingDtoBefore1);

        Assertions.assertEquals(bookingResponseDto1.toString(), actualBooking.toString());
        Assertions.assertEquals(bookingMapper.toBooking(bookingDtoBefore1, user1, item1).toString(),
                bookingMapper.toBooking(bookingDtoBefore2, user1, item1).toString());

        Mockito.verify(userService, Mockito.times(1)).getUserById(anyInt());
        Mockito.verify(userService, Mockito.times(1)).getUserEntityById(anyInt());
        Mockito.verify(itemService, Mockito.times(1)).getItemEntityById(anyInt());
        Mockito.verify(bookingStorage, Mockito.times(1)).save(any(Booking.class));
        Mockito.verify(bookingStorage, Mockito.times(1)).checkFreeDateBooking(anyInt(), any(), any());

        Mockito.verify(bookingMapper, Mockito.times(3)).toBooking(any(), any(), any());
        Mockito.verify(bookingMapper, Mockito.times(1)).toBookingRequestDto(any());

        Mockito.verifyNoMoreInteractions(userService);
        Mockito.verifyNoMoreInteractions(itemService);
        Mockito.verifyNoMoreInteractions(bookingStorage);
        Mockito.verifyNoMoreInteractions(bookingMapper);
    }

    @Test
    void addBooking_your_thing_404_Test() {
        ValidateException exc = assertThrows(ValidateException.class, new Executable() {
            @Override
            public void execute() throws ValidateException {
                UserDto userDto20 = new UserDto(2, "name20","20@mail.ru");

                User user20 = new User(3, "user@mail.ru", "name20");
                Item item20 = new Item(2, "name20", "description20", true, 2, null);

                LocalDateTime start20 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
                LocalDateTime end20 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(50);
                BookingDto bookingDto20 =
                        new BookingDto(null, start20, end20, StatusBooking.WAITING, 1, 1, "itemName1");

                when(userService.getUserById(anyInt()))
                        .thenReturn(userDto20);
                when(userService.getUserEntityById(anyInt()))
                        .thenReturn(user20);
                when(itemService.getItemEntityById(anyInt()))
                        .thenReturn(item20);


                BookingResponseDto actualBooking = bookingService.addBooking(2, bookingDto20);
            }
        });
        Assertions.assertEquals("you can't book your thing", exc.getMessage());

        Mockito.verify(userService, Mockito.times(1)).getUserById(anyInt());
        Mockito.verify(userService, Mockito.times(1)).getUserEntityById(anyInt());
        Mockito.verify(itemService, Mockito.times(1)).getItemEntityById(anyInt());

        Mockito.verify(bookingMapper, Mockito.times(1)).toBooking(any(), any(), any());

        Mockito.verifyNoMoreInteractions(userService);
        Mockito.verifyNoMoreInteractions(itemService);
        Mockito.verifyNoMoreInteractions(bookingMapper);
    }

    @Test
    void addBooking_item_available_400_Test() {
        ValidException exc = assertThrows(ValidException.class, new Executable() {
            @Override
            public void execute() throws ValidException {
                UserDto userDto20 = new UserDto(2, "name20","20@mail.ru");

                User user20 = new User(3, "user@mail.ru", "name20");
                Item item20 = new Item(2, "name20", "description20", false, 3, null);

                LocalDateTime start20 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
                LocalDateTime end20 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(50);
                BookingDto bookingDto20 =
                        new BookingDto(null, start20, end20, StatusBooking.WAITING, 1, 1, "itemName1");

                when(userService.getUserById(anyInt()))
                        .thenReturn(userDto20);
                when(userService.getUserEntityById(anyInt()))
                        .thenReturn(user20);
                when(itemService.getItemEntityById(anyInt()))
                        .thenReturn(item20);


                BookingResponseDto actualBooking = bookingService.addBooking(2, bookingDto20);
            }
        });
        Assertions.assertEquals("item available must be true", exc.getMessage());

        Mockito.verify(userService, Mockito.times(1)).getUserById(anyInt());
        Mockito.verify(userService, Mockito.times(1)).getUserEntityById(anyInt());
        Mockito.verify(itemService, Mockito.times(1)).getItemEntityById(anyInt());

        Mockito.verify(bookingMapper, Mockito.times(1)).toBooking(any(), any(), any());

        Mockito.verifyNoMoreInteractions(userService);
        Mockito.verifyNoMoreInteractions(itemService);
        Mockito.verifyNoMoreInteractions(bookingMapper);
    }

    @Test
    void addBooking_start_after_end_400_Test() {
        ValidException exc = assertThrows(ValidException.class, new Executable() {
            @Override
            public void execute() throws ValidException {
                UserDto userDto20 = new UserDto(2, "name20","20@mail.ru");

                User user20 = new User(3, "user@mail.ru", "name20");
                Item item20 = new Item(2, "name20", "description20", true, 3, null);

                LocalDateTime start20 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(50);
                LocalDateTime end20 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
                BookingDto bookingDto20 =
                        new BookingDto(null, start20, end20, StatusBooking.WAITING, 1, 1, "itemName1");

                when(userService.getUserById(anyInt()))
                        .thenReturn(userDto20);
                when(userService.getUserEntityById(anyInt()))
                        .thenReturn(user20);
                when(itemService.getItemEntityById(anyInt()))
                        .thenReturn(item20);


                BookingResponseDto actualBooking = bookingService.addBooking(2, bookingDto20);
            }
        });
        Assertions.assertEquals("start should not be after end", exc.getMessage());

        Mockito.verify(userService, Mockito.times(1)).getUserById(anyInt());
        Mockito.verify(userService, Mockito.times(1)).getUserEntityById(anyInt());
        Mockito.verify(itemService, Mockito.times(1)).getItemEntityById(anyInt());

        Mockito.verify(bookingMapper, Mockito.times(1)).toBooking(any(), any(), any());

        Mockito.verifyNoMoreInteractions(userService);
        Mockito.verifyNoMoreInteractions(itemService);
        Mockito.verifyNoMoreInteractions(bookingMapper);
    }

    @Test
    void addBooking_start_and_end_in_one_time_400_Test() {
        ValidException exc = assertThrows(ValidException.class, new Executable() {
            @Override
            public void execute() throws ValidException {
                UserDto userDto20 = new UserDto(2, "name20","20@mail.ru");

                User user20 = new User(3, "user@mail.ru", "name20");
                Item item20 = new Item(2, "name20", "description20", true, 3, null);

                LocalDateTime start20 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
                LocalDateTime end20 = start20;
                BookingDto bookingDto20 =
                        new BookingDto(null, start20, end20, StatusBooking.WAITING, 1, 1, "itemName1");

                when(userService.getUserById(anyInt()))
                        .thenReturn(userDto20);
                when(userService.getUserEntityById(anyInt()))
                        .thenReturn(user20);
                when(itemService.getItemEntityById(anyInt()))
                        .thenReturn(item20);


                BookingResponseDto actualBooking = bookingService.addBooking(2, bookingDto20);
            }
        });
        Assertions.assertEquals("start and end have one time", exc.getMessage());

        Mockito.verify(userService, Mockito.times(1)).getUserById(anyInt());
        Mockito.verify(userService, Mockito.times(1)).getUserEntityById(anyInt());
        Mockito.verify(itemService, Mockito.times(1)).getItemEntityById(anyInt());

        Mockito.verify(bookingMapper, Mockito.times(1)).toBooking(any(), any(), any());

        Mockito.verifyNoMoreInteractions(userService);
        Mockito.verifyNoMoreInteractions(itemService);
        Mockito.verifyNoMoreInteractions(bookingMapper);
    }

    @Test
    void getBookingByIdTest() {
        UserDto userDtoForCheck2 = new UserDto(1, "name2", "email2@mail.ru");
        User user2 = new User(1, "name2", "email2@mail.ru");
        Item item2 = new Item(2, "name2", "description2", true, 4, null);
        LocalDateTime start2 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(50);
        LocalDateTime end2 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(100);
        UserBookingDto userBookingDto2 = new UserBookingDto(1);
        ItemBookingDto itemBookingDto2 = new ItemBookingDto(2, "name2");
        BookingResponseDto bookingResponseDto2 =
                new BookingResponseDto(1, start2, end2, StatusBooking.WAITING, userBookingDto2, itemBookingDto2);
        Booking booking2 = new Booking(1, start2, end2, StatusBooking.WAITING, item2, user2);


        when(userService.getUserById(anyInt()))
                .thenReturn(userDtoForCheck2);
        when(bookingStorage.findById(anyInt()))
                .thenReturn(Optional.ofNullable(booking2));

        BookingResponseDto actualBooking = bookingService.getBookingById(1, 1);

        Assertions.assertEquals(bookingResponseDto2, actualBooking);

        Mockito.verify(userService, Mockito.times(1)).getUserById(anyInt());
        Mockito.verify(bookingStorage, Mockito.times(1)).findById(anyInt());

        Mockito.verify(bookingMapper, Mockito.times(1)).toBookingRequestDto(any());

        Mockito.verifyNoMoreInteractions(userService);
        Mockito.verifyNoMoreInteractions(bookingStorage);
        Mockito.verifyNoMoreInteractions(bookingMapper);
    }

    @Test
    void getBookingById_404_Test() {
        ValidateException exc = assertThrows(ValidateException.class, new Executable() {
            @Override
            public void execute() throws ValidateException {
                UserDto userDtoForCheck2 = new UserDto(1, "name2", "email2@mail.ru");
                User user2 = new User(3, "name2", "email2@mail.ru");
                Item item2 = new Item(2, "name2", "description2", true, 4, null);
                LocalDateTime start2 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(50);
                LocalDateTime end2 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(100);
                UserBookingDto userBookingDto2 = new UserBookingDto(1);
                ItemBookingDto itemBookingDto2 = new ItemBookingDto(2, "name2");
                BookingResponseDto bookingResponseDto2 =
                        new BookingResponseDto(1, start2, end2, StatusBooking.WAITING, userBookingDto2, itemBookingDto2);
                Booking booking2 = new Booking(1, start2, end2, StatusBooking.WAITING, item2, user2);


                when(userService.getUserById(anyInt()))
                        .thenReturn(userDtoForCheck2);
                when(bookingStorage.findById(anyInt()))
                        .thenReturn(Optional.ofNullable(booking2));

                BookingResponseDto actualBooking = bookingService.getBookingById(1, 1);
            }
        });
        Assertions.assertEquals("booking may be seen only by owner item or owner booking", exc.getMessage());

        Mockito.verify(userService, Mockito.times(1)).getUserById(anyInt());
        Mockito.verify(bookingStorage, Mockito.times(1)).findById(anyInt());

        Mockito.verifyNoMoreInteractions(userService);
        Mockito.verifyNoMoreInteractions(bookingStorage);
        Mockito.verifyNoMoreInteractions(bookingMapper);
    }

    @Test
    void getBookingsByUserId_WAITING_200_Test() {
        UserDto userDtoForCheck3 = new UserDto(1, "name3", "email3@mail.ru");
        User user3 = new User(1, "name3", "email3@mail.ru");
        Item item3 = new Item(2, "name3", "description3", true, 4, null);
        LocalDateTime start3 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(50);
        LocalDateTime end3 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(100);
        UserBookingDto userBookingDto3 = new UserBookingDto(1);
        ItemBookingDto itemBookingDto3 = new ItemBookingDto(2, "name3");
        BookingResponseDto bookingResponseDto3 =
                new BookingResponseDto(1, start3, end3, StatusBooking.WAITING, userBookingDto3, itemBookingDto3);
        Booking booking3 = new Booking(1, start3, end3, StatusBooking.WAITING, item3, user3);


        when(userService.getUserById(anyInt()))
                .thenReturn(userDtoForCheck3);
        when(bookingStorage.findByUserIdAndStatus(anyInt(), any(), any()))
                .thenReturn(getBookings(List.of(booking3)));

        List<BookingResponseDto> actualBooking =
                bookingService.getBookingsByUserId(1, StateBooking.WAITING, 0, 1);

        Assertions.assertEquals(List.of(bookingResponseDto3), actualBooking);

        Mockito.verify(userService, Mockito.times(1)).getUserById(anyInt());
        Mockito.verify(bookingStorage, Mockito.times(1)).findByUserIdAndStatus(anyInt(), any(), any());

        Mockito.verify(bookingMapper, Mockito.times(1)).toBookingRequestDto(any());

        Mockito.verifyNoMoreInteractions(userService);
        Mockito.verifyNoMoreInteractions(bookingStorage);
        Mockito.verifyNoMoreInteractions(bookingMapper);
    }

    @Test
    void getBookingsByUserId_CURRENT_200_Test() {
        UserDto userDtoForCheck3 = new UserDto(1, "name3", "email3@mail.ru");
        User user3 = new User(1, "name3", "email3@mail.ru");
        Item item3 = new Item(2, "name3", "description3", true, 4, null);
        LocalDateTime start3 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).minusMinutes(50);
        LocalDateTime end3 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(50);
        UserBookingDto userBookingDto3 = new UserBookingDto(1);
        ItemBookingDto itemBookingDto3 = new ItemBookingDto(2, "name3");
        BookingResponseDto bookingResponseDto3 =
                new BookingResponseDto(1, start3, end3, StatusBooking.WAITING, userBookingDto3, itemBookingDto3);
        Booking booking3 = new Booking(1, start3, end3, StatusBooking.WAITING, item3, user3);


        when(userService.getUserById(anyInt()))
                .thenReturn(userDtoForCheck3);
        when(bookingStorage.findByUserIdAndStartBeforeAndEndAfter(anyInt(), any(), any(), any()))
                .thenReturn(getBookings(List.of(booking3)));

        List<BookingResponseDto> actualBooking =
                bookingService.getBookingsByUserId(1, StateBooking.CURRENT, 0, 1);

        Assertions.assertEquals(List.of(bookingResponseDto3), actualBooking);

        Mockito.verify(userService, Mockito.times(1)).getUserById(anyInt());
        Mockito.verify(bookingStorage,
                Mockito.times(1)).findByUserIdAndStartBeforeAndEndAfter(anyInt(), any(), any(), any());

        Mockito.verify(bookingMapper, Mockito.times(1)).toBookingRequestDto(any());

        Mockito.verifyNoMoreInteractions(userService);
        Mockito.verifyNoMoreInteractions(bookingStorage);
        Mockito.verifyNoMoreInteractions(bookingMapper);
    }

    @Test
    void getBookingsByUserId_FUTURE_200_Test() {
        UserDto userDtoForCheck3 = new UserDto(1, "name3", "email3@mail.ru");
        User user3 = new User(1, "name3", "email3@mail.ru");
        Item item3 = new Item(2, "name3", "description3", true, 4, null);
        LocalDateTime start3 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(50);
        LocalDateTime end3 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(150);
        UserBookingDto userBookingDto3 = new UserBookingDto(1);
        ItemBookingDto itemBookingDto3 = new ItemBookingDto(2, "name3");
        BookingResponseDto bookingResponseDto3 =
                new BookingResponseDto(1, start3, end3, StatusBooking.WAITING, userBookingDto3, itemBookingDto3);
        Booking booking3 = new Booking(1, start3, end3, StatusBooking.WAITING, item3, user3);


        when(userService.getUserById(anyInt()))
                .thenReturn(userDtoForCheck3);
        when(bookingStorage.findByUserIdAndStartAfter(anyInt(), any(), any()))
                .thenReturn(getBookings(List.of(booking3)));

        List<BookingResponseDto> actualBooking =
                bookingService.getBookingsByUserId(1, StateBooking.FUTURE, 0, 1);

        Assertions.assertEquals(List.of(bookingResponseDto3), actualBooking);

        Mockito.verify(userService, Mockito.times(1)).getUserById(anyInt());
        Mockito.verify(bookingStorage,
                Mockito.times(1)).findByUserIdAndStartAfter(anyInt(), any(), any());

        Mockito.verify(bookingMapper, Mockito.times(1)).toBookingRequestDto(any());

        Mockito.verifyNoMoreInteractions(userService);
        Mockito.verifyNoMoreInteractions(bookingStorage);
        Mockito.verifyNoMoreInteractions(bookingMapper);
    }

    @Test
    void getBookingsByUserId_PAST_200_Test() {
        UserDto userDtoForCheck3 = new UserDto(1, "name3", "email3@mail.ru");
        User user3 = new User(1, "name3", "email3@mail.ru");
        Item item3 = new Item(2, "name3", "description3", true, 4, null);
        LocalDateTime start3 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).minusMinutes(500);
        LocalDateTime end3 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).minusMinutes(200);
        UserBookingDto userBookingDto3 = new UserBookingDto(1);
        ItemBookingDto itemBookingDto3 = new ItemBookingDto(2, "name3");
        BookingResponseDto bookingResponseDto3 =
                new BookingResponseDto(1, start3, end3, StatusBooking.WAITING, userBookingDto3, itemBookingDto3);
        Booking booking3 = new Booking(1, start3, end3, StatusBooking.WAITING, item3, user3);


        when(userService.getUserById(anyInt()))
                .thenReturn(userDtoForCheck3);
        when(bookingStorage.findByUserIdAndEndBefore(anyInt(), any(), any()))
                .thenReturn(getBookings(List.of(booking3)));

        List<BookingResponseDto> actualBooking =
                bookingService.getBookingsByUserId(1, StateBooking.PAST, 0, 1);

        Assertions.assertEquals(List.of(bookingResponseDto3), actualBooking);

        Mockito.verify(userService, Mockito.times(1)).getUserById(anyInt());
        Mockito.verify(bookingStorage,
                Mockito.times(1)).findByUserIdAndEndBefore(anyInt(), any(), any());

        Mockito.verify(bookingMapper, Mockito.times(1)).toBookingRequestDto(any());

        Mockito.verifyNoMoreInteractions(userService);
        Mockito.verifyNoMoreInteractions(bookingStorage);
        Mockito.verifyNoMoreInteractions(bookingMapper);
    }

    @Test
    void getBookingsByUserId_ALL_200_Test() {
        UserDto userDtoForCheck3 = new UserDto(1, "name3", "email3@mail.ru");
        User user3 = new User(1, "name3", "email3@mail.ru");
        Item item3 = new Item(2, "name3", "description3", true, 4, null);
        LocalDateTime start3 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).minusMinutes(500);
        LocalDateTime end3 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).minusMinutes(200);
        UserBookingDto userBookingDto3 = new UserBookingDto(1);
        ItemBookingDto itemBookingDto3 = new ItemBookingDto(2, "name3");
        BookingResponseDto bookingResponseDto3 =
                new BookingResponseDto(1, start3, end3, StatusBooking.WAITING, userBookingDto3, itemBookingDto3);
        Booking booking3 = new Booking(1, start3, end3, StatusBooking.WAITING, item3, user3);


        when(userService.getUserById(anyInt()))
                .thenReturn(userDtoForCheck3);
        when(bookingStorage.findAllByUserId(anyInt(), any()))
                .thenReturn(getBookings(List.of(booking3)));

        List<BookingResponseDto> actualBooking =
                bookingService.getBookingsByUserId(1, StateBooking.ALL, 0, 1);

        Assertions.assertEquals(List.of(bookingResponseDto3), actualBooking);

        Mockito.verify(userService, Mockito.times(1)).getUserById(anyInt());
        Mockito.verify(bookingStorage,
                Mockito.times(1)).findAllByUserId(anyInt(), any());

        Mockito.verify(bookingMapper, Mockito.times(1)).toBookingRequestDto(any());

        Mockito.verifyNoMoreInteractions(userService);
        Mockito.verifyNoMoreInteractions(bookingStorage);
        Mockito.verifyNoMoreInteractions(bookingMapper);
    }

    @Test
    void getBookingsByUserId_REJECTED_200_Test() {
        UserDto userDtoForCheck3 = new UserDto(1, "name3", "email3@mail.ru");
        User user3 = new User(1, "name3", "email3@mail.ru");
        Item item3 = new Item(2, "name3", "description3", true, 4, null);
        LocalDateTime start3 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).minusMinutes(500);
        LocalDateTime end3 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).minusMinutes(200);
        UserBookingDto userBookingDto3 = new UserBookingDto(1);
        ItemBookingDto itemBookingDto3 = new ItemBookingDto(2, "name3");
        BookingResponseDto bookingResponseDto3 =
                new BookingResponseDto(1, start3, end3, StatusBooking.REJECTED, userBookingDto3, itemBookingDto3);
        Booking booking3 = new Booking(1, start3, end3, StatusBooking.REJECTED, item3, user3);


        when(userService.getUserById(anyInt()))
                .thenReturn(userDtoForCheck3);
        when(bookingStorage.findByUserIdAndStatus(anyInt(), any(), any()))
                .thenReturn(getBookings(List.of(booking3)));

        List<BookingResponseDto> actualBooking =
                bookingService.getBookingsByUserId(1, StateBooking.REJECTED, 0, 1);

        Assertions.assertEquals(List.of(bookingResponseDto3), actualBooking);

        Mockito.verify(userService, Mockito.times(1)).getUserById(anyInt());
        Mockito.verify(bookingStorage,
                Mockito.times(1)).findByUserIdAndStatus(anyInt(), any(), any());

        Mockito.verify(bookingMapper, Mockito.times(1)).toBookingRequestDto(any());

        Mockito.verifyNoMoreInteractions(userService);
        Mockito.verifyNoMoreInteractions(bookingStorage);
        Mockito.verifyNoMoreInteractions(bookingMapper);
    }

    @Test
    void getBookingsByUserId_UNSUPPORTED_500_Test() {
        UnsupportedException exc = assertThrows(UnsupportedException.class, new Executable() {
            @Override
            public void execute() throws UnsupportedException {
                UserDto userDtoForCheck4 = new UserDto(1, "name4", "email4@mail.ru");

                when(userService.getUserById(anyInt()))
                        .thenReturn(userDtoForCheck4);

                List<BookingResponseDto> actualBooking =
                        bookingService.getBookingsByUserId(1,
                                StateBooking.valueOf("UNSUPPORTED_STATUS"), null, null);

            }
        });
        Assertions.assertEquals("Unknown state: UNSUPPORTED_STATUS", exc.getMessage());

        Mockito.verify(userService, Mockito.times(1)).getUserById(anyInt());

        Mockito.verifyNoMoreInteractions(userService);
        Mockito.verifyNoMoreInteractions(bookingStorage);
        Mockito.verifyNoMoreInteractions(bookingMapper);
    }

    @Test
    void getBookingsByOwnerItem_WAITING_200_Test() {
        UserDto userDtoForCheck4 = new UserDto(1, "name4", "email4@mail.ru");
        User user4 = new User(1, "name4", "email4@mail.ru");
        Item item4 = new Item(2, "name4", "description4", true, 4, null);
        LocalDateTime start4 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(50);
        LocalDateTime end4 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(100);
        UserBookingDto userBookingDto4 = new UserBookingDto(1);
        ItemBookingDto itemBookingDto4 = new ItemBookingDto(2, "name4");
        BookingResponseDto bookingResponseDto4 =
                new BookingResponseDto(1, start4, end4, StatusBooking.WAITING, userBookingDto4, itemBookingDto4);
        Booking booking4 = new Booking(1, start4, end4, StatusBooking.WAITING, item4, user4);

        when(userService.getUserById(anyInt()))
                .thenReturn(userDtoForCheck4);
        when(bookingStorage.findByItemOwnerIdAndStatus(anyInt(), any(), any()))
                .thenReturn(getBookings(List.of(booking4)));

        List<BookingResponseDto> actualBooking =
                bookingService.getBookingsByOwnerItem(1, StateBooking.WAITING, 0, 1);

        Assertions.assertEquals(List.of(bookingResponseDto4), actualBooking);

        Mockito.verify(userService, Mockito.times(1)).getUserById(anyInt());
        Mockito.verify(bookingStorage, Mockito.times(1)).findByItemOwnerIdAndStatus(anyInt(), any(), any());

        Mockito.verify(bookingMapper, Mockito.times(1)).toBookingRequestDto(any());

        Mockito.verifyNoMoreInteractions(userService);
        Mockito.verifyNoMoreInteractions(bookingStorage);
        Mockito.verifyNoMoreInteractions(bookingMapper);
    }

    @Test
    void getBookingsByOwnerItem_REJECTED_200_Test() {
        UserDto userDtoForCheck4 = new UserDto(1, "name4", "email4@mail.ru");
        User user4 = new User(1, "name4", "email4@mail.ru");
        Item item4 = new Item(2, "name4", "description4", true, 4, null);
        LocalDateTime start4 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(50);
        LocalDateTime end4 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(100);
        UserBookingDto userBookingDto4 = new UserBookingDto(1);
        ItemBookingDto itemBookingDto4 = new ItemBookingDto(2, "name4");
        BookingResponseDto bookingResponseDto4 =
                new BookingResponseDto(1, start4, end4, StatusBooking.REJECTED, userBookingDto4, itemBookingDto4);
        BookingResponseDto bookingResponseDto5 =
                new BookingResponseDto(3, start4.plusMinutes(50), end4.plusMinutes(100), StatusBooking.REJECTED, userBookingDto4, itemBookingDto4);

        Booking booking4 = new Booking(1, start4, end4, StatusBooking.REJECTED, item4, user4);
        Booking booking5 = new Booking(3, start4.plusMinutes(50), end4.plusMinutes(100), StatusBooking.REJECTED, item4, user4);

        when(userService.getUserById(anyInt()))
                .thenReturn(userDtoForCheck4);
        when(bookingStorage.findByItemOwnerIdAndStatus(anyInt(), any(), any()))
                .thenReturn(getBookings(List.of(booking4, booking5)));

        List<BookingResponseDto> actualBooking =
                bookingService.getBookingsByOwnerItem(1, StateBooking.REJECTED, 0, 2);

        Assertions.assertEquals(List.of(bookingResponseDto4, bookingResponseDto5), actualBooking);

        Mockito.verify(userService, Mockito.times(1)).getUserById(anyInt());
        Mockito.verify(bookingStorage, Mockito.times(1)).findByItemOwnerIdAndStatus(anyInt(), any(), any());

        Mockito.verify(bookingMapper, Mockito.times(2)).toBookingRequestDto(any());

        Mockito.verifyNoMoreInteractions(userService);
        Mockito.verifyNoMoreInteractions(bookingStorage);
        Mockito.verifyNoMoreInteractions(bookingMapper);
    }

    @Test
    void getBookingsByOwnerItem_ALL_200_Test() {
        UserDto userDtoForCheck4 = new UserDto(1, "name4", "email4@mail.ru");

        User user4 = new User(1, "name4", "email4@mail.ru");

        Item item4 = new Item(2, "name4", "description4", true, 1, null);
        Item item5 = new Item(3, "name5", "description5", true, 1, null);

        LocalDateTime start4 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(50);
        LocalDateTime end4 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(100);
        UserBookingDto userBookingDto4 = new UserBookingDto(1);

        ItemBookingDto itemBookingDto4 = new ItemBookingDto(3, "name5");

        BookingResponseDto bookingResponseDto4 =
                new BookingResponseDto(1, start4, end4, StatusBooking.REJECTED, userBookingDto4, itemBookingDto4);
        BookingResponseDto bookingResponseDto5 =
                new BookingResponseDto(3, start4.plusMinutes(50), end4.plusMinutes(100), StatusBooking.REJECTED, userBookingDto4, itemBookingDto4);

        Booking booking4 = new Booking(1, start4, end4, StatusBooking.REJECTED, item5, user4);
        Booking booking5 = new Booking(3, start4.plusMinutes(50), end4.plusMinutes(100), StatusBooking.REJECTED, item5, user4);

        when(userService.getUserById(anyInt()))
                .thenReturn(userDtoForCheck4);
        when(bookingStorage.findAllByItemOwnerId(anyInt(), any()))
                .thenReturn(getBookings(List.of(booking4, booking5)));

        List<BookingResponseDto> actualBooking =
                bookingService.getBookingsByOwnerItem(1, StateBooking.ALL, 0, 20);

        Assertions.assertEquals(List.of(bookingResponseDto4, bookingResponseDto5), actualBooking);

        Mockito.verify(userService, Mockito.times(1)).getUserById(anyInt());
        Mockito.verify(bookingStorage, Mockito.times(1)).findAllByItemOwnerId(anyInt(), any());

        Mockito.verify(bookingMapper, Mockito.times(2)).toBookingRequestDto(any());

        Mockito.verifyNoMoreInteractions(userService);
        Mockito.verifyNoMoreInteractions(bookingStorage);
        Mockito.verifyNoMoreInteractions(bookingMapper);
    }

    @Test
    void getBookingsByOwnerItem_PAST_200_Test() {
        UserDto userDtoForCheck4 = new UserDto(1, "name4", "email4@mail.ru");

        User user4 = new User(1, "name4", "email4@mail.ru");

        Item item4 = new Item(2, "name4", "description4", true, 1, null);

        LocalDateTime start4 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).minusMinutes(500);
        LocalDateTime end4 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).minusMinutes(100);

        UserBookingDto userBookingDto4 = new UserBookingDto(1);

        ItemBookingDto itemBookingDto4 = new ItemBookingDto(2, "name4");

        BookingResponseDto bookingResponseDto4 =
                new BookingResponseDto(1, start4, end4, StatusBooking.APPROVED, userBookingDto4, itemBookingDto4);
        BookingResponseDto bookingResponseDto5 =
                new BookingResponseDto(3, start4, end4, StatusBooking.APPROVED, userBookingDto4, itemBookingDto4);

        Booking booking4 = new Booking(1, start4, end4, StatusBooking.APPROVED, item4, user4);
        Booking booking5 = new Booking(3, start4, end4, StatusBooking.APPROVED, item4, user4);

        when(userService.getUserById(anyInt()))
                .thenReturn(userDtoForCheck4);
        when(bookingStorage.findByItemOwnerIdAndEndBefore(anyInt(), any(), any()))
                .thenReturn(getBookings(List.of(booking4, booking5)));

        List<BookingResponseDto> actualBooking =
                bookingService.getBookingsByOwnerItem(1, StateBooking.PAST, 0, 20);

        Assertions.assertEquals(List.of(bookingResponseDto4, bookingResponseDto5), actualBooking);

        Mockito.verify(userService, Mockito.times(1)).getUserById(anyInt());
        Mockito.verify(bookingStorage, Mockito.times(1)).findByItemOwnerIdAndEndBefore(anyInt(), any(), any());

        Mockito.verify(bookingMapper, Mockito.times(2)).toBookingRequestDto(any());

        Mockito.verifyNoMoreInteractions(userService);
        Mockito.verifyNoMoreInteractions(bookingStorage);
        Mockito.verifyNoMoreInteractions(bookingMapper);
    }

    @Test
    void getBookingsByOwnerItem_FUTURE_200_Test() {
        UserDto userDtoForCheck4 = new UserDto(1, "name4", "email4@mail.ru");

        User user4 = new User(1, "name4", "email4@mail.ru");

        Item item4 = new Item(2, "name4", "description4", true, 1, null);


        LocalDateTime start4 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(200);
        LocalDateTime end4 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(500);

        UserBookingDto userBookingDto4 = new UserBookingDto(1);

        ItemBookingDto itemBookingDto4 = new ItemBookingDto(2, "name4");

        BookingResponseDto bookingResponseDto4 =
                new BookingResponseDto(1, start4, end4, StatusBooking.APPROVED, userBookingDto4, itemBookingDto4);
        BookingResponseDto bookingResponseDto5 =
                new BookingResponseDto(3, start4, end4, StatusBooking.APPROVED, userBookingDto4, itemBookingDto4);

        Booking booking4 = new Booking(1, start4, end4, StatusBooking.APPROVED, item4, user4);
        Booking booking5 = new Booking(3, start4, end4, StatusBooking.APPROVED, item4, user4);

        when(userService.getUserById(anyInt()))
                .thenReturn(userDtoForCheck4);
        when(bookingStorage.findByItemOwnerIdAndStartAfter(anyInt(), any(), any()))
                .thenReturn(getBookings(List.of(booking4, booking5)));

        List<BookingResponseDto> actualBooking =
                bookingService.getBookingsByOwnerItem(1, StateBooking.FUTURE, 0, 20);

        Assertions.assertEquals(List.of(bookingResponseDto4, bookingResponseDto5), actualBooking);

        Mockito.verify(userService, Mockito.times(1)).getUserById(anyInt());
        Mockito.verify(bookingStorage, Mockito.times(1)).findByItemOwnerIdAndStartAfter(anyInt(), any(), any());

        Mockito.verify(bookingMapper, Mockito.times(2)).toBookingRequestDto(any());

        Mockito.verifyNoMoreInteractions(userService);
        Mockito.verifyNoMoreInteractions(bookingStorage);
        Mockito.verifyNoMoreInteractions(bookingMapper);
    }

    @Test
    void getBookingsByOwnerItem_CURRENT_200_Test() {
        UserDto userDtoForCheck4 = new UserDto(1, "name4", "email4@mail.ru");

        User user4 = new User(1, "name4", "email4@mail.ru");

        Item item4 = new Item(2, "name4", "description4", true, 1, null);


        LocalDateTime start4 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).minusMinutes(500);
        LocalDateTime end4 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(500);

        UserBookingDto userBookingDto4 = new UserBookingDto(1);

        ItemBookingDto itemBookingDto4 = new ItemBookingDto(2, "name4");

        BookingResponseDto bookingResponseDto4 =
                new BookingResponseDto(1, start4, end4, StatusBooking.APPROVED, userBookingDto4, itemBookingDto4);
        BookingResponseDto bookingResponseDto5 =
                new BookingResponseDto(3, start4, end4, StatusBooking.APPROVED, userBookingDto4, itemBookingDto4);

        Booking booking4 = new Booking(1, start4, end4, StatusBooking.APPROVED, item4, user4);
        Booking booking5 = new Booking(3, start4, end4, StatusBooking.APPROVED, item4, user4);

        when(userService.getUserById(anyInt()))
                .thenReturn(userDtoForCheck4);
        when(bookingStorage.findByItemOwnerIdAndStartBeforeAndEndAfter(anyInt(), any(), any(), any()))
                .thenReturn(getBookings(List.of(booking4, booking5)));

        List<BookingResponseDto> actualBooking =
                bookingService.getBookingsByOwnerItem(1, StateBooking.CURRENT, 0, 20);

        Assertions.assertEquals(List.of(bookingResponseDto4, bookingResponseDto5), actualBooking);

        Mockito.verify(userService, Mockito.times(1)).getUserById(anyInt());
        Mockito.verify(bookingStorage, Mockito.times(1))
                .findByItemOwnerIdAndStartBeforeAndEndAfter(anyInt(), any(), any(), any());

        Mockito.verify(bookingMapper, Mockito.times(2)).toBookingRequestDto(any());

        Mockito.verifyNoMoreInteractions(userService);
        Mockito.verifyNoMoreInteractions(bookingStorage);
        Mockito.verifyNoMoreInteractions(bookingMapper);
    }

    @Test
    void getBookingsByOwnerItem_UNSUPPORTED_500_Test() {
        UnsupportedException exc = assertThrows(UnsupportedException.class, new Executable() {
            @Override
            public void execute() throws UnsupportedException {
                UserDto userDtoForCheck4 = new UserDto(1, "name4", "email4@mail.ru");

                when(userService.getUserById(anyInt()))
                        .thenReturn(userDtoForCheck4);

                List<BookingResponseDto> actualBooking =
                        bookingService.getBookingsByOwnerItem(1,
                                StateBooking.valueOf("UNSUPPORTED_STATUS"), null, null);

            }
        });
        Assertions.assertEquals("Unknown state: UNSUPPORTED_STATUS", exc.getMessage());

        Mockito.verify(userService, Mockito.times(1)).getUserById(anyInt());

        Mockito.verifyNoMoreInteractions(userService);
        Mockito.verifyNoMoreInteractions(bookingStorage);
        Mockito.verifyNoMoreInteractions(bookingMapper);
    }

    @Test
    void checkBookingByUserIdAndItemId_200_Test() {
        User user4 = new User(1, "name4", "email4@mail.ru");
        Item item4 = new Item(2, "name4", "description4", true, 1, null);

        LocalDateTime start4 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).minusMinutes(500);
        LocalDateTime end4 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(500);

        Booking booking12 = new Booking(1, start4, end4, StatusBooking.APPROVED, item4, user4);

        when(bookingStorage.findByUserIdAndItemIdAndEndBeforeAndStatus(anyInt(), anyInt(), any(), any()))
                .thenReturn(List.of(booking12));

        bookingService.checkBookingByUserIdAndItemId(1, 1);

        Mockito.verify(bookingStorage,
                Mockito.times(1)).findByUserIdAndItemIdAndEndBeforeAndStatus(anyInt(),
                anyInt(), any(), any());

        Mockito.verifyNoMoreInteractions(bookingStorage);
    }

    @Test
    void checkBookingByUserIdAndItemId_400_Test() {
        ValidException exc = assertThrows(ValidException.class, new Executable() {
            @Override
            public void execute() throws ValidException {
                User user4 = new User(1, "name4", "email4@mail.ru");
                Item item4 = new Item(2, "name4", "description4", true, 1, null);

                LocalDateTime start4 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).minusMinutes(500);
                LocalDateTime end4 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(500);

                Booking booking12 = new Booking(1, start4, end4, StatusBooking.APPROVED, item4, user4);

                when(bookingStorage.findByUserIdAndItemIdAndEndBeforeAndStatus(anyInt(), anyInt(), any(), any()))
                        .thenReturn(new ArrayList<>());

                bookingService.checkBookingByUserIdAndItemId(1, 1);

            }
        });
        Assertions.assertEquals("booking by user - " + 1 + " on item - " + 1 + " not found", exc.getMessage());

        Mockito.verify(bookingStorage,
                Mockito.times(1)).findByUserIdAndItemIdAndEndBeforeAndStatus(anyInt(),
                anyInt(), any(), any());

        Mockito.verifyNoMoreInteractions(bookingStorage);
    }

    @Test
    void updateStatusBooking_APPROWED_200_Test() {
        UserDto userDtoForCheck4 = new UserDto(1, "name4", "email4@mail.ru");

        User user4 = new User(1, "name4", "email4@mail.ru");
        Item item4 = new Item(2, "name4", "description4", true, 1, null);

        LocalDateTime start4 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).minusMinutes(500);
        LocalDateTime end4 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(500);

        Booking booking12 = new Booking(1, start4, end4, StatusBooking.WAITING, item4, user4);
        Booking booking13 = new Booking(1, start4, end4, StatusBooking.APPROVED, item4, user4);

        UserBookingDto userBookingDto = new UserBookingDto(1);
        ItemBookingDto itemBookingDto = new ItemBookingDto(2, "name4");
        BookingResponseDto expected =
                new BookingResponseDto(1, start4, end4, StatusBooking.APPROVED, userBookingDto, itemBookingDto);

        when(userService.getUserById(anyInt()))
                .thenReturn(userDtoForCheck4);
        when(bookingStorage.findById(anyInt()))
                .thenReturn(Optional.of(booking12));
        when(bookingStorage.save(any()))
                .thenReturn(booking13);

        BookingResponseDto actualBookingDto = bookingService.updateStatusBooking(1, 1, true);

        Assertions.assertEquals(expected, actualBookingDto);

        Mockito.verify(userService, Mockito.times(1)).getUserById(anyInt());
        Mockito.verify(bookingStorage, Mockito.times(1)).findById(anyInt());
        Mockito.verify(bookingStorage, Mockito.times(1)).save(any());

        Mockito.verify(bookingMapper, Mockito.times(1)).toBookingRequestDto(any());

        Mockito.verifyNoMoreInteractions(userService);
        Mockito.verifyNoMoreInteractions(bookingStorage);
        Mockito.verifyNoMoreInteractions(bookingMapper);
    }

    @Test
    void updateStatusBooking_REJECTED_200_Test() {
        UserDto userDtoForCheck4 = new UserDto(1, "name4", "email4@mail.ru");

        User user4 = new User(1, "name4", "email4@mail.ru");
        Item item4 = new Item(2, "name4", "description4", true, 1, null);

        LocalDateTime start4 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).minusMinutes(500);
        LocalDateTime end4 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(500);

        Booking booking12 = new Booking(1, start4, end4, StatusBooking.WAITING, item4, user4);
        Booking booking13 = new Booking(1, start4, end4, StatusBooking.REJECTED, item4, user4);

        UserBookingDto userBookingDto = new UserBookingDto(1);
        ItemBookingDto itemBookingDto = new ItemBookingDto(2, "name4");
        BookingResponseDto expected =
                new BookingResponseDto(1, start4, end4, StatusBooking.REJECTED, userBookingDto, itemBookingDto);

        when(userService.getUserById(anyInt()))
                .thenReturn(userDtoForCheck4);
        when(bookingStorage.findById(anyInt()))
                .thenReturn(Optional.of(booking12));
        when(bookingStorage.save(any()))
                .thenReturn(booking13);

        BookingResponseDto actualBookingDto = bookingService.updateStatusBooking(1, 1, false);

        Assertions.assertEquals(expected, actualBookingDto);

        Mockito.verify(userService, Mockito.times(1)).getUserById(anyInt());
        Mockito.verify(bookingStorage, Mockito.times(1)).findById(anyInt());
        Mockito.verify(bookingStorage, Mockito.times(1)).save(any());

        Mockito.verify(bookingMapper, Mockito.times(1)).toBookingRequestDto(any());

        Mockito.verifyNoMoreInteractions(userService);
        Mockito.verifyNoMoreInteractions(bookingStorage);
        Mockito.verifyNoMoreInteractions(bookingMapper);
    }

    @Test
    void updateStatusBooking_status_has_been_approved_400_Test() {
        ValidException exc = assertThrows(ValidException.class, new Executable() {
            @Override
            public void execute() throws ValidException {
                UserDto userDtoForCheck4 = new UserDto(1, "name4", "email4@mail.ru");

                User user4 = new User(1, "name4", "email4@mail.ru");
                Item item4 = new Item(2, "name4", "description4", true, 1, null);

                LocalDateTime start4 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).minusMinutes(500);
                LocalDateTime end4 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(500);

                Booking booking12 = new Booking(1, start4, end4, StatusBooking.APPROVED, item4, user4);

                when(userService.getUserById(anyInt()))
                        .thenReturn(userDtoForCheck4);
                when(bookingStorage.findById(anyInt()))
                        .thenReturn(Optional.of(booking12));

                BookingResponseDto actualBookingDto = bookingService.updateStatusBooking(1, 1, false);

            }
        });
        Assertions.assertEquals("the status has already been approved", exc.getMessage());

        Mockito.verify(userService, Mockito.times(1)).getUserById(anyInt());
        Mockito.verify(bookingStorage, Mockito.times(1)).findById(anyInt());

        Mockito.verifyNoMoreInteractions(userService);
        Mockito.verifyNoMoreInteractions(bookingStorage);
    }

    @Test
    void updateStatusBooking_status_has_been_rejected_400_Test() {
        ValidException exc = assertThrows(ValidException.class, new Executable() {
            @Override
            public void execute() throws ValidException {
                UserDto userDtoForCheck4 = new UserDto(1, "name4", "email4@mail.ru");

                User user4 = new User(1, "name4", "email4@mail.ru");
                Item item4 = new Item(2, "name4", "description4", true, 1, null);

                LocalDateTime start4 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).minusMinutes(500);
                LocalDateTime end4 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(500);

                Booking booking12 = new Booking(1, start4, end4, StatusBooking.REJECTED, item4, user4);

                when(userService.getUserById(anyInt()))
                        .thenReturn(userDtoForCheck4);
                when(bookingStorage.findById(anyInt()))
                        .thenReturn(Optional.of(booking12));

                BookingResponseDto actualBookingDto = bookingService.updateStatusBooking(1, 1, false);

            }
        });
        Assertions.assertEquals("the status has been rejected", exc.getMessage());

        Mockito.verify(userService, Mockito.times(1)).getUserById(anyInt());
        Mockito.verify(bookingStorage, Mockito.times(1)).findById(anyInt());

        Mockito.verifyNoMoreInteractions(userService);
        Mockito.verifyNoMoreInteractions(bookingStorage);
    }

    @Test
    void updateStatusBooking_not_owner_item_404_Test() {
        ValidateException exc = assertThrows(ValidateException.class, new Executable() {
            @Override
            public void execute() throws ValidateException {
                UserDto userDtoForCheck4 = new UserDto(1, "name4", "email4@mail.ru");

                User user4 = new User(1, "name4", "email4@mail.ru");
                Item item4 = new Item(2, "name4", "description4", true, 2, null);

                LocalDateTime start4 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).minusMinutes(500);
                LocalDateTime end4 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(500);

                Booking booking12 = new Booking(1, start4, end4, StatusBooking.WAITING, item4, user4);

                when(userService.getUserById(anyInt()))
                        .thenReturn(userDtoForCheck4);
                when(bookingStorage.findById(anyInt()))
                        .thenReturn(Optional.of(booking12));

                BookingResponseDto actualBookingDto = bookingService.updateStatusBooking(1, 1, false);

            }
        });
        Assertions.assertEquals("user - " + 1 + " is not owner item - " + 2, exc.getMessage());

        Mockito.verify(userService, Mockito.times(1)).getUserById(anyInt());
        Mockito.verify(bookingStorage, Mockito.times(1)).findById(anyInt());

        Mockito.verifyNoMoreInteractions(userService);
        Mockito.verifyNoMoreInteractions(bookingStorage);
    }

    private Page<Booking> getBookings(List<Booking> bookings) {

        Pageable pageRequest = PageRequest.of(0, 20);

        List<Booking> allBookings = bookings;
        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), allBookings.size());

        List<Booking> pageContent = allBookings.subList(start, end);
        return new PageImpl<>(pageContent, pageRequest, allBookings.size());
    }
}
