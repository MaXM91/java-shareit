package ru.practicum.shareit.item.storage;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Qualifier("ItemStorageImpl")
@Repository
public class ItemStorageImpl implements ItemStorage {
}
