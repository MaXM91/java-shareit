package ru.practicum.shareit.request.model;

import lombok.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * ItemRequest entity.
 */
@Entity
@Table(name = "requests")
@Builder
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequest {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id")
    private User requester;

    @Column(name = "created")
    private LocalDateTime created;

    @OneToMany(mappedBy = "request", fetch = FetchType.LAZY)
    private List<Item> items;

    @Override
    public String toString() {
        return "\n" +
                "    ItemRequest{\n" +
                "               id = " + id + "\n" +
                "               description = " + description + "\n" +
                "               requester = " + requester + "\n" +
                "               created = " + created.truncatedTo(ChronoUnit.SECONDS) + "\n" +
                "               items = " + items + "\n" +
                "        }";
    }
}
