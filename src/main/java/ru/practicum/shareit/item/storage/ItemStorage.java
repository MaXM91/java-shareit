package ru.practicum.shareit.item.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface ItemStorage extends JpaRepository<Item, Integer> {
    @Query("SELECT i" +
        "   FROM Item i " +
        "   WHERE (upper(i.name) like upper(concat('%', ?1, '%')) and i.available = true" +
        "   or upper(i.description) like upper(concat('%', ?1, '%'))) and i.available = true")
    List<Item> search(String text);

    List<Item> findByOwnerId(int ownerId);
}
