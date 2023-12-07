package ru.practicum.shareit.user.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

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
    @NotBlank(message = "blank/empty email")
    @Email(message = "bad email")
    private String email;

    @Column(name = "name")
    @NotBlank(message = "blank/empty name")
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

