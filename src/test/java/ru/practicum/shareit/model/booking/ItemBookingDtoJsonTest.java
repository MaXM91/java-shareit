package ru.practicum.shareit.model.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.ItemBookingDto;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class ItemBookingDtoJsonTest {
    @Autowired
    JacksonTester<ItemBookingDto> json;

    @Test
    void itemBookingDtoJsonTest() throws Exception {
        ItemBookingDto booking = new ItemBookingDto(1, "name");

        JsonContent<ItemBookingDto> result = json.write(booking);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(booking.getId());
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(booking.getName());
    }
}