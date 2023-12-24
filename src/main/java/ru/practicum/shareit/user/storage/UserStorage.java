package ru.practicum.shareit.user.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.user.model.User;

public interface UserStorage extends JpaRepository<User, Integer> {
    User getUserByEmail(String email);

    boolean existsByEmail(String email);

    void deleteByEmail(String email);
}
