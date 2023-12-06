package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    UserDto addUser(UserDto userDto);

    UserDto getUserById(int userId);

    User getUserEntityById(int userId);

    List<UserDto> getAllUsers();

    boolean getUserByEmail(String userEmail);

    UserDto update(int userId, UserDto userDto);

    void delete(int userId);
}

