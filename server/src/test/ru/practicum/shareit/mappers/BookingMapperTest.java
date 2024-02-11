package ru.practicum.shareit.mappers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.shareit.booking.StatusBooking;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ContextConfiguration(classes = {BookingMapper.class})
public class BookingMapperTest {
    @Autowired
    BookingMapper bookingMapper;

    @Test
    void toBooking_id_not_null_Test() {
        User user1 = new User(3, "user@mail.ru", "name20");
        Item item1 = new Item(2, "name1", "description20", true, 2, null);

        LocalDateTime start1 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        LocalDateTime end1 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(50);
        BookingDto bookingDto1 =
                new BookingDto(1, start1, end1, StatusBooking.WAITING, 3, 2, "name1");

        Booking booking1 = bookingMapper.toBooking(bookingDto1, user1, item1);

        assertEquals(bookingDto1.getId(), booking1.getId());
        assertEquals(bookingDto1.getStart(), booking1.getStart());
        assertEquals(bookingDto1.getEnd(), booking1.getEnd());
        assertEquals(bookingDto1.getStatus(), booking1.getStatus());
        assertEquals(bookingDto1.getBookerId(), booking1.getUser().getId());
        assertEquals(bookingDto1.getItemId(), booking1.getItem().getId());
        assertEquals(bookingDto1.getItemName(), booking1.getItem().getName());
    }

    @Test
    void toBooking_id_null_Test() {
        User user1 = new User(3, "user@mail.ru", "name20");
        Item item1 = new Item(2, "name1", "description20", true, 2, null);

        LocalDateTime start1 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        LocalDateTime end1 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(50);
        BookingDto bookingDto1 =
                new BookingDto(null, start1, end1, StatusBooking.WAITING, 3, 2, "name1");

        Booking booking1 = bookingMapper.toBooking(bookingDto1, user1, item1);

        assertEquals(0, booking1.getId());
        assertEquals(bookingDto1.getStart(), booking1.getStart());
        assertEquals(bookingDto1.getEnd(), booking1.getEnd());
        assertEquals(bookingDto1.getStatus(), booking1.getStatus());
        assertEquals(bookingDto1.getBookerId(), booking1.getUser().getId());
        assertEquals(bookingDto1.getItemId(), booking1.getItem().getId());
        assertEquals(bookingDto1.getItemName(), booking1.getItem().getName());
    }

    @Test
    void toBooking_status_null_Test() {
        User user1 = new User(3, "user@mail.ru", "name20");
        Item item1 = new Item(2, "name1", "description20", true, 2, null);

        LocalDateTime start1 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        LocalDateTime end1 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(50);
        BookingDto bookingDto1 =
                new BookingDto(null, start1, end1, null, 3, 2, "name1");

        Booking booking1 = bookingMapper.toBooking(bookingDto1, user1, item1);

        assertEquals(0, booking1.getId());
        assertEquals(bookingDto1.getStart(), booking1.getStart());
        assertEquals(bookingDto1.getEnd(), booking1.getEnd());
        assertEquals(bookingDto1.getStatus(), booking1.getStatus());
        assertEquals(bookingDto1.getBookerId(), booking1.getUser().getId());
        assertEquals(bookingDto1.getItemId(), booking1.getItem().getId());
        assertEquals(bookingDto1.getItemName(), booking1.getItem().getName());
    }

    @Test
    void toBookingRequestDtoTest() {
        User user1 = new User(3, "user@mail.ru", "name20");
        Item item1 = new Item(2, "name1", "description20", true, 2, null);

        LocalDateTime start1 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        LocalDateTime end1 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(50);
        BookingDto bookingDto1 =
                new BookingDto(1, start1, end1, StatusBooking.WAITING, 3, 2, "name1");

        Booking booking1 = bookingMapper.toBooking(bookingDto1, user1, item1);

        BookingResponseDto bookingResponseDto = bookingMapper.toBookingRequestDto(booking1);

        assertEquals(booking1.getId(), bookingResponseDto.getId());
        assertEquals(booking1.getStart(), bookingResponseDto.getStart());
        assertEquals(booking1.getEnd(), bookingResponseDto.getEnd());
        assertEquals(booking1.getStatus(), bookingResponseDto.getStatus());
        assertEquals(booking1.getUser().getId(), bookingResponseDto.getBooker().getId());
        assertEquals(booking1.getItem().getId(), bookingResponseDto.getItem().getId());
        assertEquals(booking1.getItem().getName(), bookingResponseDto.getItem().getName());
    }
}
