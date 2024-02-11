package ru.practicum.shareit.request.dto;

import org.springframework.beans.factory.annotation.Value;

public interface ShortItem {
    int getId();

    String getName();

    String getDescription();

    boolean getAvailable();

    @Value("#{target.request.id}")
    int getRequestId();

}
