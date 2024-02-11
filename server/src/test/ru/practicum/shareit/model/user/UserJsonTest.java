package ru.practicum.shareit.model.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.model.User;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class UserJsonTest {
    @Autowired
    private JacksonTester<User> json;

    @Test
    void userTest() throws Exception {
        User user1 = new User(
                1,
                "john.doe@mail.com",
                "John");

        JsonContent<User> result = json.write(user1);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("john.doe@mail.com");
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("John");
    }

}
