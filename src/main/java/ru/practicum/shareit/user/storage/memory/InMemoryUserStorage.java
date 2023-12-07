package ru.practicum.shareit.user.storage.memory;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface InMemoryUserStorage {

        User save(User user);

        User found(int userId);

        List<User> get();

        boolean checkUserByEmail(String userEmail);

        User update(User user);

        void delete(String email);

}
