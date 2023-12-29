package ru.practicum.shareit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserServiceImpl;
import ru.practicum.shareit.validation.exceptions.EmailRegisteredException;
import ru.practicum.shareit.validation.exceptions.ObjectNotFoundException;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    UserServiceImpl userService;

    @Autowired
    private MockMvc mvc;

    private final UserDto userDto1 = new UserDto(1, "John", "john.doe@mail.com");
    private final UserDto userDto10 = new UserDto(1, null, "john.doe@mail.com");
    private final UserDto userDto11 = new UserDto(1, "John", null);
    private final UserDto userDtoBefore1 = new UserDto(null, "update", "update");
    private final UserDto userDtoAfter1 = new UserDto(1, "update", "update");
    private final UserDto userDto2 = new UserDto(2, "John2", "john2.doe@mail.com");
    private final List<UserDto> userDtoList = List.of(userDto1, userDto2);

    @Test
    void addUser_200_Test() throws Exception {
        when(userService.addUser(userDto1))
                .thenReturn(userDto1);

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDto1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(userDto1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(userDto1.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(userDto1.getEmail()));

        // Check that userService.addUser with userDto1 was started 1 time
        Mockito.verify(userService, Mockito.times(1)).addUser(userDto1);
        // Check that userService have not more interaction
        Mockito.verifyNoMoreInteractions(userService);
    }

    @Test
    void addUser_bad_name_400_Test() throws Exception {
        when(userService.addUser(userDto1))
                .thenReturn(userDto1);

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDto10))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void addUser_bad_email_400_Test() throws Exception {
        when(userService.addUser(userDto1))
                .thenReturn(userDto1);

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDto11))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void addUser_email_has_been_registered_409_Test() throws Exception {
        when(userService.addUser(userDto1))
                .thenThrow(new EmailRegisteredException("the email was registered"));

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDto1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    void getUserById_200_Test() throws Exception {
        when(userService.getUserById(anyInt()))
                .thenReturn(userDto1);

        mvc.perform(get("/users/1")
                        .content(mapper.writeValueAsString(1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(userDto1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(userDto1.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(userDto1.getEmail()));

        Mockito.verify(userService, Mockito.times(1)).getUserById(1);
        Mockito.verifyNoMoreInteractions(userService);
    }

    @Test
    void getUserById_user_not_found_404_Test() throws Exception {
        when(userService.getUserById(anyInt()))
                .thenThrow(new ObjectNotFoundException("user id - " + 1 + " not found"));

        mvc.perform(get("/users/1")
                        .content(mapper.writeValueAsString(1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

        Mockito.verify(userService, Mockito.times(1)).getUserById(1);
        Mockito.verifyNoMoreInteractions(userService);
    }

    @Test
    void getAllUsersTest() throws Exception {
        when(userService.getAllUsers())
                .thenReturn(userDtoList);

        mvc.perform(get("/users")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(userDto1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(userDto1.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email").value(userDto1.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(userDto2.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value(userDto2.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].email").value(userDto2.getEmail()));

        Mockito.verify(userService, Mockito.times(1)).getAllUsers();
        Mockito.verifyNoMoreInteractions(userService);
    }

    @Test
    void updateTest() throws Exception {
        when(userService.update(anyInt(),any()))
                .thenReturn(userDtoAfter1);

        mvc.perform(patch("/users/1")
                        .content(mapper.writeValueAsString(userDtoBefore1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(userDtoAfter1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(userDtoAfter1.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(userDtoAfter1.getEmail()));

        Mockito.verify(userService, Mockito.times(1)).update(1, userDtoBefore1);
        Mockito.verifyNoMoreInteractions(userService);
    }

    @Test
    void deleteTest() throws Exception {
        mvc.perform(delete("/users/{userId}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        Mockito.verify(userService, Mockito.times(1)).delete(1);
        Mockito.verifyNoMoreInteractions(userService);
    }
}
