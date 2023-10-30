package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.User;

import java.util.List;

public interface UserStorage {
    User save(User user);

    User found(long userId);

    List<User> get();

    boolean checkUserByEmail(String userEmail);

    User update(User user);

    void delete(String email);
}
