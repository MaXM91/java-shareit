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
    UserService userService;

    UserController(@Qualifier("InMemoryUserService") UserService userService) {
        this.userService = userService;
    }

    /**
     * Create user and return him.
     */
    @PostMapping
    public User addUser(@Valid @RequestBody UserDto userDto) {
        log.info("request POST/addUser : {}", userDto);

        User responseObject = userService.addUser(userDto);
        log.info("response POST/addUser : {}", responseObject);

        return responseObject;
    }

    /**
     * @param userId
     * @return user by userId.
     */
    @GetMapping("/{userId}")
    public User getUserById(@PathVariable int userId) {
        log.info("request GET/getUserById : {}", userId);

        User responseObject = userService.getUserById(userId);
        log.info("response GET/getUserById : {}", responseObject);

        return responseObject;
    }

    /**
     * @return all users.
     */
    @GetMapping
    public List<User> getAllUsers() {
        log.info("request GET/addAllUsers");

        List<User> responseObject = userService.getAllUsers();
        log.info("response GET/addAllUsers : {}", responseObject);

        return responseObject;
    }

    /**
     * @param userId
     * @param userDto
     * @return updated user.
     */
    @PatchMapping("/{userId}")
    public User update(@PathVariable long userId, @RequestBody UserDto userDto) {
        log.info("request PATCH/update : {}, {}", userId, userDto);

        User responseObject = userService.update(userId, userDto);
        log.info("response PATCH/update : {}, {}", userId, responseObject);

        return responseObject;
    }

    /**
     * delete user bu userId.
     *
     * @param userId
     */
    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable long userId) {
        log.info("request DELETE/userId : {}", userId);

        userService.delete(userId);
    }
}
