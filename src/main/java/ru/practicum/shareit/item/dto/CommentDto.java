package ru.practicum.shareit.item.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Builder
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    Integer id;

    @NotBlank(message = "text must be not blank")
    String text;

    String authorName;

    LocalDateTime created;

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
