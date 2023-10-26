package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;

@Qualifier("InMemoryUserService")
@Service
public class InMemoryUserService implements UserService {
    private final UserMapper userMapper;
    private final UserStorage userStorage;

    @Autowired
    InMemoryUserService(UserMapper userMapper,
                        @Qualifier("InMemoryUserStorage") UserStorage userStorage) {
        this.userMapper = userMapper;
        this.userStorage = userStorage;
    }

    @Override
    public User addUser(UserDto userDto) {
        return userStorage.save(userMapper.toUser(userDto));
    }

    @Override
    public User getUserById(long userId) {
        return userStorage.found(userId);
    }

    @Override
    public List<User> getAllUsers() {
        return userStorage.get();
    }

    @Override
    public User update(long userId, UserDto userDto) {
        return userStorage.update(userId, userMapper.toUser(userDto));
    }

    @Override
    public void delete(long userId) {
        User user = getUserById(userId);
        userStorage.delete(user);
    }
}