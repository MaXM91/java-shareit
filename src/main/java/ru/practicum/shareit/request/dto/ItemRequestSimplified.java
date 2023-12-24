package ru.practicum.shareit.request.dto;

import java.time.LocalDateTime;
import java.util.List;

public interface ItemRequestSimplified {

    int getId();

    String getDescription();

    ShortUser getRequester();

    LocalDateTime getCreated();

    List<ShortItem> getItems();
}
