package ru.practicum.shareit.booking.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.ItemBookingDto;
import ru.practicum.shareit.booking.dto.UserBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.service.UserService;

@Component
public class BookingMapper {
    private UserService userService;
    private ItemService itemService;

    BookingMapper(@Qualifier("UserServiceImpl") UserService userService,
                  @Qualifier("ItemServiceImpl") ItemService itemService) {
        this.userService = userService;
        this.itemService = itemService;
    }

    @Autowired
    public void setUserService(@Qualifier("UserServiceImpl") UserService userService) {
        this.userService = userService;
    }

    public Booking toBooking(BookingDto bookingDto) {
        Booking booking = new Booking();

        if (bookingDto.getId() != null) {
            booking.setId(bookingDto.getId());
        }

        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());

        if (bookingDto.getStatus() != null) {
            booking.setStatus(bookingDto.getStatus());
        }

        booking.setUser(userService.getUserEntityById(bookingDto.getBookerId()));
        booking.setItem(itemService.getItemEntityById(bookingDto.getItemId()));

        return booking;
    }

    public BookingDto toBookingDto(Booking booking) {
        BookingDto bookingDto = new BookingDto();

        bookingDto.setId(booking.getId());
        bookingDto.setStart(booking.getStart());
        bookingDto.setEnd(booking.getEnd());
        bookingDto.setStatus(booking.getStatus());
        bookingDto.setBookerId(booking.getUser().getId());
        bookingDto.setItemId(booking.getItem().getId());
        bookingDto.setItemName(booking.getItem().getName());

        return bookingDto;
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
