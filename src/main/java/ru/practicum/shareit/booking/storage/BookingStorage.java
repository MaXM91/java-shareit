package ru.practicum.shareit.booking.storage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.StatusBooking;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.BookingForItemDto;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingStorage extends JpaRepository<Booking, Integer> {
    Page<Booking> findAllByUserId(int userId, Pageable pageable);

    Page<Booking> findByUserIdAndEndBefore(int userId, LocalDateTime date, Pageable pageable);

    Page<Booking> findByUserIdAndStartAfter(int userId, LocalDateTime date, Pageable pageable);

    Page<Booking> findByUserIdAndStartBeforeAndEndAfter(int userId, LocalDateTime start, LocalDateTime end,
                                                        Pageable pageable);

    Page<Booking> findByUserIdAndStatus(int userId, StatusBooking status, Pageable pageable);

    Page<Booking> findAllByItemOwnerId(int userId, Pageable pageable);

    Page<Booking> findByItemOwnerIdAndEndBefore(int userId, LocalDateTime date, Pageable pageable);

    Page<Booking> findByItemOwnerIdAndStartAfter(int userId, LocalDateTime date, Pageable pageable);

    Page<Booking> findByItemOwnerIdAndStartBeforeAndEndAfter(int userId, LocalDateTime start,
                                                             LocalDateTime end, Pageable pageable);

    @Query(value = "SELECT * " +
            "FROM bookings AS b " +
            "LEFT OUTER JOIN items AS i ON b.item_id = i.id " +
            "LEFT OUTER JOIN users AS u ON b.booker_id = u.id " +
            "WHERE i.id = :id AND b.start_date >= :start AND b.start_date <= :end OR " +
            "      i.id = :id AND b.end_date >= :start AND b.end_date <= :end OR " +
            "      i.id = :id AND b.start_date >= :start AND b.end_date <= :end OR " +
            "      i.id = :id AND b.start_date <= :start AND b.end_date >= :end ",
            nativeQuery = true)
    List<Booking> checkFreeDateBooking(@Param("id") int itemId,
                                       @Param("start") LocalDateTime start,
                                       @Param("end") LocalDateTime end);

    Page<Booking> findByItemOwnerIdAndStatus(int userId, StatusBooking status, Pageable pageable);

    List<Booking> findByUserIdAndItemIdAndEndBeforeAndStatus(int userId, int itemId, LocalDateTime end,
                                                             StatusBooking statusBooking);

    @Query(nativeQuery = true, name = "BookingForItemByItemId")
    List<BookingForItemDto> findAllByItemIdAndStatus(int id, int status);

    @Query(nativeQuery = true, name = "BookingForItemByOwnerId")
    List<BookingForItemDto> findAllByItemOwnerIdAndStatus(int id, int status);
}