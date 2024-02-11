package ru.practicum.shareit.item.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Builder
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private Integer id;

    private String text;

    private String authorName;

    private LocalDateTime created;

    @Override
    public String toString() {
        return "\n" +
                "    CommentDto{\n" +
                "               id = " + id + "\n" +
                "               text = " + text + "\n" +
                "               authorName = " + authorName + "\n" +
                "               created = " + created.truncatedTo(ChronoUnit.SECONDS) + "\n" +
                "              }";
    }
}
