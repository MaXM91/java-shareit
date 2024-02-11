package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;

/**
 * Controller for work with User item-requests.
 */
@Slf4j
@Validated
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;
    private static final String UserId = "X-Sharer-User-Id";
    private static final String RequestId = "/{requestId}";

    @PostMapping
    public ResponseEntity<Object> addItemRequest(@Positive @RequestHeader(UserId) Long userId,
                                                 @Valid @RequestBody ItemRequestDto itemRequestDto) {
        log.info("request POST/addItemRequest : {}, {}", userId, itemRequestDto);

        return itemRequestClient.addItemRequest(userId, itemRequestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getAllYourItemRequest(@Positive @RequestHeader(UserId) Long userId) {
        log.info("request GET/getYourItemRequest : {}", userId);

        return itemRequestClient.getAllYourItemRequest(userId);
    }

    @GetMapping(RequestId)
    public ResponseEntity<Object> getItemRequestById(@Positive @RequestHeader(UserId) Long userId,
                                                       @Positive @PathVariable("requestId") Long requestId) {
        log.info("request GET/getItemRequestById : {}, {}", userId, requestId);

        return itemRequestClient.getItemRequestById(userId, requestId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllItemRequestOtherUser(@Positive @RequestHeader(UserId) Long userId,
                  @RequestParam(value = "from", defaultValue = "0") @Min(0) Integer from,
                  @RequestParam(value = "size", defaultValue = "20") @Min(1) @Max(20) Integer size) {
        log.info("request GET/getAllItemRequestOtherUser : {}, {}, {}", userId, from, size);

        return itemRequestClient.getAllItemRequestOtherUser(userId, from, size);
    }
}