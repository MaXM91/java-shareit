package ru.practicum.shareit.storage;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class UserStorageTest {
    @Autowired
    private UserStorage userStorage;

    @Test
    void addUserTest() {
        User userBefore = new User(0, "email1@mail.ru", "name1");
        User savedUser = userStorage.save(userBefore);

        User userEnd = new User(1, "email1@mail.ru", "name1");

        Optional<User> actualUser = userStorage.findById(savedUser.getId());

        Assertions.assertTrue(actualUser.isPresent());
        Assertions.assertNotNull(actualUser.get().getId());

        assertThat(userEnd)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(actualUser.get());
    }

    @Test
    void getUserByEmailTest() {
        User userBefore = new User(0, "email1@mail.ru", "name1");
        User savedUser = userStorage.save(userBefore);

        User userEnd = new User(1, "email1@mail.ru", "name1");

        User actualUser = userStorage.getUserByEmail(userEnd.getEmail());

        Assertions.assertTrue(actualUser != null);
        Assertions.assertNotNull(actualUser.getId());

        assertThat(userEnd)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(actualUser);
    }

    @Test
    void existsByEmailTest() {
        User userBefore = new User(0, "email1@mail.ru", "name1");
        User savedUser = userStorage.save(userBefore);

        User userEnd = new User(1, "email1@mail.ru", "name1");

        boolean actual = userStorage.existsByEmail(userEnd.getEmail());

        assertThat(true).isEqualTo(actual);
    }

    @Test
    void deleteByEmailTest() {
        User userBefore = new User(0, "email1@mail.ru", "name1");
        User savedUser = userStorage.save(userBefore);

        User userEnd = new User(1, "email1@mail.ru", "name1");

        userStorage.deleteByEmail(userEnd.getEmail());

        User actual = userStorage.getUserByEmail(userEnd.getEmail());

        Assertions.assertNull(actual);
    }
}
