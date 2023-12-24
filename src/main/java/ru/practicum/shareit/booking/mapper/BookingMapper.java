package ru.practicum.shareit.booking.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.ItemBookingDto;
import ru.practicum.shareit.booking.dto.UserBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@Component
public class BookingMapper {

    public Booking toBooking(BookingDto bookingDto, User user, Item item) {
        Booking booking = new Booking();

        if (bookingDto.getId() != null) {
            booking.setId(bookingDto.getId());
        }

        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());

        if (bookingDto.getStatus() != null) {
            booking.setStatus(bookingDto.getStatus());
        }

        booking.setUser(user);
        booking.setItem(item);

        return booking;
    }

    public BookingRequestDto toBookingRequestDto(Booking booking) {
        BookingRequestDto bookingRequestDto = new BookingRequestDto();

        bookingRequestDto.setId(booking.getId());
        bookingRequestDto.setStart(booking.getStart());
        bookingRequestDto.setEnd(booking.getEnd());
        bookingRequestDto.setStatus(booking.getStatus());
        bookingRequestDto.setBooker(new UserBookingDto(booking.getUser().getId()));
        bookingRequestDto.setItem(new ItemBookingDto(booking.getItem().getId(), booking.getItem().getName()));

        return bookingRequestDto;
    }
}
