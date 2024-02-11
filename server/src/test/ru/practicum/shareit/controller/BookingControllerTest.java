package ru.practicum.shareit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.StateBooking;
import ru.practicum.shareit.booking.StatusBooking;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.validation.exceptions.UnsupportedException;
import ru.practicum.shareit.validation.exceptions.ValidException;
import ru.practicum.shareit.validation.exceptions.ValidateException;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
public class BookingControllerTest {
    private final LocalDateTime start1 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(50);
    private final LocalDateTime end1 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(100);
    private final BookingDto bookingDtoBefore1 =
            new BookingDto(null, start1, end1, StatusBooking.WAITING, 2, 3, "name1");
    private final BookingResponseDto bookingDtoAfter1 =
            new BookingResponseDto(1, start1, end1, StatusBooking.WAITING, null, null);
    @Autowired
    ObjectMapper mapper;
    @MockBean
    BookingServiceImpl bookingService;
    @Autowired
    MockMvc mock;

    @Test
    void addBooking_200_Test() throws Exception {
        when(bookingService.addBooking(anyInt(), any()))
                .thenReturn(bookingDtoAfter1);

        mock.perform(post("/bookings")
                        .header("X-Sharer-User-Id", "1")
                        .content(mapper.writeValueAsString(bookingDtoBefore1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingDtoAfter1.getId()))
                .andExpect(jsonPath("$.start").value(bookingDtoAfter1.getStart().toString()))
                .andExpect(jsonPath("$.end").value(bookingDtoAfter1.getEnd().toString()))
                .andExpect(jsonPath("$.booker").value(bookingDtoAfter1.getBooker()))
                .andExpect(jsonPath("$.item").value(bookingDtoAfter1.getItem()));

        Mockito.verify(bookingService, Mockito.times(1)).addBooking(1, bookingDtoBefore1);
        Mockito.verifyNoMoreInteractions(bookingService);
    }

    @Test
    void addBooking_bad_available_400_Test() throws Exception {
        when(bookingService.addBooking(anyInt(), any()))
                .thenThrow(new ValidException("item available must be true"));

        mock.perform(post("/bookings")
                        .header("X-Sharer-User-Id", "1")
                        .content(mapper.writeValueAsString(bookingDtoBefore1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());

        Mockito.verify(bookingService, Mockito.times(1)).addBooking(1, bookingDtoBefore1);
        Mockito.verifyNoMoreInteractions(bookingService);
    }

    @Test
    void getBookingById_200_Test() throws Exception {
        when(bookingService.getBookingById(anyInt(), anyInt()))
                .thenReturn(bookingDtoAfter1);

        mock.perform(get("/bookings/{bookingId}", "1")
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingDtoAfter1.getId()))
                .andExpect(jsonPath("$.start").value(bookingDtoAfter1.getStart().toString()))
                .andExpect(jsonPath("$.end").value(bookingDtoAfter1.getEnd().toString()))
                .andExpect(jsonPath("$.booker").value(bookingDtoAfter1.getBooker()))
                .andExpect(jsonPath("$.item").value(bookingDtoAfter1.getItem()));

        Mockito.verify(bookingService, Mockito.times(1)).getBookingById(1, 1);
        Mockito.verifyNoMoreInteractions(bookingService);
    }

    @Test
    void getBookingById_bad_seen_404_Test() throws Exception {
        when(bookingService.getBookingById(anyInt(), anyInt()))
                .thenThrow(new ValidateException("booking may be seen only by owner item or owner booking"));

        mock.perform(get("/bookings/{bookingId}", "1")
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

        Mockito.verify(bookingService, Mockito.times(1)).getBookingById(1, 1);
        Mockito.verifyNoMoreInteractions(bookingService);
    }

    @Test
    void getBookingsByUserIdTest() throws Exception {
        when(bookingService.getBookingsByUserId(anyInt(), any(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingDtoAfter1));

        mock.perform(get("/bookings")
                        .header("X-Sharer-User-Id", "1")
                        .param("state", String.valueOf(StateBooking.WAITING))
                        .param("from", "0")
                        .param("size", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(bookingDtoAfter1.getId()))
                .andExpect(jsonPath("$[0].start").value(bookingDtoAfter1.getStart().toString()))
                .andExpect(jsonPath("$[0].end").value(bookingDtoAfter1.getEnd().toString()))
                .andExpect(jsonPath("$[0].booker").value(bookingDtoAfter1.getBooker()))
                .andExpect(jsonPath("$[0].item").value(bookingDtoAfter1.getItem()));

        Mockito.verify(bookingService,
                Mockito.times(1)).getBookingsByUserId(1, StateBooking.WAITING, 0, 1);
        Mockito.verifyNoMoreInteractions(bookingService);
    }

    @Test
    void getBookingsByOwnerItem_200_Test() throws Exception {
        when(bookingService.getBookingsByOwnerItem(anyInt(), any(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingDtoAfter1));

        mock.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", "1")
                        .param("state", String.valueOf(StateBooking.WAITING))
                        .param("from", "0")
                        .param("size", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(bookingDtoAfter1.getId()))
                .andExpect(jsonPath("$[0].start").value(bookingDtoAfter1.getStart().toString()))
                .andExpect(jsonPath("$[0].end").value(bookingDtoAfter1.getEnd().toString()))
                .andExpect(jsonPath("$[0].booker").value(bookingDtoAfter1.getBooker()))
                .andExpect(jsonPath("$[0].item").value(bookingDtoAfter1.getItem()));

        Mockito.verify(bookingService,
                Mockito.times(1)).getBookingsByOwnerItem(1, StateBooking.WAITING, 0, 1);
        Mockito.verifyNoMoreInteractions(bookingService);
    }

    @Test
    void getBookingsByOwnerItem_unsupported_status_500_Test() throws Exception {
        when(bookingService.getBookingsByOwnerItem(anyInt(), any(), anyInt(), anyInt()))
                .thenThrow(new UnsupportedException("Unknown state: UNSUPPORTED_STATUS"));

        mock.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", "1")
                        .param("state", String.valueOf(StateBooking.WAITING))
                        .param("from", "0")
                        .param("size", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isInternalServerError());

        Mockito.verify(bookingService,
                Mockito.times(1)).getBookingsByOwnerItem(1, StateBooking.WAITING, 0, 1);
        Mockito.verifyNoMoreInteractions(bookingService);
    }

    @Test
    void changeStatusBookingTest() throws Exception {
        when(bookingService.updateStatusBooking(anyInt(), anyInt(), anyBoolean()))
                .thenReturn(bookingDtoAfter1);

        mock.perform(patch("/bookings/{bookingId}", 1)
                        .header("X-Sharer-User-Id", "1")
                        .param("approved", String.valueOf(true))
                        .content(mapper.writeValueAsString(bookingDtoBefore1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingDtoAfter1.getId()))
                .andExpect(jsonPath("$.start").value(bookingDtoAfter1.getStart().toString()))
                .andExpect(jsonPath("$.end").value(bookingDtoAfter1.getEnd().toString()))
                .andExpect(jsonPath("$.booker").value(bookingDtoAfter1.getBooker()))
                .andExpect(jsonPath("$.item").value(bookingDtoAfter1.getItem()));

        Mockito.verify(bookingService,
                Mockito.times(1)).updateStatusBooking(1, 1, true);
        Mockito.verifyNoMoreInteractions(bookingService);
    }
}