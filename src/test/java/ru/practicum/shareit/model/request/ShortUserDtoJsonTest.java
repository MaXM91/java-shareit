package ru.practicum.shareit.model.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.request.dto.ShortUserDto;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class ShortUserDtoJsonTest {
    @Autowired
    JacksonTester<ShortUserDto> json;

    @Test
    void shortUserDtoJsonTest() throws Exception {
        ShortUserDto shortUserDto1 = new ShortUserDto(1);

        JsonContent<ShortUserDto> result = json.write(shortUserDto1);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
    }
}
