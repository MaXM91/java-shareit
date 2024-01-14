package ru.practicum.shareit.model.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.dto.UserDto;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class UserDtoJsonTest {
    @Autowired
    private JacksonTester<UserDto> json;

    @Test
    void userDtoTest() throws Exception {
        UserDto userDto1 = new UserDto(1, "John","john.doe@mail.com");

        JsonContent<UserDto> result = json.write(userDto1);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("john.doe@mail.com");
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("John");
    }
}