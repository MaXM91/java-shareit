package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.memory.InMemoryUserStorage;
import ru.practicum.shareit.validation.exceptions.EmailRegisteredException;

import java.util.List;
import java.util.stream.Collectors;

@Qualifier("InMemoryUserService")
@Service
public class InMemoryUserService implements UserService {
    private final UserMapper userMapper;
    private final InMemoryUserStorage userStorage;

    @Autowired
    InMemoryUserService(UserMapper userMapper, InMemoryUserStorage userStorage) {
        this.userMapper = userMapper;
        this.userStorage = userStorage;
    }

    @Override
    public UserDto addUser(UserDto userDto) {
        User user = userMapper.toUser(userDto);

        checkEmail(user.getEmail());

        return userMapper.toUserDto(userStorage.save(user));
    }

    @Override
    public UserDto getUserById(int userId) {
        return userMapper.toUserDto(userStorage.found(userId));
    }

    @Override
    public User getUserEntityById(int userId) {
        return null;
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> usersList = userStorage.get();

        return usersList.stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean getUserByEmail(String userEmail) {
        return userStorage.checkUserByEmail(userEmail);
    }

    @Override
    public UserDto update(int userId, UserDto userDto) {
        User user = userMapper.toUser(userDto);
        User check = userMapper.toUser(getUserById(userId));

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

        return userMapper.toUserDto(userStorage.update(check));
    }

    @Override
    public void delete(int userId) {
        User user = userMapper.toUser(getUserById(userId));
        userStorage.delete(user.getEmail());
    }

    private void checkEmail(String email) {
        if (getUserByEmail(email)) {
            throw new EmailRegisteredException("the email was registered");
        }
    }
}
