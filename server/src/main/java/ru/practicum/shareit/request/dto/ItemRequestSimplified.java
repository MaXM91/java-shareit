package ru.practicum.shareit.request.dto;

import java.time.LocalDateTime;

public interface ItemRequestSimplified {
    int getId();

    String getDescription();

    ShortUser getRequester();

    LocalDateTime getCreated();
}
