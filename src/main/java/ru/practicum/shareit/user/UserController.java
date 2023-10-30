package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Validated
@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    UserService userService;

    @Autowired
    UserController(@Qualifier("InMemoryUserService") UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User addUser(@Valid @RequestBody UserDto userDto) {
        log.info("request POST/addUser : {}", userDto);

        User responseObject = userService.addUser(userDto);
        log.info("response POST/addUser : {}", responseObject);

        return responseObject;
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable int userId) {
        log.info("request GET/getUserById : {}", userId);

        User responseObject = userService.getUserById(userId);
        log.info("response GET/getUserById : {}", responseObject);

        return responseObject;
    }

    @GetMapping
    public List<User> getAllUsers() {
        log.info("request GET/addAllUsers");

        List<User> responseObject = userService.getAllUsers();
        log.info("response GET/addAllUsers : {}", responseObject);

        return responseObject;
    }

    @PatchMapping("/{userId}")
    public User update(@PathVariable long userId, @RequestBody UserDto userDto) {
        log.info("request PATCH/update : {}, {}", userId, userDto);

        User responseObject = userService.update(userId, userDto);
        log.info("response PATCH/update : {}, {}", userId, responseObject);

        return responseObject;
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable long userId) {
        log.info("request DELETE/userId : {}", userId);

        userService.delete(userId);
    }
}
