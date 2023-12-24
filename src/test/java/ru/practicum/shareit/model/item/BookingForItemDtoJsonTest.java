package ru.practicum.shareit.model.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.BookingForItemDto;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class BookingForItemDtoJsonTest {
    @Autowired
    JacksonTester<BookingForItemDto> json;

    @Test
    void commentDtoJsonTest() throws Exception {
        LocalDateTime start = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(50);
        LocalDateTime end = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(100);
        BookingForItemDto commentDto1 = new BookingForItemDto(1, 2, start, end, 3);
        BookingForItemDto commentDto2 = new BookingForItemDto(1, 2, start, end, 3);

        JsonContent<BookingForItemDto> result = json.write(commentDto1);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(commentDto1.getId());
        assertThat(result).extractingJsonPathNumberValue("$.bookerId").isEqualTo(commentDto1.getBookerId());
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo(commentDto1.getStart().toString());
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo(commentDto1.getEnd().toString());
        assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(commentDto1.getItemId());

        Assertions.assertEquals(commentDto1.toString(), commentDto2.toString());
    }
}