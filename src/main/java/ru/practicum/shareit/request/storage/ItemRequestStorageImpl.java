package ru.practicum.shareit.request.storage;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Qualifier("ItemRequestStorageImpl")
@Repository
public class ItemRequestStorageImpl implements ItemRequestStorage {
}
