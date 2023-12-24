package ru.practicum.shareit.request.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.request.dto.*;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestStorage;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.validation.exceptions.ObjectNotFoundException;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Qualifier("ItemRequestServiceImpl")
@Transactional
@Service
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestStorage itemRequestStorage;
    private final ItemRequestMapper itemRequestMapper;
    private final UserService userService;
    private final ItemStorage itemStorage;

    public ItemRequestServiceImpl(ItemRequestStorage itemRequestStorage,
                                  ItemRequestMapper itemRequestMapper,
                                  @Qualifier("UserServiceImpl") UserService userService,
                                  ItemStorage itemStorage) {
        this.itemRequestStorage = itemRequestStorage;
        this.itemRequestMapper = itemRequestMapper;
        this.userService = userService;
        this.itemStorage = itemStorage;
    }

    @Override
    public ItemRequestDto addItemRequest(int userId, ItemRequestDto itemRequestDto) {
        checkUser(userId);

        User foundedUser = userService.getUserEntityById(userId);
        ItemRequest itemRequest = itemRequestMapper.toItemRequest(itemRequestDto, foundedUser);

        return itemRequestMapper.toItemRequestDto(itemRequestStorage.save(itemRequest));
    }

    /**
     * getItemRequestEntityById only for mapping on services
     */
    @Override
    public ItemRequest getItemRequestEntityById(int requestId) {
        return getItemRequest(requestId);
    }

    @Override
    public List<ItemRequestSimplifiedDto> getAllYourItemRequest(int userId) {
        checkUser(userId);

        List<ItemRequestSimplified> itemRequests = itemRequestStorage.findAllByRequesterId(userId);

        if (itemRequests.isEmpty()) {
            return new ArrayList<>();
        } else {
            List<ItemRequestSimplifiedDto> response = new ArrayList<>();

            List<ItemRequestSimplifiedDto> itemRequestSimplifiedDto = itemRequests.stream()
                    .map(itemRequestMapper::toItemRequestSimplifiedDto)
                    .collect(Collectors.toList());

            List<ShortItem> items = itemStorage.findAllByRequesterId(userId);

            if (items.isEmpty()) {
                return itemRequestSimplifiedDto;
            } else {
                List<ShortItemDto> itemsDto = items.stream()
                        .map(itemRequestMapper::toShortItemDto)
                        .collect(Collectors.toList());

                Map<Integer, List<ShortItemDto>> shortItemsDto = itemsDto.stream()
                        .collect(Collectors.groupingBy(ShortItemDto::getRequestId));

                for (ItemRequestSimplifiedDto itemRequest : itemRequestSimplifiedDto) {
                    itemRequest.setItems(shortItemsDto.get(itemRequest.getId()));
                    response.add(itemRequest);
                }

                return response;
            }
        }
    }

    @Override
    public ItemRequestSimplifiedDto getItemRequestById(int userId, int requestId) {
        checkUser(userId);

        ItemRequestSimplified foundedItemRequest = itemRequestStorage.getItemRequestById(requestId);

        if (foundedItemRequest == null) {
            throw new ObjectNotFoundException("itemRequest id - " + requestId + " not found");
        } else {
            List<ShortItemDto> shortItemsDto = itemStorage.findAllByRequestId(requestId).stream()
                    .map(itemRequestMapper::toShortItemDto)
                    .collect(Collectors.toUnmodifiableList());

            ItemRequestSimplifiedDto response = itemRequestMapper.toItemRequestSimplifiedDto(foundedItemRequest);
            response.setItems(shortItemsDto);

            return response;
        }
    }

    @Override
    public List<ItemRequestSimplifiedDto> getAllItemRequestPageable(int userId, Integer from, Integer size) {
        checkUser(userId);

        List<ItemRequestSimplified> itemRequestsSimplified = itemRequestStorage.findAllBy(PageRequest.of(from / size,
                size, Sort.by(Sort.Direction.DESC, "created"))).getContent();

        if (itemRequestsSimplified.isEmpty()) {
            return new ArrayList<>();
        } else {

            List<ItemRequestSimplifiedDto> itemRequestsSimplifiedDto = itemRequestsSimplified.stream()
                    .map(itemRequestMapper::toItemRequestSimplifiedDto)
                    .collect(Collectors.toList());

            List<Item> items = itemStorage.findAll();

            if (items.isEmpty()) {
                return itemRequestsSimplifiedDto;
            } else {
                List<ShortItemDto> shortItems = items.stream()
                        .filter(x -> x.getRequest() != null)
                        .map(itemRequestMapper::toShortItemDto)
                        .collect(Collectors.toList());

                List<ItemRequestSimplifiedDto> response = new ArrayList<>();

                Map<Integer, List<ShortItemDto>> shortItemsDto = shortItems.stream()
                        .collect(Collectors.groupingBy(ShortItemDto::getRequestId));

                for (ItemRequestSimplifiedDto itemRequest : itemRequestsSimplifiedDto) {
                    itemRequest.setItems(shortItemsDto.get(itemRequest.getId()));
                    response.add(itemRequest);
                }

                return response.stream()
                        .filter(x -> x.getRequester().getId() != userId)
                        .collect(Collectors.toList());
            }
        }
    }

    private void checkUser(Integer userId) {
        UserDto foundUser = userService.getUserById(userId);

        if (foundUser == null || foundUser.getEmail() == null) {
            throw new ObjectNotFoundException("user id - " + userId + " not found");
        }
    }

    private ItemRequest getItemRequest(int requestId) {
        return itemRequestStorage.findById(requestId)
                .orElseThrow(() -> new ObjectNotFoundException("itemRequest id - " + requestId + " not found"));
    }
}