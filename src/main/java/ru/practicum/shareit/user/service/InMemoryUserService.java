package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.storage.UserStorage;
import ru.practicum.shareit.validation.exception.EmailRegisteredException;

import java.util.List;

@Service
public class InMemoryUserService implements UserService {
    private final UserMapper userMapper;
    private final UserStorage userStorage;

    @Autowired
    InMemoryUserService(UserMapper userMapper, UserStorage userStorage) {
        this.userMapper = userMapper;
        this.userStorage = userStorage;
    }

    @Override
    public User addUser(UserDto userDto) {
        User user = userMapper.toUser(userDto);

        checkEmail(user.getEmail());

        return userStorage.save(user);
    }

    @Override
    public User getUserById(int userId) {
        return userStorage.found(userId);
    }

    @Override
    public List<User> getAllUsers() {
        return userStorage.get();
    }

    @Override
    public boolean getUserByEmail(String userEmail) {
        return userStorage.checkUserByEmail(userEmail);
    }

    @Override
    public User update(int userId, UserDto userDto) {
        User user = userMapper.toUser(userDto);
        User check = getUserById(userId);

        if (user.getEmail() != null && !user.getEmail().isEmpty() && !user.getEmail().isBlank() &&
                !check.getEmail().equals(user.getEmail())) {
            checkEmail(user.getEmail());
            delete(check.getId());
            check.setEmail(user.getEmail());
        }

        if (user.getName() != null && !user.getName().isEmpty() && !user.getName().isBlank() &&
                !check.getName().equals(user.getName())) {
            check.setName(user.getName());
        }

        return userStorage.update(check);
    }

    @Override
    public void delete(int userId) {
        User user = getUserById(userId);
        userStorage.delete(user.getEmail());
    }

    private void checkEmail(String email) {
        if (getUserByEmail(email)) {
            throw new EmailRegisteredException("the email was registered");
        }
    }
}