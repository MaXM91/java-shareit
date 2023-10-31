package ru.practicum.shareit.user.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;

@Component
public class UserMapper {
    public UserDto toUserDto(User user) {
        return new UserDto(user.getId(), user.getEmail(), user.getName());
    }

    public User toUser(UserDto userDto) {
        int id = 0;

        if (userDto.getId() != null && userDto.getId() > 0) {
            id = userDto.getId();
        }

        return new User(id, userDto.getEmail(), userDto.getName());
    }
}
