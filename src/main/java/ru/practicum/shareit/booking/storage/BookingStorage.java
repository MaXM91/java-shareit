package ru.practicum.shareit.booking.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.StatusBooking;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.BookingForItemDto;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingStorage extends JpaRepository<Booking, Integer> {
    List<Booking> findAllByUserId(int userId);

    List<Booking> findByUserIdAndEndBefore(int userId, LocalDateTime date);

    List<Booking> findByUserIdAndStartAfter(int userId, LocalDateTime date);

    List<Booking> findByUserIdAndStartBeforeAndEndAfter(int userId, LocalDateTime start, LocalDateTime end);

    List<Booking> findByUserIdAndStatus(int userId, StatusBooking status);

    List<Booking> findAllByItemOwnerId(int userId);

    List<Booking> findByItemOwnerIdAndEndBefore(int userId, LocalDateTime date);

    List<Booking> findByItemOwnerIdAndStartAfter(int userId, LocalDateTime date);

    List<Booking> findByItemOwnerIdAndStartBeforeAndEndAfter(int userId, LocalDateTime start, LocalDateTime end);

    List<Booking> findByItemOwnerIdAndStatus(int userId, StatusBooking status);

    List<Booking> findByUserIdAndItemIdAndEndBeforeAndStatus(int userId, int itemId, LocalDateTime end,
                                                             StatusBooking statusBooking);

    @Query(nativeQuery = true, name = "BookingForItem")
    List<BookingForItemDto> findAllByItemIdAndStatus(int id, int status);
}