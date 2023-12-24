package ru.practicum.shareit.storage;

import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class ItemStorageTest {
    @Autowired
    ItemStorage itemStorage;

    @Autowired
    UserStorage userStorage;

    @Test
    void addItemTest() {
        User userBefore = new User(0, "email1@mail.ru", "name1");
        User savedUser = userStorage.save(userBefore);

        Item itemBefore = new Item(0, "name1", "description1", true, savedUser.getId(), null);
        Item savedItem = itemStorage.save(itemBefore);

        Item itemEnd = new Item(1, "name1", "description1", true, savedUser.getId(), null);

        Optional<Item> actualItem = itemStorage.findById(savedItem.getId());

        Assertions.assertTrue(actualItem.isPresent());
        Assertions.assertNotNull(actualItem.get().getId());

        assertThat(itemEnd)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(actualItem.get());
    }

    @Test
    void searchTest() {
        User userBefore = new User(0, "email2@mail.ru", "name2");
        User savedUser = userStorage.save(userBefore);

        Item itemBefore = new Item(0, "name2", "description2", true, savedUser.getId(), null);
        Item savedItem = itemStorage.save(itemBefore);

        Item itemEnd = new Item(2, "name2", "description2", true, savedUser.getId(), null);

        List<Item> actualItem = itemStorage.search("script");

        Assertions.assertNotNull(actualItem.get(0).getId());

        assertThat(itemEnd)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(actualItem.get(0));
    }

    @Test
    void findByOwnerIdTest() {
        User userBefore = new User(0, "email3@mail.ru", "name3");
        User savedUser = userStorage.save(userBefore);

        Item itemBefore = new Item(0, "name3", "description3", true, savedUser.getId(), null);
        Item savedItem = itemStorage.save(itemBefore);

        Item itemEnd = new Item(3, "name3", "description3", true, savedUser.getId(), null);

        List<Item> actualItem = itemStorage.findByOwnerId(savedUser.getId());

        Assertions.assertNotNull(actualItem);

        assertThat(itemEnd)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(actualItem.get(0));
    }
}
