package ru.practicum.shareit.booking.storage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query(value = "SELECT * " +
            "FROM bookings AS b " +
            "LEFT OUTER JOIN items AS i ON b.item_id = i.id " +
            "LEFT OUTER JOIN users AS u ON b.booker_id = u.id " +
            "WHERE i.id = :id AND CAST(b.start_date AS date) >= :start AND CAST(b.start_date AS date) <= :end OR" +
            "     i.id = :id AND CAST(b.end_date AS date) >= :start AND CAST(b.end_date AS date) <= :end",
            nativeQuery = true)
    List<Booking> checkFreeDateBooking(@Param("id") int itemId,
                                       @Param("start") LocalDateTime start,
                                       @Param("end") LocalDateTime end);

    List<Booking> findByItemOwnerIdAndStatus(int userId, StatusBooking status);

    List<Booking> findByUserIdAndItemIdAndEndBeforeAndStatus(int userId, int itemId, LocalDateTime end,
                                                             StatusBooking statusBooking);

    @Query(nativeQuery = true, name = "BookingForItemByItemId")
    List<BookingForItemDto> findAllByItemIdAndStatus(int id, int status);

    @Query(nativeQuery = true, name = "BookingForItemByOwnerId")
    List<BookingForItemDto> findAllByItemOwnerIdAndStatus(int id, int status);

    Page<BookingForItemDto> findAllBy(Pageable pageable);
}