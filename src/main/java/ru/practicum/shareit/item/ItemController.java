package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

/**
 * Controller for work with Item entity.
 */
@Validated
@Slf4j
@RestController
@RequestMapping(path = "/items")
public class ItemController {
    private final ItemService itemService;

    ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ItemDto addItem(@RequestHeader("X-Sharer-User-Id") Integer userId, @Valid @RequestBody ItemDto itemDto) {
        log.info("request POST/addItem : {}, {}", userId, itemDto);

        ItemDto responseObject = itemService.addItem(userId, itemDto);
        log.info("response POST/addItem : {}", responseObject);

        return responseObject;
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@RequestHeader("X-Sharer-User-Id") Integer userId, @PathVariable int itemId) {
        log.info("request GET/getItemById : {}, {}", userId, itemId);

        ItemDto responseObject = itemService.getItemById(userId, itemId);
        log.info("response GET/getItemById : {}", responseObject);

        return responseObject;
    }

    @GetMapping
    public List<ItemDto> getItemsByUserId(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        log.info("request GET/getAllItemsByUserId: {}", userId);

        List<ItemDto> responseObject = itemService.getItemsByOwnerId(userId);
        log.info("response GET/getAllItemsByUserId: {}", responseObject);

        return responseObject;
    }

    @GetMapping("/search")
    public List<ItemDto> getItemByString(@RequestHeader("X-Sharer-User-Id") Integer userId, @RequestParam String text) {
        log.info("request GET/getItemByString: {}, {}", userId, text);

        List<ItemDto> responseObject = itemService.getItemByString(userId, text);
        log.info("response GET/getItemByString: {}", responseObject);

        return responseObject;
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader("X-Sharer-User-Id") Integer userId,
                       @PathVariable int itemId,
                       @RequestBody ItemDto itemDto) {
        log.info("request PATCH/update: {}, {}, {}", userId, itemId, itemDto);

        ItemDto responseObject = itemService.update(userId, itemId, itemDto);
        log.info("request PATCH/update: {}", responseObject);
        return responseObject;
    }
}
