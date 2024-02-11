package ru.practicum.shareit.booking;

public enum StatusBooking {
    WAITING,                                                                   // Новое бронирование ожидает одобрения
    APPROVED,                                                                  // Бронирование подтверждено владельцем
    REJECTED,                                                                  // Бронирование отклонено владельцем
    CANCELED                                                                   // Бронирование отменено создателем
}