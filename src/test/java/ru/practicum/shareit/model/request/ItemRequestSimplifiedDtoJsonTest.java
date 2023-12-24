package ru.practicum.shareit.model.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.request.dto.ItemRequestSimplifiedDto;
import ru.practicum.shareit.request.dto.ShortItemDto;
import ru.practicum.shareit.request.dto.ShortUserDto;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class ItemRequestSimplifiedDtoJsonTest {
    @Autowired
    JacksonTester<ItemRequestSimplifiedDto> json;

    @Test
    void itemRequestSimplifiedDtoJsonTest() throws Exception {
        ShortUserDto user1 = new ShortUserDto(1);
        ShortItemDto item1 = new ShortItemDto(1, "name1", "description1", true, 1);
        ItemRequestSimplifiedDto itemRequest1 =
                new ItemRequestSimplifiedDto(2, "description1", user1,
                        LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS), List.of(item1));

        JsonContent<ItemRequestSimplifiedDto> result = json.write(itemRequest1);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(2);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("description1");
        assertThat(result).extractingJsonPathNumberValue("$.requester.id").isEqualTo(user1.getId());
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo(itemRequest1.getCreated().toString());
        assertThat(result).extractingJsonPathNumberValue("$.items[0].id").isEqualTo(item1.getId());
        assertThat(result).extractingJsonPathStringValue("$.items[0].name").isEqualTo(item1.getName());
        assertThat(result).extractingJsonPathStringValue("$.items[0].description").isEqualTo(item1.getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.items[0].available").isEqualTo(true);
        assertThat(result).extractingJsonPathNumberValue("$.items[0].requestId").isEqualTo(item1.getRequestId());
    }
}
