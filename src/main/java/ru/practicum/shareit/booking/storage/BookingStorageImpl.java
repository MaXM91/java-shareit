package ru.practicum.shareit.booking.storage;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Qualifier("BookingStorageImpl")
@Repository
public class BookingStorageImpl implements BookingStorage {
}
