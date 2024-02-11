package ru.practicum.shareit.user.model;

import lombok.*;

import javax.persistence.*;

/**
 * Basic entity User
 */
@Entity
@Table(name = "users", schema = "public")
@Builder
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_seq")
    private int id;

    @Column(name = "email")
    private String email;

    @Column(name = "name")
    private String name;

    @Override
    public String toString() {
        return "\n" +
            "    User{\n" +
            "         id = " + id + "\n" +
            "         email = " + email + "\n" +
            "         name = " + name + "\n" +
            "        }";
    }
}

