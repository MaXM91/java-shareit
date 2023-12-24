package ru.practicum.shareit.request.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestSimplified;
import ru.practicum.shareit.request.dto.ItemRequestSimplifiedDto;
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
import java.util.stream.Collectors;

@Qualifier("ItemRequestServiceImpl")
@Transactional
@Service
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestStorage itemRequestStorage;
    private final ItemRequestMapper itemRequestMapper;
    private final UserService userService;

    public ItemRequestServiceImpl(ItemRequestStorage itemRequestStorage,
                                  ItemRequestMapper itemRequestMapper,
                                  @Qualifier("UserServiceImpl") UserService userService) {
        this.itemRequestStorage = itemRequestStorage;
        this.itemRequestMapper = itemRequestMapper;
        this.userService = userService;
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

        List<ItemRequestSimplified> itemRequest = itemRequestStorage.findAllByRequesterId(userId);

        if (itemRequest.isEmpty()) {
            return new ArrayList<>();
        } else {
            return itemRequest.stream()
                              .map(itemRequestMapper::toItemRequestSimplifiedDto)
                              .collect(Collectors.toList());
        }
    }

    @Override
    public ItemRequestSimplifiedDto getItemRequestById(int userId, int requestId) {
        checkUser(userId);

        ItemRequestSimplified foundedItemRequest = itemRequestStorage.getItemRequestById(requestId);

        if (foundedItemRequest == null) {
            throw new ObjectNotFoundException("itemRequest id - " + requestId + " not found");
        } else {
            return itemRequestMapper.toItemRequestSimplifiedDto(foundedItemRequest);
        }
    }

    @Override
    public List<ItemRequestSimplifiedDto> getAllItemRequestPageable(int userId, Integer from, Integer size) {
        checkUser(userId);

        if (from == null && size == null) {
            return new ArrayList<>();
        } else {
            return itemRequestStorage.findAllBy(PageRequest.of(from,
                            size, Sort.by(Sort.Direction.DESC, "created"))).stream()
                    .filter(x -> x.getRequester().getId() != userId)
                    .map(itemRequestMapper::toItemRequestSimplifiedDto)
                    .collect(Collectors.toList());
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