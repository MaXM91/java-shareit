package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

/**
 * Controller for work with User entity.
 */
@Validated
@Slf4j
@RestController
@RequestMapping(path = "/users")
public class UserController {
    private static final String UserId = "/{userId}";
    private final UserService userService;

    UserController(@Qualifier("UserServiceImpl") UserService userService) {
        this.userService = userService;
    }

    /**
     * Create user and return him.
     */
    @PostMapping
    public UserDto addUser(@Valid @RequestBody UserDto userDto) {
        log.info("request POST/addUser : {}", userDto);

        UserDto responseObject = userService.addUser(userDto);
        log.info("response POST/addUser : {}", responseObject);

        return responseObject;
    }

    /**
     * @param userId
     * @return user by userId.
     */
    @GetMapping(UserId)
    public UserDto getUserById(@PathVariable int userId) {
        log.info("request GET/getUserById : {}", userId);

        UserDto responseObject = userService.getUserById(userId);
        log.info("response GET/getUserById : {}", responseObject);

        return responseObject;
    }

    /**
     * @return all users.
     */
    @GetMapping
    public List<UserDto> getAllUsers() {
        log.info("request GET/addAllUsers");

        List<UserDto> responseObject = userService.getAllUsers();
        log.info("response GET/addAllUsers : {}", responseObject);

        return responseObject;
    }

    /**
     * @param userId
     * @param userDto
     * @return updated user.
     */
    @PatchMapping(UserId)
    public UserDto update(@PathVariable int userId, @RequestBody UserDto userDto) {
        log.info("request PATCH/update : {}, {}", userId, userDto);

        UserDto responseObject = userService.update(userId, userDto);
        log.info("response PATCH/update : {}, {}", userId, responseObject);

        return responseObject;
    }

    /**
     * delete user bu userId.
     *
     * @param userId
     */
    @DeleteMapping(UserId)
    public void deleteUser(@PathVariable int userId) {
        log.info("request DELETE/userId : {}", userId);

        userService.delete(userId);
    }
}
