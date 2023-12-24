package ru.practicum.shareit.booking.model;

import lombok.*;
import ru.practicum.shareit.booking.StatusBooking;

import ru.practicum.shareit.item.dto.BookingForItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Booking entity.
 */

@Entity
@Table(name = "bookings", schema = "public")
@Builder
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@NamedNativeQuery(name = "BookingForItemByOwnerId",
        query = "SELECT b.id AS id, b.booker_id AS bookerId, b.start_date AS start, b.end_date AS \"end\", b.item_id AS itemId " +
                "FROM bookings AS b " +
                "LEFT OUTER JOIN items AS i ON b.item_id = i.id " +
                "WHERE i.owner_id = ?1 AND b.status = ?2 " +
                "ORDER BY start DESC ",
        resultSetMapping = "MyBookingForItemDto")

@NamedNativeQuery(name = "BookingForItemByItemId",
        query = "SELECT b.id AS id, b.booker_id AS bookerId, b.start_date AS start, b.end_date AS \"end\", b.item_id AS itemId " +
                "FROM bookings AS b " +
                "LEFT OUTER JOIN items AS i ON b.item_id = i.id " +
                "WHERE i.id = ?1 AND b.status = ?2 " +
                "ORDER BY start DESC ",
        resultSetMapping = "MyBookingForItemDto")

@SqlResultSetMapping(
        name = "MyBookingForItemDto",
        classes = { @ConstructorResult(
                columns = {
                        @ColumnResult(name = "id", type = Integer.class),
                        @ColumnResult(name = "bookerId", type = Integer.class),
                        @ColumnResult(name = "start", type = LocalDateTime.class),
                        @ColumnResult(name = "end", type = LocalDateTime.class),
                        @ColumnResult(name = "itemId", type = Integer.class)
                },
                targetClass = BookingForItemDto.class
        )}
)

public class Booking {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "start_date")
    @NotNull
    private LocalDateTime start;

    @Column(name = "end_date")
    @NotNull
    private LocalDateTime end;

    @Column(name = "status")
    @Enumerated(EnumType.ORDINAL)
    private StatusBooking status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booker_id")
    private User user;

    @Override
    public String toString() {
        return "\n" +
                "    bookingDto{\n" +
                "               id = " + id + "\n" +
                "               start = " + start + "\n" +
                "               end = " + end + "\n" +
                "               status = " + status + "\n" +
                "               user = " + user + "\n" +
                "               item = " + item + "\n" +
                "              }";
    }
}
