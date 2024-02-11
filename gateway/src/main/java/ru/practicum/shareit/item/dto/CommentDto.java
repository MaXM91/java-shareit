package ru.practicum.shareit.item.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private Integer id;

    @NotBlank(message = "text must be not blank")
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
                "               created = " + created + "\n" +
                "              }";
    }
}
