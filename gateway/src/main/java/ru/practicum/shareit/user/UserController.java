package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;

/**
 * Controller for work with User entity.
 */
@Validated
@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserClient userClient;
    private static final String UserId = "/{userId}";

    /**
     * Create user and return him.
     */
    @PostMapping
    public ResponseEntity<Object> addUser(@Valid @RequestBody UserDto userDto) {
        log.info("request POST/addUser : {}", userDto);

        return userClient.postUser(userDto);
    }

    /**
     * @param userId
     * @return user by userId.
     */
    @GetMapping(UserId)
    public ResponseEntity<Object> getUserById(@PathVariable int userId) {
        log.info("request GET/getUserById : {}", userId);

        return userClient.getUserById(userId);
    }

    /**
     * @return all users.
     */
    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        log.info("request GET/addAllUsers");

        return userClient.getAllUsers();
    }

    /**
     * @param userId
     * @param userDto
     * @return updated user.
     */
    @PatchMapping(UserId)
    public ResponseEntity<Object> update(@PathVariable int userId, @RequestBody UserDto userDto) {
        log.info("request PATCH/update : {}, {}", userId, userDto);

        return userClient.update(userId, userDto);
    }

    /**
     * delete user bu userId.
     *
     * @param userId
     */
    @DeleteMapping(UserId)
    public ResponseEntity<Object> deleteUser(@PathVariable int userId) {
        log.info("request DELETE/userId : {}", userId);

        return userClient.deleteUser(userId);
    }
}