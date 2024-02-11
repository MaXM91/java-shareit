package ru.practicum.shareit.model.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.StatusBooking;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class BookingJsonTest {
    @Autowired
    JacksonTester<Booking> json;

    @Test
    void bookingJsonTest() throws Exception {
        LocalDateTime start = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        LocalDateTime end = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        User user = new User(1, "user@mail.ru", "name1");
        Item item = new Item(1, "name1", "description1", true, 1, null);

        Booking booking = new Booking(1, start, end, StatusBooking.WAITING, item, user);

        JsonContent<Booking> result = json.write(booking);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(booking.getId());
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo(booking.getStart().toString());
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo(booking.getEnd().toString());
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo(booking.getStatus().toString());
        assertThat(result).extractingJsonPathNumberValue("$.item.id").isEqualTo(item.getId());
        assertThat(result).extractingJsonPathStringValue("$.item.name").isEqualTo(item.getName());
        assertThat(result).extractingJsonPathStringValue("$.item.description").isEqualTo(item.getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.item.available").isEqualTo(item.getAvailable());
        assertThat(result).extractingJsonPathNumberValue("$.item.ownerId").isEqualTo(item.getOwnerId());
        assertThat(result).extractingJsonPathValue("$.item.request").isEqualTo(item.getRequest());
        assertThat(result).extractingJsonPathNumberValue("$.user.id").isEqualTo(user.getId());
        assertThat(result).extractingJsonPathStringValue("$.user.name").isEqualTo(user.getName());
        assertThat(result).extractingJsonPathStringValue("$.user.email").isEqualTo(user.getEmail());
    }
}
