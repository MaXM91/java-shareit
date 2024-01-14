package ru.practicum.shareit.mappers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ContextConfiguration(classes = {UserMapper.class})
public class UserMapperTest {
    @Autowired
    UserMapper userMapper;

    @Test
    void toUserDto_200_Test() {
        User user1 = new User(1, "user@mail,ru", "name1");

        UserDto userDto1 = userMapper.toUserDto(user1);

        assertEquals(userDto1.getId(), user1.getId());
        assertEquals(userDto1.getName(), user1.getName());
        assertEquals(userDto1.getEmail(), user1.getEmail());
    }

    @Test
    void toUser_200_Test() {
        UserDto userDto1 = new UserDto(1, "name1", "userDto@mail,ru");

        User user1 = userMapper.toUser(userDto1);

        assertEquals(user1.getId(), userDto1.getId());
        assertEquals(user1.getName(), userDto1.getName());
        assertEquals(user1.getEmail(), userDto1.getEmail());
    }

    @Test
    void toUser_id_null_200_Test() {
        UserDto userDto1 = new UserDto(null, "name1", "userDto@mail,ru");

        User user1 = userMapper.toUser(userDto1);

        assertEquals(user1.getId(), 0);
        assertEquals(user1.getName(), userDto1.getName());
        assertEquals(user1.getEmail(), userDto1.getEmail());
    }
}