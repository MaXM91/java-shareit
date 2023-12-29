package ru.practicum.shareit.request.dto;

import lombok.*;
import ru.practicum.shareit.item.model.Item;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Item-requests entity.
 */
@Data
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestDto {
    private Integer id;

    @NotNull(message = "description can not be null")
    @NotBlank(message = "description can not be empty or blank")
    private String description;

    private Integer requester;

    private LocalDateTime created = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

    private List<Item> items;

    @Override
    public String toString() {
        return "\n" +
                "    ItemRequestDto{\n" +
                "                   id = " + id + "\n" +
                "                   description = " + description + "\n" +
                "                   requester = " + requester + "\n" +
                "                   created = " + created.truncatedTo(ChronoUnit.SECONDS) + "\n" +
                "                   items = " + items + "\n" +
                "        }";
    }
}
