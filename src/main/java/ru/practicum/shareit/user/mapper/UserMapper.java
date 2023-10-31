package ru.practicum.shareit.user.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;

@Component
public class UserMapper {
    public UserDto toUserDto(User user) {
        return new UserDto(user.getEmail(), user.getName());
    }

    public User toUser(UserDto userDto) {
        return new User(0, userDto.getEmail(), userDto.getName());
    }
}
