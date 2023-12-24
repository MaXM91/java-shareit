package ru.practicum.shareit.model.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.StatusBooking;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.ItemBookingDto;
import ru.practicum.shareit.booking.dto.UserBookingDto;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class BookingRequestDtoJsonTest {
    @Autowired
    JacksonTester<BookingRequestDto> json;

    @Test
    void bookingRequestDtoJsonTest() throws Exception {
        LocalDateTime start = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        LocalDateTime end = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        UserBookingDto user = new UserBookingDto(1);
        ItemBookingDto item = new ItemBookingDto(1, "itemName");

        BookingRequestDto bookingRequestDto = new BookingRequestDto(1, start, end, StatusBooking.WAITING, user, item);

        JsonContent<BookingRequestDto> result = json.write(bookingRequestDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(bookingRequestDto.getId());
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo(bookingRequestDto.getStart().toString());
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo(bookingRequestDto.getEnd().toString());
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo(bookingRequestDto.getStatus().toString());
        assertThat(result).extractingJsonPathNumberValue("$.booker.id").isEqualTo(bookingRequestDto.getBooker().getId());
        assertThat(result).extractingJsonPathNumberValue("$.item.id").isEqualTo(bookingRequestDto.getItem().getId());
        assertThat(result).extractingJsonPathStringValue("$.item.name").isEqualTo(bookingRequestDto.getItem().getName());
    }
}
