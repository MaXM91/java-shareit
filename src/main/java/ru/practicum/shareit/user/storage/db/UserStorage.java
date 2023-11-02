package ru.practicum.shareit.user.storage.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

@Repository
public interface UserStorage extends JpaRepository<User, Integer>, QuerydslPredicateExecutor<User> {
    User getUserByEmail(String email);

    boolean existsByEmail(String email);

    void deleteByEmail(String email);
}
