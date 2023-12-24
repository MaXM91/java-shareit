package ru.practicum.shareit.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceImpl;
import ru.practicum.shareit.user.storage.UserStorage;
import ru.practicum.shareit.validation.exceptions.EmailRegisteredException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ContextConfiguration(classes = {UserServiceImpl.class, UserMapper.class})
public class UserServiceTest {
    @SpyBean
    private UserMapper userMapper;

    @MockBean
    private UserStorage userStorage;

    @Autowired
    private UserServiceImpl userService;

    @Test
    void addUser_200_Test() {
        UserDto userDtoBefore1 = new UserDto(null, "John1", "john.doe1@mail.com");
        User userAfter1 = new User(1, "john.doe1@mail.com", "John1");
        UserDto userDtoAfter1 = new UserDto(1, "John1", "john.doe1@mail.com");

        when(userStorage.save(any()))
                .thenReturn(userAfter1);

        UserDto actualUser = userService.addUser(userDtoBefore1);

        Assertions.assertEquals(userDtoAfter1.toString(), actualUser.toString());
        Assertions.assertEquals(userMapper.toUser(userDtoAfter1).toString(), userMapper.toUser(actualUser).toString());

        Mockito.verify(userStorage, Mockito.times(1)).save(any());
        Mockito.verifyNoMoreInteractions(userStorage);

        Mockito.verify(userMapper, Mockito.times(3)).toUser(any());
        Mockito.verify(userMapper, Mockito.times(1)).toUserDto(userAfter1);
        Mockito.verifyNoMoreInteractions(userStorage);
    }


    @Test
    void getUserById_200_Test() {
        User userAfter3 = new User(1, "john.doe3@mail.com", "John3");
        UserDto userDtoAfter3 = new UserDto(1, "John3", "john.doe3@mail.com");

        when(userStorage.findById(anyInt()))
                .thenReturn(Optional.ofNullable(userAfter3));

        UserDto actualUser = userService.getUserById(1);

        Assertions.assertEquals(userDtoAfter3, actualUser);

        Mockito.verify(userStorage, Mockito.times(1)).findById(1);
        Mockito.verifyNoMoreInteractions(userStorage);

        Mockito.verify(userMapper, Mockito.times(1)).toUserDto(userAfter3);
        Mockito.verifyNoMoreInteractions(userStorage);
    }

    @Test
    void getUserEntityById_200_Test() {
        User userAfter4 = new User(1, "john.doe4@mail.com", "John4");

        when(userStorage.findById(anyInt()))
                .thenReturn(Optional.ofNullable(userAfter4));

        User actualUser = userService.getUserEntityById(1);

        Assertions.assertEquals(userAfter4, actualUser);

        Mockito.verify(userStorage, Mockito.times(1)).findById(1);
        Mockito.verifyNoMoreInteractions(userStorage);

        Mockito.verifyNoMoreInteractions(userStorage);
    }

    @Test
    void getAllUsersTest() {
        User userAfter5 = new User(1, "john.doe5@mail.com", "John5");
        UserDto userDtoAfter5 = new UserDto(1, "John5", "john.doe5@mail.com");

        when(userStorage.findAll())
                .thenReturn(List.of(userAfter5));

        List<UserDto> actualUser = userService.getAllUsers();

        Assertions.assertEquals(List.of(userDtoAfter5), actualUser);

        Mockito.verify(userStorage, Mockito.times(1)).findAll();
        Mockito.verifyNoMoreInteractions(userStorage);

        Mockito.verify(userMapper, Mockito.times(1)).toUserDto(userAfter5);

        Mockito.verifyNoMoreInteractions(userStorage);
    }

    @Test
    void getUserByEmailTest() {
        when(userStorage.existsByEmail(anyString()))
                .thenReturn(true);

        boolean actualUser = userService.getUserByEmail("john.doe6@mail.com");

        Assertions.assertEquals(true, actualUser);

        Mockito.verify(userStorage, Mockito.times(1)).existsByEmail("john.doe6@mail.com");
        Mockito.verifyNoMoreInteractions(userStorage);

        Mockito.verifyNoMoreInteractions(userStorage);
    }

    @Test
    void update_200_Test() {
        User userAfter7 = new User(1, "john.doe7@mail.com", "John7");
        UserDto userDtoUpdateBefore7 = new UserDto(1, "update7", "update7@mail.com");
        User userUpdateBefore7 = new User(1, "update7@mail.com", "update7");
        UserDto userDtoUpdateAfter7 = new UserDto(1, "update7", "update7@mail.com");

        when(userStorage.findById(anyInt()))
                .thenReturn(Optional.ofNullable(userAfter7));
        when(userStorage.getUserByEmail(anyString()))
                .thenReturn(null);
        when(userStorage.save(any()))
                .thenReturn(userUpdateBefore7);


        UserDto actualUser = userService.update(1, userDtoUpdateBefore7);

        Assertions.assertEquals(userDtoUpdateAfter7.toString(), actualUser.toString());

        Mockito.verify(userStorage, Mockito.times(1)).findById(anyInt());
        Mockito.verify(userStorage, Mockito.times(1)).save(any());
        Mockito.verify(userStorage, Mockito.times(1)).getUserByEmail(anyString());

        Mockito.verify(userMapper, Mockito.times(1)).toUser(any());
        Mockito.verify(userMapper, Mockito.times(1)).toUserDto(any());

        Mockito.verifyNoMoreInteractions(userStorage);
    }

    @Test
    void update_null_data_200_Test() {
        User userAfter7 = new User(1, "john.doe7@mail.com", "John7");
        UserDto userDtoUpdateBefore7 = new UserDto(1, null, null);
        User userUpdateBefore7 = new User(1, "john.doe7@mail.com", "John7");
        UserDto userDtoUpdateAfter7 = new UserDto(1, "John7", "john.doe7@mail.com");

        when(userStorage.findById(anyInt()))
                .thenReturn(Optional.ofNullable(userAfter7));
        when(userStorage.getUserByEmail(anyString()))
                .thenReturn(null);
        when(userStorage.save(any()))
                .thenReturn(userUpdateBefore7);


        UserDto actualUser = userService.update(1, userDtoUpdateBefore7);

        Assertions.assertEquals(userDtoUpdateAfter7.toString(), actualUser.toString());

        Mockito.verify(userStorage, Mockito.times(1)).findById(anyInt());
        Mockito.verify(userStorage, Mockito.times(1)).save(any());

        Mockito.verify(userMapper, Mockito.times(1)).toUser(any());
        Mockito.verify(userMapper, Mockito.times(1)).toUserDto(any());

        Mockito.verifyNoMoreInteractions(userStorage);
    }

    @Test
    void update_empty_data_200_Test() {
        User userAfter7 = new User(1, "john.doe7@mail.com", "John7");
        UserDto userDtoUpdateBefore7 = new UserDto(1, "", "");
        User userUpdateBefore7 = new User(1, "john.doe7@mail.com", "John7");
        UserDto userDtoUpdateAfter7 = new UserDto(1, "John7", "john.doe7@mail.com");

        when(userStorage.findById(anyInt()))
                .thenReturn(Optional.ofNullable(userAfter7));
        when(userStorage.getUserByEmail(anyString()))
                .thenReturn(null);
        when(userStorage.save(any()))
                .thenReturn(userUpdateBefore7);


        UserDto actualUser = userService.update(1, userDtoUpdateBefore7);

        Assertions.assertEquals(userDtoUpdateAfter7.toString(), actualUser.toString());

        Mockito.verify(userStorage, Mockito.times(1)).findById(anyInt());
        Mockito.verify(userStorage, Mockito.times(1)).save(any());

        Mockito.verify(userMapper, Mockito.times(1)).toUser(any());
        Mockito.verify(userMapper, Mockito.times(1)).toUserDto(any());

        Mockito.verifyNoMoreInteractions(userStorage);
    }

    @Test
    void update_blank_data_200_Test() {
        User userAfter7 = new User(1, "john.doe7@mail.com", "John7");
        UserDto userDtoUpdateBefore7 = new UserDto(1, "        ", "       ");
        User userUpdateBefore7 = new User(1, "john.doe7@mail.com", "John7");
        UserDto userDtoUpdateAfter7 = new UserDto(1, "John7", "john.doe7@mail.com");

        when(userStorage.findById(anyInt()))
                .thenReturn(Optional.ofNullable(userAfter7));
        when(userStorage.getUserByEmail(anyString()))
                .thenReturn(null);
        when(userStorage.save(any()))
                .thenReturn(userUpdateBefore7);


        UserDto actualUser = userService.update(1, userDtoUpdateBefore7);

        Assertions.assertEquals(userDtoUpdateAfter7.toString(), actualUser.toString());

        Mockito.verify(userStorage, Mockito.times(1)).findById(anyInt());
        Mockito.verify(userStorage, Mockito.times(1)).save(any());

        Mockito.verify(userMapper, Mockito.times(1)).toUser(any());
        Mockito.verify(userMapper, Mockito.times(1)).toUserDto(any());

        Mockito.verifyNoMoreInteractions(userStorage);
    }

    @Test
    void update_equals_data_200_Test() {
        User userAfter7 = new User(1, "john.doe7@mail.com", "John7");
        UserDto userDtoUpdateBefore7 = new UserDto(1, "John7", "john.doe7@mail.com");
        User userUpdateBefore7 = new User(1, "john.doe7@mail.com", "John7");
        UserDto userDtoUpdateAfter7 = new UserDto(1, "John7", "john.doe7@mail.com");

        when(userStorage.findById(anyInt()))
                .thenReturn(Optional.ofNullable(userAfter7));
        when(userStorage.getUserByEmail(anyString()))
                .thenReturn(null);
        when(userStorage.save(any()))
                .thenReturn(userUpdateBefore7);


        UserDto actualUser = userService.update(1, userDtoUpdateBefore7);

        Assertions.assertEquals(userDtoUpdateAfter7.toString(), actualUser.toString());

        Mockito.verify(userStorage, Mockito.times(1)).findById(anyInt());
        Mockito.verify(userStorage, Mockito.times(1)).save(any());

        Mockito.verify(userMapper, Mockito.times(1)).toUser(any());
        Mockito.verify(userMapper, Mockito.times(1)).toUserDto(any());

        Mockito.verifyNoMoreInteractions(userStorage);
    }

    @Test
    void update_bad_email_409_Test() {
        EmailRegisteredException exc = assertThrows(EmailRegisteredException.class, new Executable() {
            @Override
            public void execute() throws EmailRegisteredException {
                User userAfter8 = User.builder().id(1).email("john.doe7@mail.com").name("John7").build();
                User userAfter7 = new User(1, "john.doe7@mail.com", "John7");
                UserDto userDtoUpdateBefore7 = new UserDto(1, "update7", "update7@mail.com");

                when(userStorage.findById(anyInt()))
                        .thenReturn(Optional.of(userAfter7));
                when(userStorage.getUserByEmail(anyString()))
                        .thenReturn(userAfter8);

                UserDto actualUser = userService.update(1, userDtoUpdateBefore7);
            }
        });
        Assertions.assertEquals("the email was registered", exc.getMessage());

        Mockito.verify(userStorage, Mockito.times(1)).findById(anyInt());
        Mockito.verify(userStorage, Mockito.times(1)).getUserByEmail(anyString());

        Mockito.verify(userMapper, Mockito.times(1)).toUser(any());

        Mockito.verifyNoMoreInteractions(userStorage);
        Mockito.verifyNoMoreInteractions(userMapper);
    }

    @Test
    void deleteTest() {
        User userAfter8 = new User(1, "john.doe8@mail.com", "John8");

        when(userStorage.findById(anyInt()))
                .thenReturn(Optional.ofNullable(userAfter8));

        userService.delete(1);

        Mockito.verify(userStorage, Mockito.times(1)).findById(anyInt());
        Mockito.verify(userStorage, Mockito.times(1)).deleteByEmail(anyString());
        Mockito.verifyNoMoreInteractions(userStorage);
    }
}
