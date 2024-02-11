package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;

/**
 * Controller for work with Item entity.
 */
@Validated
@Slf4j
@RestController
@RequestMapping(path = "/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemClient itemClient;
    private static final String UserId = "X-Sharer-User-Id";
    private static final String ItemId = "/{itemId}";
    private static final String Search = "/search";
    private static final String Comment = "/comment";

    @PostMapping
    public ResponseEntity<Object> addItem(@Positive @RequestHeader(UserId) Long userId, @Valid @RequestBody ItemDto itemDto) {
        log.info("request POST/addItem : {}, {}", userId, itemDto);

        return itemClient.addItem(userId, itemDto);
    }

    @PostMapping(ItemId + Comment)
    public ResponseEntity<Object> addComment(@Positive @RequestHeader(UserId) Long userId, @PathVariable Long itemId,
                                 @Valid @RequestBody CommentDto commentDto) {
        log.info("request POST/addComment : {}, {}, {}", userId, itemId, commentDto);

        return itemClient.addComment(userId, itemId, commentDto);
    }

    @GetMapping(ItemId)
    public ResponseEntity<Object> getItemById(@Positive @RequestHeader(UserId) Long userId,
                                                 @PathVariable Long itemId) {
        log.info("request GET/getItemById : {}, {}", userId, itemId);

        return itemClient.getItemById(userId, itemId);
    }

    @GetMapping
    public ResponseEntity<Object> getItemsByOwnerId(@Positive @RequestHeader(UserId) Long userId,
                                                      @RequestParam(value = "from", defaultValue = "0") @Min(0) Integer from,
                                                      @RequestParam(value = "size", defaultValue = "20") @Min(1) @Max(20) Integer size) {
        log.info("request GET/getAllItemsByUserId: {}, {}, {}", userId, from, size);

        return itemClient.getItemsByOwnerId(userId, from, size);
    }

    @GetMapping(Search)
    public ResponseEntity<Object> getItemByString(@Positive @RequestHeader(UserId) Long userId,
                                         @RequestParam String text,
                                         @RequestParam(value = "from", defaultValue = "0") @Min(0) Integer from,
                                         @RequestParam(value = "size", defaultValue = "20") @Min(1) @Max(20) Integer size) {

        log.info("request GET/getItemByString: {}, {}, {}, {}", userId, text, from, size);

        return itemClient.getItemByString(userId, text, from, size);
    }

    @PatchMapping(ItemId)
    public ResponseEntity<Object> update(@Positive @RequestHeader(UserId) Long userId,
                          @PathVariable int itemId,
                          @RequestBody ItemDto itemDto) {
        log.info("request PATCH/update: {}, {}, {}", userId, itemId, itemDto);

        return itemClient.update(userId, itemId, itemDto);
    }
}