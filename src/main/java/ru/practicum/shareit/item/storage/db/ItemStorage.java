package ru.practicum.shareit.item.storage.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage extends JpaRepository<Item, Integer> {
    @Query("SELECT i" +
        "   FROM Item i " +
        "   WHERE (upper(i.name) like upper(concat('%', ?1, '%')) and i.available = true" +
        "   or upper(i.description) like upper(concat('%', ?1, '%'))) and i.available = true")
    List<Item> search(String text);

    List<Item> findByOwnerId(int ownerId);
}
