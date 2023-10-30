package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    default User addUser(UserDto userDto) {
        return null;
    }

    default User getUserById(long userId) {
        return null;
    }

    default List<User> getAllUsers() {
        return null;
    }

    default boolean getUserByEmail(String userEmail) {
        return true;
    }

    default User update(long userId, UserDto userDto) {
        return null;
    }

    default void delete(long userId) {
    }
}
