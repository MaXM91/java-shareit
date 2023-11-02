package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    default UserDto addUser(UserDto userDto) {
        return null;
    }

    default UserDto getUserById(int userId) {
        return null;
    }

    default User getUserEntityById(int userId) {
        return null;
    }

    default List<UserDto> getAllUsers() {
        return null;
    }

    default boolean getUserByEmail(String userEmail) {
        return true;
    }

    default UserDto update(int userId, UserDto userDto) {
        return null;
    }

    default void delete(int userId) {
    }
}

