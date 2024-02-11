package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.request.model.ItemRequest;

import javax.persistence.*;

/**
 * Item entity.
 */
@Entity
@Table(name = "items", schema = "public")
@Builder
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "is_available")
    private Boolean available;

    @Column(name = "owner_id")
    private int ownerId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "request_id")
    private ItemRequest request;

    @Override
    public String toString() {
        return "\n" +
                "    Item{\n" +
                "         id = " + id + "\n" +
                "         name = " + name + "\n" +
                "         description = " + description + "\n" +
                "         available = " + available + "\n" +
                "         ownerId = " + ownerId + "\n" +
                "         request = " + request + "\n" +
                "        }";
    }
}
