package ru.practicum.shareit.model.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.UserBookingDto;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class UserBookingDtoJsonTest {

    @Autowired
    JacksonTester<UserBookingDto> json;

    @Test
    void userBookingDtoJsonTest() throws Exception {
        UserBookingDto booking = new UserBookingDto(1);

        JsonContent<UserBookingDto> result = json.write(booking);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(booking.getId());
    }
}