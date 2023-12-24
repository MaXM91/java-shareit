package ru.practicum.shareit.integration;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceImpl;
import ru.practicum.shareit.user.storage.UserStorage;
import ru.practicum.shareit.validation.exceptions.ObjectNotFoundException;

import javax.transaction.Transactional;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceImplTest {
    private final UserServiceImpl userService;
    private final UserStorage userStorage;

    @AfterEach
    void deleteAll() {
        userStorage.deleteAll();
    }

    @Test
    void addUserTest() {
        UserDto userDtoBefore1 = new UserDto(null, "user1", "user1@mail.ru");

        UserDto userDtoAfter1 = userService.addUser(userDtoBefore1);

        Assertions.assertNotNull(userDtoAfter1.getId());

        assertThat(userDtoAfter1)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(userDtoBefore1);
    }

    @Test
    void getUserByIdTest() {
        UserDto userDtoBefore1 = new UserDto(null, "user1", "user1@mail.ru");

        UserDto userDtoAfter1 = userService.addUser(userDtoBefore1);
        UserDto foundedUser1 = userService.getUserById(userDtoAfter1.getId());

        Assertions.assertNotNull(userDtoAfter1.getId());
        Assertions.assertNotNull(foundedUser1.getId());

        assertThat(userDtoAfter1)
                .usingRecursiveComparison()
                .isEqualTo(foundedUser1);
    }

    @Test
    void getUserEntityByIdTest() {
        UserDto userDtoBefore1 = new UserDto(null, "user1", "user1@mail.ru");

        UserDto userDtoAfter1 = userService.addUser(userDtoBefore1);
        User foundedUser1 = userService.getUserEntityById(userDtoAfter1.getId());

        Assertions.assertNotNull(userDtoAfter1.getId());
        Assertions.assertNotNull(foundedUser1.getEmail());

        Assertions.assertEquals(userDtoAfter1.getId(), foundedUser1.getId());
        Assertions.assertEquals(userDtoAfter1.getEmail(), foundedUser1.getEmail());
        Assertions.assertEquals(userDtoAfter1.getName(), foundedUser1.getName());
    }

    @Test
    void getAllUsersTest() {
        UserDto userDtoBefore1 = new UserDto(null, "user1", "user1@mail.ru");
        UserDto userDtoBefore2 = new UserDto(null, "user2", "user2@mail.ru");

        UserDto userDtoAfter1 = userService.addUser(userDtoBefore1);
        UserDto userDtoAfter2 = userService.addUser(userDtoBefore2);

        List<UserDto> foundedUsers = userService.getAllUsers();

        assertThat(foundedUsers)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(List.of(userDtoAfter1, userDtoAfter2));
    }

    @Test
    void getUserByEmailTest() {
        UserDto userDtoBefore1 = new UserDto(null, "user1", "user1@mail.ru");

        UserDto userDtoAfter1 = userService.addUser(userDtoBefore1);
        boolean foundedUser1 = userService.getUserByEmail(userDtoAfter1.getEmail());

        Assertions.assertTrue(foundedUser1);
    }

    @Test
    void updateTest() {
        UserDto userDtoBefore1 = new UserDto(null, "user1", "user1@mail.ru");

        UserDto userDtoAfter1 = userService.addUser(userDtoBefore1);

        UserDto userDtoBeforeUpdate1 = new UserDto(userDtoAfter1.getId(), "update1", "update1@mail.ru");
        UserDto userDtoAfterUpdate1 = userService.update(userDtoBeforeUpdate1.getId(), userDtoBeforeUpdate1);

        Assertions.assertEquals(userDtoBeforeUpdate1, userDtoAfterUpdate1);
    }

    @Test
    void deleteTest() {
        UserDto userDtoBefore1 = new UserDto(null, "user1", "user1@mail.ru");

        UserDto userDtoAfter1 = userService.addUser(userDtoBefore1);
        userService.delete(userDtoAfter1.getId());

        ObjectNotFoundException exc = assertThrows(ObjectNotFoundException.class, new Executable() {
            @Override
            public void execute() throws ObjectNotFoundException {
                userService.getUserById(userDtoAfter1.getId());
            }
        });

        Assertions.assertEquals("user id - " + userDtoAfter1.getId() + " not found", exc.getMessage());
    }
}
