package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.storage.db.CommentStorage;
import ru.practicum.shareit.user.service.UserService;

@Service
public class CommentService {
    private final CommentStorage commentStorage;
    private final CommentMapper commentMapper;
    private final UserService userService;
    private final ItemService itemService;
    private final BookingService bookingService;

    @Autowired
    CommentService(CommentStorage commentStorage, CommentMapper commentMapper,
                   @Qualifier("UserServiceImpl") UserService userService,
                   @Qualifier("ItemServiceImpl") ItemService itemService,
                   @Qualifier("BookingServiceImpl") BookingService bookingService) {
        this.commentStorage = commentStorage;
        this.commentMapper = commentMapper;
        this.userService = userService;
        this.itemService = itemService;
        this.bookingService = bookingService;
    }

    public CommentDto addComment(int userId, int itemId, CommentDto commentDto) {
        checkUsers(userId);
        checkBooking(userId, itemId);

    Comment newComment = commentMapper.toComment(userService.getUserEntityById(userId),
                itemService.getItemEntityById(itemId), commentDto);

        return commentMapper.toCommentDto(commentStorage.save(newComment));
    }

    private void checkUsers(int userId) {
        userService.getUserById(userId);
    }

    private void checkBooking(int userId, int itemId) {
        bookingService.getBookingByUserIdAndItemId(userId, itemId);
    }
}
