package ru.practicum.shareit.storage;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.CommentStorage;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@DataJpaTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class CommentStorageTest {
    @Autowired
    CommentStorage commentStorage;

    @Autowired
    UserStorage userStorage;

    @Autowired
    ItemStorage itemStorage;

    @Test
    void findAllByItemIdTest() {
        User userLast = new User(0, "email1@mail.ru", "name1");
        User userSave = userStorage.save(userLast);

        Item itemLast = new Item(0, "name3", "description3", true, userSave.getId(), null);
        Item itemSave = itemStorage.save(itemLast);

        LocalDateTime created = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        Comment comment = new Comment(0, "text1", itemSave, userSave, created);

        Comment response = commentStorage.save(comment);

        CommentDto responseDto =
                new CommentDto(response.getId(), response.getText(), response.getAuthor().getName(), response.getCreated());

        List<CommentDto> actual = commentStorage.findAllByItemId(response.getItem().getId());

        Assertions.assertTrue(actual != null && !actual.isEmpty());
        Assertions.assertEquals(responseDto, actual.get(0));
    }
}
