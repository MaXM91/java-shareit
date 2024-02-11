package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;
import ru.practicum.shareit.validation.exceptions.EmailRegisteredException;
import ru.practicum.shareit.validation.exceptions.ObjectNotFoundException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Qualifier("UserServiceImpl")
@Service
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;
    private final UserMapper userMapper;

    @Autowired
    UserServiceImpl(UserStorage userStorage, UserMapper userMapper) {
        this.userStorage = userStorage;
        this.userMapper = userMapper;
    }

    @Override
    public UserDto addUser(UserDto userDto) {
        User user = userMapper.toUser(userDto);

        user = userStorage.save(user);
        return userMapper.toUserDto(user);
    }

    @Override
    public UserDto getUserById(int userId) {
        return userMapper.toUserDto(getUser(userId));
    }

    /**
     * getUserEntityById only for mapping on services
     */
    @Override
    public User getUserEntityById(int userId) {
        return getUser(userId);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> usersList = userStorage.findAll();

        return usersList.stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean getUserByEmail(String userEmail) {
        return userStorage.existsByEmail(userEmail);
    }

    @Override
    public UserDto update(int userId, UserDto userDto) {
        User user = userMapper.toUser(userDto);
        User check = getUser(userId);

        check.setId(userId);

        if (user.getEmail() != null && !user.getEmail().isEmpty() && !user.getEmail().isBlank() &&
                !check.getEmail().equals(user.getEmail())) {
            checkEmail(user.getEmail());

            check.setEmail(user.getEmail());
        }

        if (user.getName() != null && !user.getName().isEmpty() && !user.getName().isBlank() &&
                !check.getName().equals(user.getName())) {
            check.setName(user.getName());
        }

        return userMapper.toUserDto(userStorage.save(check));
    }

    @Override
    public void delete(int userId) {
        User user = getUser(userId);
        userStorage.deleteByEmail(user.getEmail());
    }

    private void checkEmail(String email) {
        if (userStorage.getUserByEmail(email) != null) {
            throw new EmailRegisteredException("the email was registered");
        }
    }

    private User getUser(int userId) {
        return userStorage.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("user id - " + userId + " not found"));
    }
}
