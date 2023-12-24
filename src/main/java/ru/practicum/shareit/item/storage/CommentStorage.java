package ru.practicum.shareit.item.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;

@Repository
public interface CommentStorage extends JpaRepository<Comment, Integer> {
    @Query(nativeQuery = true, name = "CommentForItem")
    List<CommentDto> findAllByItemId(int itemId);
}
