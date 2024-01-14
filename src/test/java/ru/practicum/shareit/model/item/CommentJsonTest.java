package ru.practicum.shareit.model.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class CommentJsonTest {
    @Autowired
    JacksonTester<Comment> json;

    @Test
    void commentJsonTest() throws Exception {
        User user = new User(1,"user@mail.ru","name1");
        Item item = new Item(1, "name1", "description", true, 1,null);
        LocalDateTime created = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        Comment comment1 = new Comment(
                1,
                "text1",
                item,
                user,
                created);

        JsonContent<Comment> result = json.write(comment1);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo("text1");
        assertThat(result).extractingJsonPathNumberValue("$.item.id").isEqualTo(item.getId());
        assertThat(result).extractingJsonPathStringValue("$.item.name").isEqualTo(item.getName());
        assertThat(result).extractingJsonPathStringValue("$.item.description").isEqualTo(item.getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.item.available").isEqualTo(item.getAvailable());
        assertThat(result).extractingJsonPathNumberValue("$.item.ownerId").isEqualTo(item.getOwnerId());
        assertThat(result).extractingJsonPathStringValue("$.item.request").isEqualTo(item.getRequest());
        assertThat(result).extractingJsonPathNumberValue("$.author.id").isEqualTo(user.getId());
        assertThat(result).extractingJsonPathStringValue("$.author.name").isEqualTo(user.getName());
        assertThat(result).extractingJsonPathStringValue("$.author.email").isEqualTo(user.getEmail());
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo(comment1.getCreated().toString());
    }
}
