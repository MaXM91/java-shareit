package ru.practicum.shareit.item.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Component
public class CommentMapper {
    public Comment toComment(User user, Item item, CommentDto commentDto) {
        Comment newComment = new Comment();

        if (commentDto.getId() == null) {
            newComment.setId(0);
        } else {
            newComment.setId(commentDto.getId());
        }

        newComment.setText(commentDto.getText());
        newComment.setItem(item);
        newComment.setAuthor(user);

        if (commentDto.getCreated() == null) {
            newComment.setCreated(LocalDateTime.now());
        } else {
            newComment.setCreated(commentDto.getCreated());
        }

        return newComment;
    }

    public CommentDto toCommentDto(Comment comment) {
        CommentDto newCommentDto = new CommentDto();

        newCommentDto.setId(comment.getId());
        newCommentDto.setText(comment.getText());
        newCommentDto.setAuthorName(comment.getAuthor().getName());
        newCommentDto.setCreated(comment.getCreated());

        return newCommentDto;
    }
}
