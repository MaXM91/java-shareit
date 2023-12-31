package ru.practicum.shareit.user.storage.memory;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.validation.exceptions.ObjectNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserStorageInMemory implements InMemoryUserStorage {
    private int id = 1;
    private final Map<String, User> users = new HashMap<>();

    @Override
    public User save(User user) {
        user.setId(id++);
        users.put(user.getEmail(), user);

        return user;
    }

    @Override
    public User found(int userId) {
        return foundUser(userId);
    }

    @Override
    public List<User> get() {
        return new ArrayList<>(users.values());
    }

    @Override
    public boolean checkUserByEmail(String email) {
        return users.containsKey(email);
    }

    @Override
    public User update(User user) {
        users.put(user.getEmail(), user);
        return user;
    }

    @Override
    public void delete(String email) {
        users.remove(email);
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
            throw new ObjectNotFoundException("user id - " + userId + " not found");
        } else {
            return foundUser;
        }
    }
}
