package ru.practicum.shareit.mappers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ContextConfiguration(classes = {CommentMapper.class})
public class CommentMapperTest {
    @Autowired
    CommentMapper commentMapper;

    @Test
    void toCommentDtoTest() {
        User user1 = new User(1, "email1@mail.ru","name1");
        Item item1 = new Item(1,"name1", "description1", true,1, null);
        LocalDateTime time1 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        Comment newComment1 = new Comment(1, "text1", item1, user1, time1);

        CommentDto commentDto1 = commentMapper.toCommentDto(newComment1);

        assertEquals(newComment1.getId(), commentDto1.getId());
        assertEquals(newComment1.getText(), commentDto1.getText());
        assertEquals(newComment1.getAuthor().getName(), commentDto1.getAuthorName());
        assertEquals(newComment1.getCreated().toString(), commentDto1.getCreated().toString());
    }

    @Test
    void toComment_id_not_null_Test() {
        User user1 = new User(1, "email1@mail.ru","name1");
        Item item1 = new Item(1,"name1", "description1", true,1, null);
        LocalDateTime time1 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        CommentDto newCommentDto1 = new CommentDto(1, "text1", "name1", time1);

        Comment comment1 = commentMapper.toComment(user1, item1, newCommentDto1);

        assertEquals(newCommentDto1.getId(), comment1.getId());
        assertEquals(newCommentDto1.getText(), comment1.getText());
        assertEquals(newCommentDto1.getAuthorName(), comment1.getAuthor().getName());
        assertEquals(newCommentDto1.getCreated().toString(), comment1.getCreated().toString());

    }

    @Test
    void toComment_id_null_Test() {
        User user1 = new User(1, "email1@mail.ru","name1");
        Item item1 = new Item(1,"name1", "description1", true,1, null);
        LocalDateTime time1 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        CommentDto newCommentDto1 = new CommentDto(null, "text1", "name1", time1);

        Comment comment1 = commentMapper.toComment(user1, item1, newCommentDto1);

        assertEquals(0, comment1.getId());
        assertEquals(newCommentDto1.getText(), comment1.getText());
        assertEquals(newCommentDto1.getAuthorName(), comment1.getAuthor().getName());
        assertEquals(newCommentDto1.getCreated().toString(), comment1.getCreated().toString());

    }

    @Test
    void toComment_created_null_Test() {
        User user1 = new User(1, "email1@mail.ru","name1");
        Item item1 = new Item(1,"name1", "description1", true,1, null);
        LocalDateTime time1 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        CommentDto newCommentDto1 = new CommentDto(1, "text1", "name1", null);

        Comment comment1 = commentMapper.toComment(user1, item1, newCommentDto1);

        assertEquals(newCommentDto1.getId(), comment1.getId());
        assertEquals(newCommentDto1.getText(), comment1.getText());
        assertEquals(newCommentDto1.getAuthorName(), comment1.getAuthor().getName());
        assertNotNull(comment1.getCreated());

    }
}
