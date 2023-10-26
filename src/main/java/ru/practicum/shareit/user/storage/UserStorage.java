package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.User;

import java.util.List;

public interface UserStorage {
    User save(User user);

    User found(long userId);

    List<User> get();

    User update(long userId, User user);

    void delete(User user);
}
