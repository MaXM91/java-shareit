package ru.practicum.shareit.model.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemDto;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


@JsonTest
public class ItemDtoJsonTest {
    @Autowired
    JacksonTester<ItemDto> json;

    @Test
    void itemDtoJsonTest() throws Exception {
        ItemDto itemDto1 = new ItemDto(1, "name1", "description1", true, 1);

        JsonContent<ItemDto> result = json.write(itemDto1);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("name1");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("description1");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
        assertThat(result).extractingJsonPathValue("$.requestId").isEqualTo(1);
    }
}
