package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.request.service.ItemRequestService;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    ItemRequestService itemRequestService;

    ItemRequestController(@Qualifier("InMemoryItemRequestService") ItemRequestService itemRequestService) {
        this.itemRequestService = itemRequestService;
    }
}
