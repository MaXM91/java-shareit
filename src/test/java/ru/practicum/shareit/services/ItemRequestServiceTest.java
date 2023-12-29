package ru.practicum.shareit.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.shareit.request.dto.*;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.request.storage.ItemRequestStorage;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceImpl;
import ru.practicum.shareit.validation.exceptions.ObjectNotFoundException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@SpringBootTest
@ContextConfiguration(classes = {ItemRequestServiceImpl.class, ItemRequestMapper.class})
public class ItemRequestServiceTest {
    @SpyBean
    private ItemRequestMapper itemRequestMapper;
    @Autowired
    private ItemRequestServiceImpl itemRequestService;
    @MockBean
    private ItemRequestStorage itemRequestStorage;
    @MockBean
    private UserServiceImpl userService;

    @Test
    void addItemRequestTest() {
        final UserDto userDto1 = new UserDto(1, "name1", "email1@mail.ru");
        final User user1 = new User(1, "email1@mail.ru", "name1");
        final LocalDateTime itemRequestTime1 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        final ItemRequestDto itemRequestDtoBefore1 = new ItemRequestDto(null,
                "itemRequestDescription1", null, itemRequestTime1, null);

        final ItemRequest itemRequestAfter1 = new ItemRequest(1, "itemRequestDescription1", user1,
                itemRequestTime1, null);
        final ItemRequestDto itemRequestDtoAfter1 = new ItemRequestDto(1,
                "itemRequestDescription1", 1, itemRequestTime1, new ArrayList<>());


        when(userService.getUserById(anyInt()))
                .thenReturn(userDto1);
        when(userService.getUserEntityById(anyInt()))
                .thenReturn(user1);
        when(itemRequestStorage.save(any(ItemRequest.class)))
                .thenReturn(itemRequestAfter1);

        ItemRequestDto actualItemRequest = itemRequestService.addItemRequest(1, itemRequestDtoBefore1);

        Assertions.assertEquals(itemRequestDtoAfter1.toString(), actualItemRequest.toString());
        Assertions.assertEquals(itemRequestMapper.toItemRequest(itemRequestDtoAfter1, user1).toString(),
                itemRequestMapper.toItemRequest(actualItemRequest, user1).toString());

        Mockito.verify(userService, Mockito.times(1)).getUserById(anyInt());
        Mockito.verify(userService, Mockito.times(1)).getUserEntityById(anyInt());
        Mockito.verify(itemRequestStorage, Mockito.times(1)).save(any(ItemRequest.class));

        Mockito.verify(itemRequestMapper, Mockito.times(3)).toItemRequest(any(), any());
        Mockito.verify(itemRequestMapper, Mockito.times(1)).toItemRequestDto(any());

        Mockito.verifyNoMoreInteractions(userService);
        Mockito.verifyNoMoreInteractions(itemRequestStorage);
        Mockito.verifyNoMoreInteractions(itemRequestMapper);
    }

    @Test
    void getItemRequestEntityByIdTest() {
        final User user2 = new User(1, "email2@mail.ru", "name2");
        final LocalDateTime itemRequestTime2 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        final ItemRequest itemRequestAfter2 = new ItemRequest(1, "itemRequestDescription2", user2,
                itemRequestTime2, null);

        when(itemRequestStorage.findById(anyInt()))
                .thenReturn(Optional.of(itemRequestAfter2));

        ItemRequest actualItemRequest = itemRequestService.getItemRequestEntityById(1);

        Assertions.assertEquals(itemRequestAfter2, actualItemRequest);

        Mockito.verify(itemRequestStorage, Mockito.times(1)).findById(any());

        Mockito.verifyNoMoreInteractions(itemRequestStorage);
    }

    @Test
    void getAllYourItemRequest_200_Test() {
        final UserDto userDto3 = new UserDto(1, "name3", "email3@mail.ru");
        final LocalDateTime itemRequestTime3 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        final ShortUser shortUser3 = new ShortUser() {
            @Override
            public int getId() {
                return 1;
            }
        };
        final ShortUserDto shortUserDto3 = new ShortUserDto(1);
        final ItemRequestSimplified itemRequestSimplified3 =
                new ItemRequestSimplified() {
                    @Override
                    public int getId() {
                        return 1;
                    }

                    @Override
                    public String getDescription() {
                        return "itemRequestSimplifiedDescription3";
                    }

                    @Override
                    public ShortUser getRequester() {
                        return shortUser3;
                    }

                    @Override
                    public LocalDateTime getCreated() {
                        return itemRequestTime3;
                    }

                    @Override
                    public List<ShortItem> getItems() {
                        return null;
                    }
                };

        final ItemRequestSimplifiedDto itemRequestSimplifiedDto3 =
                new ItemRequestSimplifiedDto(1, "itemRequestSimplifiedDescription3",
                        shortUserDto3, itemRequestTime3, null);

        when(userService.getUserById(anyInt()))
                .thenReturn(userDto3);
        when(itemRequestStorage.findAllByRequesterId(anyInt()))
                .thenReturn(List.of(itemRequestSimplified3));

        List<ItemRequestSimplifiedDto> actualItemRequest = itemRequestService.getAllYourItemRequest(1);

        Assertions.assertEquals(List.of(itemRequestSimplifiedDto3), actualItemRequest);

        Mockito.verify(userService, Mockito.times(1)).getUserById(anyInt());
        Mockito.verify(itemRequestStorage, Mockito.times(1)).findAllByRequesterId(anyInt());

        Mockito.verify(itemRequestMapper, Mockito.times(1)).toItemRequestSimplifiedDto(any());

        Mockito.verifyNoMoreInteractions(userService);
        Mockito.verifyNoMoreInteractions(itemRequestStorage);
        Mockito.verifyNoMoreInteractions(itemRequestMapper);
    }

    @Test
    void getAllYourItemRequest_new_ArrayList_200_Test() {
        final UserDto userDto3 = new UserDto(1, "name3", "email3@mail.ru");

        when(userService.getUserById(anyInt()))
                .thenReturn(userDto3);
        when(itemRequestStorage.findAllByRequesterId(anyInt()))
                .thenReturn(new ArrayList<>());

        List<ItemRequestSimplifiedDto> actualItemRequest = itemRequestService.getAllYourItemRequest(1);

        Assertions.assertEquals(new ArrayList<>(), actualItemRequest);

        Mockito.verify(userService, Mockito.times(1)).getUserById(anyInt());
        Mockito.verify(itemRequestStorage, Mockito.times(1)).findAllByRequesterId(anyInt());

        Mockito.verifyNoMoreInteractions(userService);
        Mockito.verifyNoMoreInteractions(itemRequestStorage);
        Mockito.verifyNoMoreInteractions(itemRequestMapper);
    }

    @Test
    void getItemRequestById_200_Test() {
        final UserDto userDto4 = new UserDto(1, "name4", "email4@mail.ru");
        final LocalDateTime itemRequestTime4 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        final ShortUser shortUser4 = new ShortUser() {
            @Override
            public int getId() {
                return 1;
            }
        };
        final ShortUserDto shortUserDto4 = new ShortUserDto(1);
        final ItemRequestSimplified itemRequestSimplified4 =
                new ItemRequestSimplified() {
                    @Override
                    public int getId() {
                        return 1;
                    }

                    @Override
                    public String getDescription() {
                        return "itemRequestSimplifiedDescription4";
                    }

                    @Override
                    public ShortUser getRequester() {
                        return shortUser4;
                    }

                    @Override
                    public LocalDateTime getCreated() {
                        return itemRequestTime4;
                    }

                    @Override
                    public List<ShortItem> getItems() {
                        return null;
                    }
                };

        final ItemRequestSimplifiedDto itemRequestSimplifiedDto4 =
                new ItemRequestSimplifiedDto(1, "itemRequestSimplifiedDescription4",
                        shortUserDto4, itemRequestTime4, null);

        when(userService.getUserById(anyInt()))
                .thenReturn(userDto4);
        when(itemRequestStorage.getItemRequestById(anyInt()))
                .thenReturn(itemRequestSimplified4);

        ItemRequestSimplifiedDto actualItemRequest = itemRequestService.getItemRequestById(1, 1);

        Assertions.assertEquals(itemRequestSimplifiedDto4, actualItemRequest);

        Mockito.verify(userService, Mockito.times(1)).getUserById(anyInt());
        Mockito.verify(itemRequestStorage, Mockito.times(1)).getItemRequestById(anyInt());

        Mockito.verify(itemRequestMapper, Mockito.times(1)).toItemRequestSimplifiedDto(any());

        Mockito.verifyNoMoreInteractions(userService);
        Mockito.verifyNoMoreInteractions(itemRequestStorage);
        Mockito.verifyNoMoreInteractions(itemRequestMapper);
    }

    @Test
    void getItemRequestById_itemRequest_not_found_404_Test() {
        ObjectNotFoundException exc = assertThrows(ObjectNotFoundException.class, new Executable() {
            @Override
            public void execute() throws ObjectNotFoundException {
                final UserDto userDto4 = new UserDto(1, "name4", "email4@mail.ru");

                when(userService.getUserById(anyInt()))
                        .thenReturn(userDto4);
                when(itemRequestStorage.getItemRequestById(anyInt()))
                        .thenReturn(null);

                ItemRequestSimplifiedDto actualItemRequest = itemRequestService.getItemRequestById(1, 1);
            }
        });
        Assertions.assertEquals("itemRequest id - " + 1 + " not found", exc.getMessage());

        Mockito.verify(userService, Mockito.times(1)).getUserById(anyInt());
        Mockito.verify(itemRequestStorage, Mockito.times(1)).getItemRequestById(anyInt());

        Mockito.verifyNoMoreInteractions(userService);
        Mockito.verifyNoMoreInteractions(itemRequestStorage);
        Mockito.verifyNoMoreInteractions(itemRequestMapper);
    }

    @Test
    void getItemRequestById_user_not_found_404_Test() {
        ObjectNotFoundException exc = assertThrows(ObjectNotFoundException.class, new Executable() {
            @Override
            public void execute() throws ObjectNotFoundException {
                when(userService.getUserById(anyInt()))
                        .thenReturn(null);

                ItemRequestSimplifiedDto actualItemRequest = itemRequestService.getItemRequestById(1, 1);
            }
        });
        Assertions.assertEquals("user id - " + 1 + " not found", exc.getMessage());

        Mockito.verify(userService, Mockito.times(1)).getUserById(anyInt());

        Mockito.verifyNoMoreInteractions(userService);
    }

    @Test
    void getAllItemRequestPageable_New_ArrayList_200_Test() {
        final UserDto userDto5 = new UserDto(1, "name5", "email5@mail.ru");

        when(userService.getUserById(anyInt()))
                .thenReturn(userDto5);

        List<ItemRequestSimplifiedDto> actualItemRequest = itemRequestService.getAllItemRequestPageable(1, null, null);

        Assertions.assertEquals(new ArrayList<>(), actualItemRequest);

        Mockito.verify(userService, Mockito.times(1)).getUserById(anyInt());

        Mockito.verifyNoMoreInteractions(userService);
        Mockito.verifyNoMoreInteractions(itemRequestStorage);
        Mockito.verifyNoMoreInteractions(itemRequestMapper);
    }

    @Test
    void getAllItemRequestPageable_200_Test() {
        final UserDto userDto6 = new UserDto(1, "name6", "email6@mail.ru");
        final LocalDateTime itemRequestTime6 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        final ShortUser shortUser6 = new ShortUser() {
            @Override
            public int getId() {
                return 1;
            }
        };
        final ShortUserDto shortUserDto6 = new ShortUserDto(1);
        final ItemRequestSimplified itemRequestSimplified6 =
                new ItemRequestSimplified() {
                    @Override
                    public int getId() {
                        return 1;
                    }

                    @Override
                    public String getDescription() {
                        return "itemRequestSimplifiedDescription6";
                    }

                    @Override
                    public ShortUser getRequester() {
                        return shortUser6;
                    }

                    @Override
                    public LocalDateTime getCreated() {
                        return itemRequestTime6;
                    }

                    @Override
                    public List<ShortItem> getItems() {
                        return null;
                    }
                };

        Page<ItemRequestSimplified> page6 = new PageImpl<>((List.of(itemRequestSimplified6)));

        final ItemRequestSimplifiedDto itemRequestSimplifiedDto6 =
                new ItemRequestSimplifiedDto(1, "itemRequestSimplifiedDescription6",
                        shortUserDto6, itemRequestTime6, null);

        when(userService.getUserById(anyInt()))
                .thenReturn(userDto6);
        when(itemRequestStorage.findAllBy(any()))
                .thenReturn(page6);


        List<ItemRequestSimplifiedDto> actualItemRequest = itemRequestService.getAllItemRequestPageable(2, 0, 1);

        Assertions.assertEquals(itemRequestSimplifiedDto6, actualItemRequest.get(0));

        Mockito.verify(userService, Mockito.times(1)).getUserById(anyInt());
        Mockito.verify(itemRequestStorage, Mockito.times(1)).findAllBy(any());
        Mockito.verify(itemRequestMapper, Mockito.times(1)).toItemRequestSimplifiedDto(any());

        Mockito.verifyNoMoreInteractions(userService);
        Mockito.verifyNoMoreInteractions(itemRequestStorage);
        Mockito.verifyNoMoreInteractions(itemRequestMapper);
    }
}
