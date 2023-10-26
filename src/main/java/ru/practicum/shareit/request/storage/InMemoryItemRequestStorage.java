package ru.practicum.shareit.request.storage;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Qualifier("InMemoryItemRequestStorage")
@Repository
public class InMemoryItemRequestStorage implements ItemRequestStorage {
}
