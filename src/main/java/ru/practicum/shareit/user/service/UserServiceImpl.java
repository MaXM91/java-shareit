package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    UserMapper userMapper;
    UserStorage userStorage;

    UserServiceImpl(UserMapper userMapper,
                    @Qualifier("UserStorageImpl") UserStorage userStorage) {
        this.userMapper = userMapper;
        this.userStorage = userStorage;
    }

    @Override
    public User addUser(UserDto userDto) {
        return null;
    }

    @Override
    public User getUserById(long userId) {
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        return null;
    }

    public boolean getUserByEmail(String userEmail) {
        return true;
    }

    @Override
    public User update(long userId, UserDto userDto) {
        return null;
    }

    @Override
    public void delete(long userId) {

    }
}
