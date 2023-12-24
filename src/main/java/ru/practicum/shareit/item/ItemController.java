package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingCommentDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.service.CommentServiceImpl;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import javax.validation.constraints.*;
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
    private final CommentServiceImpl commentServiceImpl;
    private static final String UserId = "X-Sharer-User-Id";
    private static final String ItemId = "/{itemId}";
    private static final String Search = "/search";
    private static final String Comment = "/comment";

    @Autowired
    ItemController(@Qualifier("ItemServiceImpl") ItemService itemService, CommentServiceImpl commentServiceImpl) {
        this.itemService = itemService;
        this.commentServiceImpl = commentServiceImpl;
    }

    @PostMapping
    public ItemDto addItem(@Positive @RequestHeader(UserId) Integer userId, @Valid @RequestBody ItemDto itemDto) {
        log.info("request POST/addItem : {}, {}", userId, itemDto);

        ItemDto responseObject = itemService.addItem(userId, itemDto);
        log.info("response POST/addItem : {}", responseObject);

        return responseObject;
    }

    @PostMapping(ItemId + Comment)
    public CommentDto addComment(@Positive @RequestHeader(UserId) Integer userId, @PathVariable int itemId,
                                 @Valid @RequestBody CommentDto commentDto) {
        log.info("request POST/addComment : {}, {}, {}", userId, itemId, commentDto);

        CommentDto responseObject = commentServiceImpl.addComment(userId, itemId, commentDto);
        log.info("response POST/addComment : {}", responseObject);

        return responseObject;
    }

    @GetMapping(ItemId)
    public ItemWithBookingCommentDto getItemById(@Positive @RequestHeader(UserId) Integer userId,
                                                 @PathVariable int itemId) {
        log.info("request GET/getItemById : {}, {}", userId, itemId);

        ItemWithBookingCommentDto responseObject = itemService.getItemById(userId, itemId);
        log.info("response GET/getItemById : {}", responseObject);

        return responseObject;
    }

    @GetMapping
    public List<ItemWithBookingDto> getItemsByOwnerId(@Positive @RequestHeader(UserId) Integer userId,
                @RequestParam(value = "from", required = false, defaultValue = "0") @Min(0) Integer from,
                @RequestParam(value = "size", required = false, defaultValue = "20") @Min(1) @Max(20) Integer size) {
        log.info("request GET/getAllItemsByUserId: {}, {}, {}", userId, from, size);

        List<ItemWithBookingDto> responseObject = itemService.getItemsByOwnerId(userId, from, size);
        log.info("response GET/getAllItemsByUserId: {}", responseObject);

        return responseObject;
    }

    @GetMapping(Search)
    public List<ItemDto> getItemByString(@Positive @RequestHeader(UserId) Integer userId,
                  @RequestParam String text,
                  @RequestParam(value = "from", required = false, defaultValue = "0") @Min(0) Integer from,
                  @RequestParam(value = "size", required = false, defaultValue = "20") @Min(1) @Max(20) Integer size) {

        log.info("request GET/getItemByString: {}, {}, {}, {}", userId, text, from, size);

        List<ItemDto> responseObject = itemService.getItemByString(userId, text, from, size);
        log.info("response GET/getItemByString: {}", responseObject);

        return responseObject;
    }

    @PatchMapping(ItemId)
    public ItemDto update(@Positive @RequestHeader(UserId) Integer userId,
                       @PathVariable int itemId,
                       @RequestBody ItemDto itemDto) {
        log.info("request PATCH/update: {}, {}, {}", userId, itemId, itemDto);

        ItemDto responseObject = itemService.update(userId, itemId, itemDto);
        log.info("request PATCH/update: {}", responseObject);
        return responseObject;
    }
}
