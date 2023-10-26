package ru.practicum.shareit.user.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.validation.exception.EmailRegisteredException;
import ru.practicum.shareit.validation.exception.ObjectNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Qualifier("InMemoryUserStorage")
@Slf4j
@Repository
public class InMemoryUserStorage implements UserStorage {
    private long id = 1;
    private final Map<String, User> users = new HashMap<>();

    @Override
    public User save(User user) {
        checkEmail(user.getEmail());
        user.setId(id++);
        users.put(user.getEmail(), user);

        return user;
    }

    @Override
    public User found(long userId) {
        return foundUser(userId);
    }

    @Override
    public List<User> get() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User update(long userId, User user) {
        User check = foundUser(userId);

        if (user.getEmail() != null && !user.getEmail().isEmpty() && !user.getEmail().isBlank() && !check.getEmail().equals(user.getEmail())) {
            checkEmail(user.getEmail());
            delete(check);
            check.setEmail(user.getEmail());
        }

        if (user.getName() != null && !user.getName().isEmpty() && !user.getName().isBlank() && !check.getName().equals(user.getName())) {
            check.setName(user.getName());
        }

        users.put(check.getEmail(), check);
        return check;
    }

    @Override
    public void delete(User user) {
        users.remove(user.getEmail());
    }

    private User foundUser(long userId) {
        User foundUser = null;
        for (User user : users.values()) {
            if (user.getId() == userId) {
                foundUser = user;
                break;
            }
        }

        if (foundUser == null) {
            log.info("user id - {} not found", userId);
            throw new ObjectNotFoundException("User id - " + userId + " not found");
        } else {
            return foundUser;
        }
    }

    private void checkEmail(String email) {
        if (users.containsKey(email)) {
            throw new EmailRegisteredException("the email was registered");
        }
    }
}
