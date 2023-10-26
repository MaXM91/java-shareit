package ru.practicum.shareit.user.storage;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.User;

import java.util.List;

@Qualifier("UserStorageImpl")
@Repository
public class UserStorageImpl implements UserStorage {
    @Override
    public User save(User user) {
        return null;
    }

    @Override
    public User found(long userId) {
        return null;
    }

    @Override
    public List<User> get() {
        return null;
    }

    @Override
    public User update(long userId, User user) {
        return null;
    }

    @Override
    public void delete(User user) {

    }
}
