package ru.practicum.shareit.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.CommentServiceImpl;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.item.storage.CommentStorage;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@SpringBootTest
@ContextConfiguration(classes = {CommentServiceImpl.class, CommentMapper.class})
public class CommentServiceTest {
    @SpyBean
    CommentMapper commentMapper;

    @Autowired
    CommentServiceImpl commentServiceImpl;

    @MockBean
    CommentStorage commentStorage;

    @MockBean
    UserServiceImpl userService;

    @MockBean
    ItemServiceImpl itemService;

    @MockBean
    BookingServiceImpl bookingService;

    @Test
    void addCommentTest() {
        UserDto userDto1 = new UserDto(1,"name1","name1@mail.ru");
        LocalDateTime time1 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        CommentDto commentDto1 = new CommentDto(0, "text1", "name1", time1);
        CommentDto commentExpectedDto1 = new CommentDto(1, "text1", "name1", time1);
        User user1 = new User(1, "email1@mail.ru","name1");
        Item item1 = new Item(1,"name1", "description1", true,1, null);
        Comment newComment1 = new Comment(1, "text1", item1, user1, time1);

        when(userService.getUserById(anyInt()))
                .thenReturn(userDto1);
        when(userService.getUserEntityById(anyInt()))
                .thenReturn(user1);
        when(itemService.getItemEntityById(anyInt()))
                .thenReturn(item1);
        when(commentStorage.save(any(Comment.class)))
                .thenReturn(newComment1);


        CommentDto actualComment = commentServiceImpl.addComment(1,1, commentDto1);

        Assertions.assertEquals(commentExpectedDto1.toString(), actualComment.toString());
        Assertions.assertEquals(commentMapper.toComment(user1, item1, commentExpectedDto1).toString(),
                commentMapper.toComment(user1, item1, actualComment).toString());

        Mockito.verify(userService, Mockito.times(1)).getUserById(anyInt());
        Mockito.verify(userService, Mockito.times(1)).getUserEntityById(anyInt());
        Mockito.verify(itemService, Mockito.times(1)).getItemEntityById(anyInt());
        Mockito.verify(commentStorage, Mockito.times(1)).save(any(Comment.class));

        Mockito.verify(commentMapper, Mockito.times(3)).toComment(any(), any(), any());
        Mockito.verify(commentMapper, Mockito.times(1)).toCommentDto(any());
        Mockito.verifyNoMoreInteractions(userService);
        Mockito.verifyNoMoreInteractions(itemService);
        Mockito.verifyNoMoreInteractions(commentStorage);
        Mockito.verifyNoMoreInteractions(commentMapper);
    }
}
