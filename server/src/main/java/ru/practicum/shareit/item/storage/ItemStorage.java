package ru.practicum.shareit.item.storage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ShortItem;

import java.util.List;

@Repository
public interface ItemStorage extends JpaRepository<Item, Integer> {
    @Query("SELECT i" +
        "   FROM Item i " +
        "   WHERE (upper(i.name) like upper(concat('%', ?1, '%')) and i.available = true" +
        "   or upper(i.description) like upper(concat('%', ?1, '%'))) and i.available = true")
    Page<Item> search(String text, Pageable pageable);

    Page<Item> findByOwnerId(int ownerId, Pageable pageable);

    @Query("SELECT i " +
           "FROM Item i " +
           "JOIN i.request r " +
           "JOIN r.requester AS u " +
           "WHERE u.id = ?1 ")
    List<ShortItem> findAllByRequesterId(int userId);

    @Query("SELECT i " +
            "FROM Item i " +
            "JOIN i.request r " +
            "WHERE r.id = ?1 ")
    List<ShortItem> findAllByRequestId(int requestId);
}
