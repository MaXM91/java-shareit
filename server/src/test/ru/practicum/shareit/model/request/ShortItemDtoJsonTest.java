package ru.practicum.shareit.model.request;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.request.dto.ShortItemDto;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
@JsonTest
public class ShortItemDtoJsonTest {
    @Autowired
    JacksonTester<ShortItemDto> json;

    @Test
    void shortItemDtoJsonTest() throws Exception {
        ShortItemDto shortItemDto1 = new ShortItemDto(1, "name1", "description1", true, 1);
        ShortItemDto shortItemDto2 = new ShortItemDto(1, "name1", "description1", true, 1);

        JsonContent<ShortItemDto> result = json.write(shortItemDto1);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("name1");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("description1");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
        assertThat(result).extractingJsonPathValue("$.requestId").isEqualTo(1);
        Assertions.assertEquals(shortItemDto1.toString(), shortItemDto2.toString());
    }
}