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
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
class UserControllerTests {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    UserServiceImpl userService;

    @Autowired
    private MockMvc mvc;

    private final UserDto userDto1 = new UserDto(1, "John", "john.doe@mail.com");
    private final UserDto userDtoBefore1 = new UserDto(null, "update", "update");
    private final UserDto userDtoAfter1 = new UserDto(1, "update", "update");
    private final UserDto userDto2 = new UserDto(2, "John2", "john2.doe@mail.com");
    private final List<UserDto> userDtoList = List.of(userDto1, userDto2);

    @Test
    void addUserTest() throws Exception {
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
    void getUserByIdTest() throws Exception {
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
