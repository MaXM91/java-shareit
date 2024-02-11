package ru.practicum.shareit.model.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class ItemRequestDtoJsonTest {
    @Autowired
    JacksonTester<ItemRequestDto> json;

    @Test
    void itemRequestJsonTest() throws Exception {

        ItemDto item1 = new ItemDto(1, "name1", "description1",true, 5);
        ItemRequestDto itemRequest1 =
                new ItemRequestDto(2, "description1", 1,
                        LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS), List.of(item1));

        JsonContent<ItemRequestDto> result = json.write(itemRequest1);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(2);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("description1");
        assertThat(result).extractingJsonPathNumberValue("$.requester").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo(itemRequest1.getCreated().toString());
        assertThat(result).extractingJsonPathNumberValue("$.items[0].id").isEqualTo(item1.getId());
        assertThat(result).extractingJsonPathStringValue("$.items[0].name").isEqualTo(item1.getName());
        assertThat(result).extractingJsonPathStringValue("$.items[0].description").isEqualTo(item1.getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.items[0].available").isEqualTo(item1.getAvailable());
    }
}
