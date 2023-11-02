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
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.db.CommentStorage;
import ru.practicum.shareit.item.storage.db.ItemStorage;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.validation.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.validation.exceptions.ValidateException;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

    @Autowired
    ItemServiceImpl(ItemMapper itemMapper, ItemStorage itemStorage,
                    @Qualifier("UserServiceImpl") UserService userService,
                    CommentMapper commentMapper,
                    BookingStorage bookingStorage,
                    CommentStorage commentStorage) {
        this.itemMapper = itemMapper;
        this.itemStorage = itemStorage;
        this.userService = userService;
        this.bookingStorage = bookingStorage;
        this.commentStorage = commentStorage;
    }

    @Override
    public ItemDto addItem(int userId, ItemDto itemDto) {
        checkUsers(userId);

        Item newItem = itemMapper.toItem(itemDto);
        newItem.setOwnerId(userId);

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

        if (foundedItem.getOwnerId() == userId) {
            foundBookingForItem(itemWithBookingDto);
        }

        ItemWithBookingCommentDto itemWithBookingCommentDto = itemMapper.toItemWithBookingCommentDto(itemWithBookingDto);

        itemWithBookingCommentDto.setComments(commentStorage.findAllByItemId(itemId));

        return itemWithBookingCommentDto;
    }

    @Override
    public List<ItemWithBookingDto> getItemsByOwnerId(int userId) {
        checkUsers(userId);

        List<ItemWithBookingDto> allItems = itemStorage.findByOwnerId(userId).stream()
                .map(itemMapper::toItemWithBookingDto)
                .collect(Collectors.toList());

        List<ItemWithBookingDto> response = new ArrayList<>();

        for (ItemWithBookingDto item : allItems) {
            response.add(foundBookingForItem(item));
        }

        return response;
    }

    @Override
    public List<ItemDto> getItemByString(int userId, String text) {
        checkUsers(userId);

        if (text.isBlank() || text.isEmpty()) {
            return new ArrayList<>();
        }

        return itemMapper.toItemDtoList(itemStorage.search(text));
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
        Optional<Item> foundItem = itemStorage.findById(itemId);

        if (foundItem.isPresent()) {
            return foundItem.get();
        } else {
            throw new ObjectNotFoundException("item id - " + itemId + " not found");
        }
    }

    private ItemWithBookingDto foundBookingForItem(ItemWithBookingDto foundedItem) {
        List<BookingForItemDto> foundedBooking =
                bookingStorage.findAllByItemIdAndStatus(foundedItem.getId(), StatusBooking.APPROVED.ordinal());

        LocalDateTime thisMoment = LocalDateTime.now();
        long lDifference = 1000000000000000000L;
        long nDifference = 1000000000000000000L;
        long difference;
        Duration diff;

        for (int i = 0; i < foundedBooking.size(); i++) {
            if (foundedBooking.get(i).getStart().isBefore(thisMoment) && foundedBooking.get(i).getEnd().isAfter(thisMoment)) {
                foundedItem.setLastBooking(foundedBooking.get(i));

                if (i + 1 < foundedBooking.size()) {
                    foundedItem.setNextBooking(foundedBooking.get(i + 1));
                }
                return foundedItem;
            } else {
                if (foundedBooking.get(i).getEnd().isBefore(thisMoment)) {
                    diff = Duration.between(thisMoment, foundedBooking.get(i).getEnd());
                    difference = Math.abs(diff.getSeconds());
                    if (difference < lDifference) {
                        lDifference = difference;
                        foundedItem.setLastBooking(foundedBooking.get(i));
                    }
                } else {
                    diff = Duration.between(thisMoment, foundedBooking.get(i).getStart());
                    difference = Math.abs(diff.getSeconds());
                    if (difference < nDifference) {
                        nDifference = difference;
                        foundedItem.setNextBooking(foundedBooking.get(i));
                    }
                    if (i + 1 == foundedBooking.size()) {
                        return foundedItem;
                    }
                }
            }
        }
        return foundedItem;
    }

}
