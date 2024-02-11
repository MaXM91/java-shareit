package ru.practicum.shareit.model.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.ItemBookingDto;
import ru.practicum.shareit.booking.dto.UserBookingDto;
import ru.practicum.shareit.booking.enums.StatusBooking;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class BookingResponseDtoJsonTest {
    @Autowired
    JacksonTester<BookingResponseDto> json;

    @Test
    void bookingRequestDtoJsonTest() throws Exception {
        LocalDateTime start = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        LocalDateTime end = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        UserBookingDto user = new UserBookingDto(1);
        ItemBookingDto item = new ItemBookingDto(1, "itemName");

        BookingResponseDto bookingResponseDto = new BookingResponseDto(1, start, end, StatusBooking.WAITING, user, item);

        JsonContent<BookingResponseDto> result = json.write(bookingResponseDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(bookingResponseDto.getId());
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo(bookingResponseDto.getStart().toString());
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo(bookingResponseDto.getEnd().toString());
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo(bookingResponseDto.getStatus().toString());
        assertThat(result).extractingJsonPathNumberValue("$.booker.id").isEqualTo(bookingResponseDto.getBooker().getId());
        assertThat(result).extractingJsonPathNumberValue("$.item.id").isEqualTo(bookingResponseDto.getItem().getId());
        assertThat(result).extractingJsonPathStringValue("$.item.name").isEqualTo(bookingResponseDto.getItem().getName());
    }
}
