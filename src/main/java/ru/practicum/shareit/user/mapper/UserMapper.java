package ru.practicum.shareit.user.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

@Component
public class UserMapper {
    public UserDto toUserDto(User user) {
        if (user == null) {
            return null;
        }

        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }

    public User toUser(UserDto userDto) {
        if (userDto == null) {
            return null;
        }

        User user = new User();

        if (userDto.getId() == null) {
            user.setId(0);
        } else {
            user.setId(userDto.getId());
        }

        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());

        return user;
    }
}

