package ru.practicum.shareit.storage;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import ru.practicum.shareit.booking.StatusBooking;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.item.dto.BookingForItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class BookingStorageTest {
    @Autowired
    BookingStorage bookingStorage;

    @Autowired
    UserStorage userStorage;

    @Autowired
    ItemStorage itemStorage;

    @Test
    void addBookingTest() {
        User user1 = new User(0,"email1@mail.ru","name1");
        User actualUser1 = userStorage.save(user1);

        Item item1 =
                new Item(0, "name1", "description1", true, actualUser1.getId(), null);
        Item actualItem1 = itemStorage.save(item1);

        LocalDateTime start1 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(50);
        LocalDateTime end1 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(100);
        Booking booking1 = new Booking(0, start1, end1, StatusBooking.WAITING, actualItem1, actualUser1);
        Booking actualBooking = bookingStorage.save(booking1);

        Assertions.assertNotNull(actualBooking);

        assertThat(booking1)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(actualBooking);
    }

    @Test
    void findAllByUserIdTest() {
        User user2 = new User(0,"email2@mail.ru","name2");
        User actualUser2 = userStorage.save(user2);

        Item item1 =
                new Item(0, "name2", "description2", true, actualUser2.getId(), null);
        Item actualItem2 = itemStorage.save(item1);

        LocalDateTime start2 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(50);
        LocalDateTime end2 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(100);
        Booking booking2 = new Booking(0, start2, end2, StatusBooking.WAITING, actualItem2, actualUser2);
        Booking bookingEnd = bookingStorage.save(booking2);

        List<Booking> actualBooking = bookingStorage.findAllByUserId(actualUser2.getId());

        Assertions.assertNotNull(actualBooking);

        assertThat(bookingEnd)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(actualBooking.get(0));
    }

    @Test
    void findByUserIdAndEndBeforeTest() {
        User user3 = new User(0,"email3@mail.ru","name3");
        User actualUser3 = userStorage.save(user3);

        Item item3 =
                new Item(0, "name3", "description3", true, actualUser3.getId(), null);
        Item actualItem3 = itemStorage.save(item3);

        LocalDateTime start3 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).minusMinutes(50);
        LocalDateTime end3 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).minusMinutes(40);
        Booking booking3 = new Booking(0, start3, end3, StatusBooking.WAITING, actualItem3, actualUser3);
        Booking bookingEnd = bookingStorage.save(booking3);

        List<Booking> actualBooking = bookingStorage.findByUserIdAndEndBefore(actualUser3.getId(),LocalDateTime.now());

        Assertions.assertNotNull(actualBooking);

        assertThat(bookingEnd)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(actualBooking.get(0));
    }

    @Test
    void findByUserIdAndStartAfterTest() {
        User user4 = new User(0,"email4@mail.ru","name4");
        User actualUser4 = userStorage.save(user4);

        Item item4 =
                new Item(0, "name4", "description4", true, actualUser4.getId(), null);
        Item actualItem4 = itemStorage.save(item4);

        LocalDateTime start4 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(50);
        LocalDateTime end4 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(40);
        Booking booking4 = new Booking(0, start4, end4, StatusBooking.WAITING, actualItem4, actualUser4);
        Booking bookingEnd = bookingStorage.save(booking4);

        List<Booking> actualBooking = bookingStorage.findByUserIdAndStartAfter(actualUser4.getId(),LocalDateTime.now());

        Assertions.assertNotNull(actualBooking);

        assertThat(bookingEnd)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(actualBooking.get(0));
    }

    @Test
    void findByUserIdAndStartBeforeAndEndAfterTest() {
        User user5 = new User(0,"email5@mail.ru","name5");
        User actualUser5 = userStorage.save(user5);

        Item item5 =
                new Item(0, "name5", "description5", true, actualUser5.getId(), null);
        Item actualItem5 = itemStorage.save(item5);

        LocalDateTime start5 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).minusMinutes(150);
        LocalDateTime end5 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(140);
        Booking booking5 = new Booking(0, start5, end5, StatusBooking.WAITING, actualItem5, actualUser5);
        Booking bookingEnd = bookingStorage.save(booking5);

        List<Booking> actualBooking =
                bookingStorage.findByUserIdAndStartBeforeAndEndAfter(actualUser5.getId(),
                                                                     LocalDateTime.now().minusMinutes(100),
                                                                     LocalDateTime.now().plusMinutes(100));

        Assertions.assertNotNull(actualBooking);

        assertThat(bookingEnd)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(actualBooking.get(0));
    }

    @Test
    void findByUserIdAndStatusTest() {
        User user6 = new User(0,"email6@mail.ru","name6");
        User actualUser6 = userStorage.save(user6);

        Item item6 =
                new Item(0, "name6", "description6", true, actualUser6.getId(), null);
        Item actualItem6 = itemStorage.save(item6);

        LocalDateTime start6 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).minusMinutes(150);
        LocalDateTime end6 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(140);
        Booking booking6 = new Booking(0, start6, end6, StatusBooking.WAITING, actualItem6, actualUser6);
        Booking bookingEnd = bookingStorage.save(booking6);

        List<Booking> actualBooking =
                bookingStorage.findByUserIdAndStatus(actualUser6.getId(), StatusBooking.WAITING);

        Assertions.assertNotNull(actualBooking);

        assertThat(bookingEnd)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(actualBooking.get(0));
    }

    @Test
    void findAllByItemOwnerIdTest() {
        User user7 = new User(0,"email7@mail.ru","name7");
        User actualUser7 = userStorage.save(user7);

        Item item7 =
                new Item(0, "name7", "description7", true, actualUser7.getId(), null);
        Item actualItem7 = itemStorage.save(item7);

        LocalDateTime start7 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).minusMinutes(150);
        LocalDateTime end7 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(140);
        Booking booking7 = new Booking(0, start7, end7, StatusBooking.WAITING, actualItem7, actualUser7);
        Booking bookingEnd = bookingStorage.save(booking7);

        List<Booking> actualBooking =
                bookingStorage.findAllByItemOwnerId(item7.getOwnerId());

        Assertions.assertNotNull(actualBooking);

        assertThat(bookingEnd)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(actualBooking.get(0));
    }

    @Test
    void findByItemOwnerIdAndStartAfterTest() {
        User user8 = new User(0,"email8@mail.ru","name8");
        User actualUser8 = userStorage.save(user8);

        Item item8 =
                new Item(0, "name8", "description8", true, actualUser8.getId(), null);
        Item actualItem8 = itemStorage.save(item8);

        LocalDateTime start8 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(150);
        LocalDateTime end8 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(200);
        Booking booking8 = new Booking(0, start8, end8, StatusBooking.WAITING, actualItem8, actualUser8);
        Booking bookingEnd = bookingStorage.save(booking8);

        List<Booking> actualBooking =
                bookingStorage.findByItemOwnerIdAndStartAfter(item8.getOwnerId(), LocalDateTime.now().plusMinutes(100));

        Assertions.assertNotNull(actualBooking);

        assertThat(bookingEnd)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(actualBooking.get(0));
    }

    @Test
    void findByItemOwnerIdAndEndBeforeTest() {
        User user9 = new User(0,"email9@mail.ru","name9");
        User actualUser9 = userStorage.save(user9);

        Item item9 =
                new Item(0, "name9", "description9",
                        true, actualUser9.getId(), null);
        Item actualItem9 = itemStorage.save(item9);

        LocalDateTime start9 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).minusMinutes(150);
        LocalDateTime end9 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(90);
        Booking booking9 = new Booking(0, start9, end9, StatusBooking.WAITING, actualItem9, actualUser9);
        Booking bookingEnd = bookingStorage.save(booking9);

        List<Booking> actualBooking =
                bookingStorage.findByItemOwnerIdAndEndBefore(item9.getOwnerId(), LocalDateTime.now().plusMinutes(100));

        Assertions.assertNotNull(actualBooking);

        assertThat(bookingEnd)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(actualBooking.get(0));
    }

    @Test
    void findByItemOwnerIdAndStartBeforeAndEndAfterTest() {
        User user10 = new User(0,"email10@mail.ru","name10");
        User actualUser10 = userStorage.save(user10);

        Item item10 =
                new Item(0, "name10", "description10",
                        true, actualUser10.getId(), null);
        Item actualItem10 = itemStorage.save(item10);

        LocalDateTime start10 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).minusMinutes(150);
        LocalDateTime end10 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(110);
        Booking booking10 = new Booking(0, start10, end10, StatusBooking.WAITING, actualItem10, actualUser10);
        Booking bookingEnd = bookingStorage.save(booking10);

        List<Booking> actualBooking =
                bookingStorage.findByItemOwnerIdAndStartBeforeAndEndAfter(item10.getOwnerId(),
                        LocalDateTime.now().plusMinutes(100),
                        LocalDateTime.now().plusMinutes(100));

        Assertions.assertNotNull(actualBooking);

        assertThat(bookingEnd)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(actualBooking.get(0));
    }

 /*   @Test
    void checkFreeDateBookingTest() {
        User user11 = new User(0,"email11@mail.ru","name11");
        User actualUser11 = userStorage.save(user11);

        Item item11 =
                new Item(0, "name11", "description11", true, actualUser11.getId(), null);
        Item actualItem11 = itemStorage.save(item11);

        LocalDateTime start11 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(70);
        LocalDateTime end11 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(80);
        Booking booking11 = new Booking(0, start11, end11, StatusBooking.WAITING, actualItem11, actualUser11);
        Booking bookingEnd = bookingStorage.save(booking11);

        List<Booking> actualBooking =
                bookingStorage.checkFreeDateBooking(item11.getOwnerId(),
                        LocalDateTime.now().plusMinutes(50),
                        LocalDateTime.now().plusMinutes(100));

        Assertions.assertNotNull(actualBooking);

        assertThat(bookingEnd)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(actualBooking.get(0));
    }*/

    @Test
    void findByItemOwnerIdAndStatusTest() {
        User user12 = new User(0,"email12@mail.ru","name12");
        User actualUser12 = userStorage.save(user12);

        Item item12 =
                new Item(0, "name12", "description12",
                        true, actualUser12.getId(), null);
        Item actualItem12 = itemStorage.save(item12);

        LocalDateTime start12 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).minusMinutes(150);
        LocalDateTime end12 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(110);
        Booking booking12 = new Booking(0, start12, end12, StatusBooking.WAITING, actualItem12, actualUser12);
        Booking bookingEnd = bookingStorage.save(booking12);

        List<Booking> actualBooking =
                bookingStorage.findByItemOwnerIdAndStatus(item12.getOwnerId(),StatusBooking.WAITING);

        Assertions.assertNotNull(actualBooking);

        assertThat(bookingEnd)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(actualBooking.get(0));
    }

    @Test
    void findByUserIdAndItemIdAndEndBeforeAndStatusTest() {
        User user13 = new User(0,"email12@mail.ru","name12");
        User actualUser13 = userStorage.save(user13);

        Item item13 =
                new Item(0, "name12", "description12",
                        true, actualUser13.getId(), null);
        Item actualItem13 = itemStorage.save(item13);

        LocalDateTime start13 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).minusMinutes(150);
        LocalDateTime end13 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(80);
        Booking booking13 = new Booking(0, start13, end13, StatusBooking.WAITING, actualItem13, actualUser13);
        Booking bookingEnd = bookingStorage.save(booking13);

        List<Booking> actualBooking =
                bookingStorage.findByUserIdAndItemIdAndEndBeforeAndStatus(actualUser13.getId(),
                        actualItem13.getId(),
                        LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(110),
                        StatusBooking.WAITING);

        Assertions.assertNotNull(actualBooking);

        assertThat(bookingEnd)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(actualBooking.get(0));
    }

    @Test
    void findAllByItemIdAndStatusTest() {
        User user14 = new User(0,"email14@mail.ru","name14");
        User actualUser14 = userStorage.save(user14);

        Item item14 =
                new Item(0, "name14",
                        "description14", true, actualUser14.getId(), null);
        Item actualItem14 = itemStorage.save(item14);

        LocalDateTime start14 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).minusMinutes(150);
        LocalDateTime end14 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(80);
        Booking booking14 = new Booking(0, start14, end14, StatusBooking.WAITING, actualItem14, actualUser14);
        Booking bookingEnd = bookingStorage.save(booking14);

        BookingForItemDto bookingForItemDto =
                new BookingForItemDto(1,actualUser14.getId(), start14, end14, item14.getId());

        List<BookingForItemDto> actualBooking =
                bookingStorage.findAllByItemIdAndStatus(actualUser14.getId(),
                        StatusBooking.WAITING.ordinal());

        Assertions.assertNotNull(actualBooking);

        assertThat(bookingForItemDto)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(actualBooking.get(0));
    }

    @Test
    void findAllByItemOwnerIdAndStatusTest() {
        User user15 = new User(0,"email15@mail.ru","name15");
        User actualUser15 = userStorage.save(user15);

        Item item15 =
                new Item(0, "name15", "description15",
                        true, actualUser15.getId(), null);
        Item actualItem15 = itemStorage.save(item15);

        LocalDateTime start15 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).minusMinutes(150);
        LocalDateTime end15 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(80);
        Booking booking15 = new Booking(0, start15, end15, StatusBooking.WAITING, actualItem15, actualUser15);
        Booking bookingEnd = bookingStorage.save(booking15);

        BookingForItemDto bookingForItemDto =
                new BookingForItemDto(1, actualUser15.getId(), start15, end15, item15.getId());

        List<BookingForItemDto> actualBooking =
                bookingStorage.findAllByItemOwnerIdAndStatus(actualItem15.getOwnerId(),
                        StatusBooking.WAITING.ordinal());

        Assertions.assertNotNull(actualBooking);

        assertThat(bookingForItemDto)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(actualBooking.get(0));
    }
}
