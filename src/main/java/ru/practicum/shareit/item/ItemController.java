package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.service.ItemService;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/items")
public class ItemController {
    ItemService itemService;

    @Autowired
    ItemController(@Qualifier("InMemoryItemService") ItemService itemService) {
        this.itemService = itemService;
    }
}
