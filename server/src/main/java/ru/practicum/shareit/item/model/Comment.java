package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Table(name = "comments")
@Entity
@Builder
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@NamedNativeQuery(name = "CommentForItem",
        query = "SELECT c.id AS id, c.text AS text, u.name AS authorName, c.created AS created " +
                "FROM comments AS c " +
                "LEFT OUTER JOIN users AS u ON c.author_id = u.id " +
                "WHERE item_id = ?1 ",
        resultSetMapping = "MyCommentForItemWithBookingCommentDto")

@SqlResultSetMapping(
        name = "MyCommentForItemWithBookingCommentDto",
        classes = { @ConstructorResult(
                columns = {
                        @ColumnResult(name = "id", type = Integer.class),
                        @ColumnResult(name = "text", type = String.class),
                        @ColumnResult(name = "authorName", type = String.class),
                        @ColumnResult(name = "created", type = LocalDateTime.class)
                },
                targetClass = CommentDto.class
        )}
)
public class Comment {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "text")
    private String text;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    private LocalDateTime created;

    @Override
    public String toString() {
        return "\n" +
                "    Comment{\n" +
                "            id = " + id + "\n" +
                "            text = " + text + "\n" +
                "            item = " + item + "\n" +
                "            author = " + author + "\n" +
                "            created = " + created.truncatedTo(ChronoUnit.SECONDS) + "\n" +
                "           }";
    }
}
