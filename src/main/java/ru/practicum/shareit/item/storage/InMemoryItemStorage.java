package ru.practicum.shareit.item.storage;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Qualifier("InMemoryItemStorage")
@Repository
public class InMemoryItemStorage implements ItemStorage {
}
