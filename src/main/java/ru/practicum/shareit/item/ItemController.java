package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Validated
@Slf4j
@RestController
@RequestMapping(path = "/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public Item addItem(@RequestHeader("X-Sharer-User-Id") long userId, @Valid @RequestBody ItemDto itemDto) {
        log.info("request POST/addItem : {}, {}", userId, itemDto);

        Item responseObject = itemService.addItem(userId, itemDto);
        log.info("response POST/addItem : {}", responseObject);

        return responseObject;
    }

    @GetMapping("/{itemId}")
    public Item getItemById(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long itemId) {
        log.info("request GET/getItemById : {}, {}", userId, itemId);

        Item responseObject = itemService.getItemById(userId, itemId);
        log.info("response GET/getItemById : {}", responseObject);

        return responseObject;
    }

    @GetMapping
    public List<Item> getItemsByUserId(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("request GET/getAllItemsByUserId: {}", userId);

        List<Item> responseObject = itemService.getItemsByOwnerId(userId);
        log.info("response GET/getAllItemsByUserId: {}", responseObject);

        return responseObject;
    }

    @GetMapping("/search")
    public List<Item> getItemByString(@RequestHeader("X-Sharer-User-Id") long userId, @RequestParam String text) {
        log.info("request GET/getItemByString: {}, {}", userId, text);

        List<Item> responseObject = itemService.getItemByString(userId, text);
        log.info("response GET/getItemByString: {}", responseObject);

        return responseObject;
    }

    @PatchMapping("/{itemId}")
    public Item update(@RequestHeader("X-Sharer-User-Id") long userId,
                       @PathVariable long itemId,
                       @RequestBody ItemDto itemDto) {
        log.info("request PATCH/update: {}, {}, {}", userId, itemId, itemDto);

        Item responseObject = itemService.update(userId, itemId, itemDto);
        log.info("request PATCH/update: {}", responseObject);
        return responseObject;
    }
}
