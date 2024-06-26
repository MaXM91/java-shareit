package ru.practicum.shareit.request.storage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.dto.ItemRequestSimplified;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

@Repository
public interface ItemRequestStorage extends JpaRepository<ItemRequest, Integer> {
    List<ItemRequestSimplified> findAllByRequesterId(int userId);

    Page<ItemRequestSimplified> findAllBy(Pageable pageable);

    ItemRequestSimplified getItemRequestById(int requestId);
}
