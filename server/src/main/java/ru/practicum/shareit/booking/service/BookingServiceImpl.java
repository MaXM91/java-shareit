package ru.practicum.shareit.booking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.StateBooking;
import ru.practicum.shareit.booking.StatusBooking;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.validation.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.validation.exceptions.UnsupportedException;
import ru.practicum.shareit.validation.exceptions.ValidException;
import ru.practicum.shareit.validation.exceptions.ValidateException;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Qualifier("BookingServiceImpl")
@Service
public class BookingServiceImpl implements BookingService {
    private final BookingStorage bookingStorage;
    private final BookingMapper bookingMapper;
    private final UserService userService;
    private final ItemService itemService;

    @Autowired
    BookingServiceImpl(BookingStorage bookingStorage, BookingMapper bookingMapper,
                       @Qualifier("UserServiceImpl") UserService userService,
                       @Qualifier("ItemServiceImpl") ItemService itemService) {
        this.bookingStorage = bookingStorage;
        this.bookingMapper = bookingMapper;
        this.userService = userService;
        this.itemService = itemService;
    }

    @Override
    public BookingResponseDto addBooking(Integer userId, BookingDto bookingDto) {
        checkUser(userId);

        bookingDto.setBookerId(userId);
        Booking newBooking = bookingMapper.toBooking(bookingDto,
                userService.getUserEntityById(userId),
                itemService.getItemEntityById(bookingDto.getItemId()));

        if (newBooking.getItem().getOwnerId() == userId) {
            throw new ValidateException("you can't book your thing");
        }

        if (!newBooking.getItem().getAvailable()) {
            throw new ValidException("item available must be true");
        }

        if (bookingDto.getStart().isAfter(bookingDto.getEnd())) {
            throw new ValidException("start should not be after end");
        }

        if (bookingDto.getStart().isEqual(bookingDto.getEnd())) {
            throw new ValidException("start and end have one time");
        }

        if (!(bookingStorage.checkFreeDateBooking(newBooking.getItem().getId(),
                newBooking.getStart(), newBooking.getEnd()).isEmpty())) {
            throw new ValidateException("item is booked for this time");
        }

        newBooking.setStatus(StatusBooking.WAITING);

        return bookingMapper.toBookingRequestDto(bookingStorage.save(newBooking));
    }

    @Override
    public BookingResponseDto getBookingById(int userId, int bookingId) {
        checkUser(userId);

        Booking foundedBooking = getById(bookingId);

        if (foundedBooking.getItem().getOwnerId() != userId && foundedBooking.getUser().getId() != userId) {
            throw new ValidateException("booking may be seen only by owner item or owner booking");
        }

        return bookingMapper.toBookingRequestDto(foundedBooking);
    }

    @Override
    public List<BookingResponseDto> getBookingsByUserId(int userId, StateBooking state, Integer from, Integer size) {
        checkUser(userId);

        LocalDateTime date = LocalDateTime.now();
        StatusBooking status;

        switch (state) {
            case ALL:
                return response(bookingStorage.findAllByUserId(userId, PageRequest.of(from / size,
                        size, Sort.by(Sort.Direction.DESC, "end"))));
            case PAST:
                return response(bookingStorage.findByUserIdAndEndBefore(userId, date, PageRequest.of(from / size,
                        size, Sort.by(Sort.Direction.DESC, "end"))));
            case FUTURE:
                return response(bookingStorage.findByUserIdAndStartAfter(userId, date, PageRequest.of(from / size,
                        size, Sort.by(Sort.Direction.DESC, "end"))));
            case CURRENT:
                return response(bookingStorage.findByUserIdAndStartBeforeAndEndAfter(userId, date, date,
                        PageRequest.of(from / size, size, Sort.by(Sort.Direction.DESC, "end"))));
            case WAITING:
                status = StatusBooking.WAITING;
                return response(bookingStorage.findByUserIdAndStatus(userId, status, PageRequest.of(from / size,
                        size, Sort.by(Sort.Direction.DESC, "end"))));
            case REJECTED:
                status = StatusBooking.REJECTED;
                return response(bookingStorage.findByUserIdAndStatus(userId, status, PageRequest.of(from / size,
                        size, Sort.by(Sort.Direction.DESC, "end"))));
            default:
                throw new UnsupportedException("Unknown state: UNSUPPORTED_STATUS");
        }
    }

    @Override
    public List<BookingResponseDto> getBookingsByOwnerItem(int userId, StateBooking state, Integer from, Integer size) {
        checkUser(userId);

        LocalDateTime date = LocalDateTime.now();
        StatusBooking status;

        switch (state) {
            case ALL:
                return response(bookingStorage.findAllByItemOwnerId(userId, PageRequest.of(from / size,
                        size, Sort.by(Sort.Direction.DESC, "end"))));
            case PAST:
                return response(bookingStorage.findByItemOwnerIdAndEndBefore(userId, date, PageRequest.of(from / size,
                        size, Sort.by(Sort.Direction.DESC, "end"))));
            case FUTURE:
                return response(bookingStorage.findByItemOwnerIdAndStartAfter(userId, date, PageRequest.of(from / size,
                        size, Sort.by(Sort.Direction.DESC, "end"))));
            case CURRENT:
                return response(bookingStorage.findByItemOwnerIdAndStartBeforeAndEndAfter(userId, date, date,
                        PageRequest.of(from / size, size, Sort.by(Sort.Direction.DESC, "end"))));
            case WAITING:
                status = StatusBooking.WAITING;
                return response(bookingStorage.findByItemOwnerIdAndStatus(userId, status, PageRequest.of(from / size,
                        size, Sort.by(Sort.Direction.DESC, "end"))));
            case REJECTED:
                status = StatusBooking.REJECTED;
                return response(bookingStorage.findByItemOwnerIdAndStatus(userId, status, PageRequest.of(from / size,
                        size, Sort.by(Sort.Direction.DESC, "end"))));
            default:                throw new UnsupportedException("Unknown state: UNSUPPORTED_STATUS");
        }
    }

    public void checkBookingByUserIdAndItemId(int userId, int itemId) {
        List<Booking> foundedBooking = bookingStorage.findByUserIdAndItemIdAndEndBeforeAndStatus(userId,
                itemId, LocalDateTime.now(), StatusBooking.APPROVED);

        if (foundedBooking.isEmpty()) {
            throw new ValidException("booking by user - " + userId + " on item - " + itemId + " not found");
        }
    }

    @Override
    public BookingResponseDto updateStatusBooking(int userId, int bookingId, boolean approved) {
        checkUser(userId);

        Booking foundedBooking = getById(bookingId);

        if (foundedBooking.getStatus() == StatusBooking.APPROVED) {
            throw new ValidException("the status has already been approved");
        }

        if (foundedBooking.getStatus() == StatusBooking.REJECTED) {
            throw new ValidException("the status has been rejected");
        }

        if (foundedBooking.getItem().getOwnerId() != userId) {
            throw new ValidateException("user - " + userId + " is not owner item - " + foundedBooking.getItem().getId());
        }

        if (approved) {
            foundedBooking.setStatus(StatusBooking.APPROVED);
        } else {
            foundedBooking.setStatus(StatusBooking.REJECTED);
        }

        return bookingMapper.toBookingRequestDto(save(foundedBooking));
    }

    private void checkUser(Integer userId) {
        UserDto foundUser = userService.getUserById(userId);

        if (foundUser.getEmail() == null) {
            throw new ObjectNotFoundException("user id - " + userId + " not found");
        }
    }

    private Booking save(Booking booking) {
        return bookingStorage.save(booking);
    }

    private Booking getById(int bookingId) {
        return bookingStorage.findById(bookingId)
                .orElseThrow(() -> new ObjectNotFoundException("booking id - " + bookingId + " not found"));
    }

    private List<BookingResponseDto> response(Page<Booking> bookings) {
            return bookings.stream()
                    .map(bookingMapper::toBookingRequestDto)
                    .collect(Collectors.toList());
    }
}