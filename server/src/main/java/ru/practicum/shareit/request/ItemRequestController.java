package ru.practicum.shareit.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestSimplifiedDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

/**
 * Controller for work with User item-requests.
 */
@Slf4j
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private static final String UserId = "X-Sharer-User-Id";
    private static final String RequestId = "/{requestId}";
    private final ItemRequestService itemRequestService;

    ItemRequestController(@Qualifier("ItemRequestServiceImpl") ItemRequestService itemRequestService) {
        this.itemRequestService = itemRequestService;
    }

    @PostMapping
    public ItemRequestDto addItemRequest(@RequestHeader(UserId) Integer userId,
                                         @RequestBody ItemRequestDto itemRequestDto) {
        log.info("request POST/addItemRequest : {}, {}", userId, itemRequestDto);

        ItemRequestDto responseObject = itemRequestService.addItemRequest(userId, itemRequestDto);
        log.info("response POST/addItemRequest : {}", responseObject);

        return responseObject;
    }

    @GetMapping
    public List<ItemRequestSimplifiedDto> getAllYourItemRequest(@RequestHeader(UserId) Integer userId) {
        log.info("request GET/getYourItemRequest : {}", userId);

        List<ItemRequestSimplifiedDto> responseObject = itemRequestService.getAllYourItemRequest(userId);
        log.info("response GET/getYourItemRequest : {}", responseObject);

        return responseObject;
    }

    @GetMapping(RequestId)
    public ItemRequestSimplifiedDto getItemRequestById(@RequestHeader(UserId) Integer userId,
                                             @PathVariable("requestId") Integer requestId) {
        log.info("request GET/getItemRequestById : {}, {}", userId, requestId);

        ItemRequestSimplifiedDto responseObject = itemRequestService.getItemRequestById(userId, requestId);
        log.info("response GET/getItemRequestById : {}", responseObject);

        return responseObject;
    }

    @GetMapping("/all")
    public List<ItemRequestSimplifiedDto> getAllItemRequestOtherUser(@RequestHeader(UserId) Integer userId,
            Integer from, Integer size) {
        log.info("request GET/getAllItemRequestOtherUser : {}, {}, {}", userId, from, size);

        List<ItemRequestSimplifiedDto> responseObject = itemRequestService.getAllItemRequestPageable(userId, from, size);
        log.info("response GET/getAllItemRequestOtherUser : {}", responseObject);

        return responseObject;
    }
}
