package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.StatusBooking;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.item.dto.BookingForItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingCommentDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.CommentStorage;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.validation.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.validation.exceptions.ValidateException;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@Qualifier("ItemServiceImpl")
@Service
public class ItemServiceImpl implements ItemService {
    private final ItemMapper itemMapper;
    private final ItemStorage itemStorage;
    private final UserService userService;
    private final BookingStorage bookingStorage;
    private final CommentStorage commentStorage;
    private final ItemRequestService itemRequestService;

    @Autowired
    ItemServiceImpl(ItemMapper itemMapper, ItemStorage itemStorage,
                    @Qualifier("UserServiceImpl") UserService userService,
                    BookingStorage bookingStorage,
                    CommentStorage commentStorage,
                    @Qualifier("ItemRequestServiceImpl") ItemRequestService itemRequestService) {
        this.itemMapper = itemMapper;
        this.itemStorage = itemStorage;
        this.userService = userService;
        this.bookingStorage = bookingStorage;
        this.commentStorage = commentStorage;
        this.itemRequestService = itemRequestService;
    }

    @Override
    public ItemDto addItem(int userId, ItemDto itemDto) {
        checkUsers(userId);

        Item newItem = itemMapper.toItem(itemDto);
        newItem.setOwnerId(userId);

        if (itemDto.getRequestId() != null) {
            newItem.setRequest(itemRequestService.getItemRequestEntityById(itemDto.getRequestId()));
        }

        return itemMapper.toItemDto(itemStorage.save(newItem));
    }

    /**
     * getItemEntityById only for mapping on services
     */
    @Override
    public Item getItemEntityById(Integer itemId) {
        return getItem(itemId);
    }

    @Override
    public ItemWithBookingCommentDto getItemById(int userId, int itemId) {
        checkUsers(userId);

        Item foundedItem = getItem(itemId);
        ItemWithBookingDto itemWithBookingDto = itemMapper.toItemWithBookingDto(foundedItem);
        List<BookingForItemDto> bookings = bookingStorage.findAllByItemIdAndStatus(foundedItem.getId(),
                StatusBooking.APPROVED.ordinal());

        if (foundedItem.getOwnerId() == userId) {
            foundBookingForItem(itemWithBookingDto, bookings);
        }

        ItemWithBookingCommentDto itemWithBookingCommentDto = itemMapper.toItemWithBookingCommentDto(itemWithBookingDto);

        itemWithBookingCommentDto.setComments(commentStorage.findAllByItemId(itemId));

        return itemWithBookingCommentDto;
    }

    @Override
    public List<ItemWithBookingDto> getItemsByOwnerId(int userId, Integer from, Integer size) {
        checkUsers(userId);

        List<ItemWithBookingDto> allItems;

        if (from == null && size == null) {
            allItems = itemStorage.findByOwnerId(userId).stream()
                    .map(itemMapper::toItemWithBookingDto)
                    .collect(Collectors.toList());
        } else {
            allItems = itemStorage.findByOwnerId(userId).stream()
                    .skip(from)
                    .limit(size)
                    .map(itemMapper::toItemWithBookingDto)
                    .collect(Collectors.toList());
        }
        List<BookingForItemDto> bookings = bookingStorage.findAllByItemOwnerIdAndStatus(userId,
                StatusBooking.APPROVED.ordinal());

        List<ItemWithBookingDto> response = new ArrayList<>();

        for (ItemWithBookingDto item : allItems) {
            response.add(foundBookingForItem(item, bookings.stream()
                                                           .filter(x -> x.getItemId() == item.getId())
                                                           .collect(Collectors.toList())));
        }
        return response;
    }

    @Override
    public List<ItemDto> getItemByString(int userId, String text, Integer from, Integer size) {
        checkUsers(userId);

        if (text.isBlank() || text.isEmpty()) {
            return new ArrayList<>();
        }

        List<Item> foundedItem = itemStorage.search(text);

        if (from == null && size == null) {
            return itemMapper.toItemDtoList(foundedItem);
        } else {
            return foundedItem.stream()
                              .skip(from)
                              .limit(size)
                              .map(itemMapper::toItemDto)
                              .collect(Collectors.toList());
        }
    }

    @Override
    public ItemDto update(int userId, int itemId, ItemDto itemDto) {
        checkUsers(userId);

        Item newItem = itemMapper.toItem(itemDto);

        Item saveItem = getItem(itemId);

        if (saveItem.getOwnerId() != userId) {
            throw new ValidateException("item id - " + itemId + " have other ownerId - " + saveItem.getOwnerId());
        }

        if (newItem.getName() != null && !newItem.getName().isEmpty() && !newItem.getName().isBlank()
                && !newItem.getName().equals(saveItem.getName())) {
            saveItem.setName(newItem.getName());
        }

        if (newItem.getDescription() != null && !newItem.getDescription().isEmpty() && !newItem.getDescription().isBlank()
                && !newItem.getDescription().equals(saveItem.getDescription())) {
            saveItem.setDescription(newItem.getDescription());
        }

        if (newItem.getAvailable() != null && !newItem.getAvailable().equals(saveItem.getAvailable())) {
            saveItem.setAvailable(newItem.getAvailable());
        }

        return itemMapper.toItemDto(itemStorage.save(saveItem));
    }

    @Override
    public void delete(int userId, Item item) {
        checkUsers(userId);

        itemStorage.delete(item);
    }

    private void checkUsers(int userId) {
        userService.getUserById(userId);
    }

    private Item getItem(int itemId) {
        return itemStorage.findById(itemId)
                .orElseThrow(() -> new ObjectNotFoundException("item id - " + itemId + " not found"));
    }

    private ItemWithBookingDto foundBookingForItem(ItemWithBookingDto foundedItem, List<BookingForItemDto> bookings) {
        LocalDateTime thisMoment = LocalDateTime.now();

        List<BookingForItemDto> lastBooking;
        List<BookingForItemDto> nextBooking;

        lastBooking = bookings.stream()
                .filter(x -> ((x.getStart().isBefore(thisMoment) || x.getStart().isEqual(thisMoment)) &&
                        (x.getEnd().isAfter(thisMoment) || x.getEnd().isEqual(thisMoment))) || x.getEnd().isBefore(thisMoment))
                .sorted(Comparator.comparing(BookingForItemDto::getEnd).reversed())
                .limit(1)
                .collect(Collectors.toList());

        if (!lastBooking.isEmpty()) {
            foundedItem.setLastBooking(lastBooking.get(0));
        }

        nextBooking = bookings.stream()
                    .filter(x -> x.getStart().isAfter(thisMoment) &&
                            x.getEnd().isAfter(x.getStart()))
                    .sorted(Comparator.comparing(BookingForItemDto::getStart))
                    .limit(1)
                    .collect(Collectors.toList());

        if (!nextBooking.isEmpty()) {
           foundedItem.setNextBooking(nextBooking.get(0));
        }

     return foundedItem;
    }
}