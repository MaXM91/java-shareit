package ru.practicum.shareit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestSimplifiedDto;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
public class ItemRequestControllerTest {
    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ItemRequestServiceImpl itemRequestService;

    @Autowired
    private MockMvc mvc;

    private final ItemRequestDto itemRequestDtoBefore1 =
            new ItemRequestDto(null, "description1", null, LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS), null);
    private final ItemRequestDto itemRequestDtoAfter1 =
            new ItemRequestDto(1, "description1", null, itemRequestDtoBefore1.getCreated(), null);
    ItemRequestSimplifiedDto itemRequestForTest1 =
            new ItemRequestSimplifiedDto(1, "weqr", null, LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS), null);

    @Test
    public void addItemRequestTest() throws Exception {
        when(itemRequestService.addItemRequest(anyInt(), any()))
                .thenReturn(itemRequestDtoAfter1);

        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(itemRequestDtoBefore1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemRequestDtoAfter1.getId()))
                .andExpect(jsonPath("$.description").value(itemRequestDtoAfter1.getDescription()))
                .andExpect(jsonPath("$.requester").value(itemRequestDtoAfter1.getRequester()))
                .andExpect(jsonPath("$.created").value(itemRequestDtoAfter1.getCreated().toString()))
                .andExpect(jsonPath("$.items").value(itemRequestDtoAfter1.getItems()));

        Mockito.verify(itemRequestService, Mockito.times(1)).addItemRequest(1, itemRequestDtoBefore1);
        Mockito.verifyNoMoreInteractions(itemRequestService);
    }

    @Test
    public void getAllYourItemRequestTest() throws Exception {

        when(itemRequestService.getAllYourItemRequest(anyInt()))
                .thenReturn(List.of(itemRequestForTest1));

        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(itemRequestForTest1.getId()))
                .andExpect(jsonPath("$[0].description").value(itemRequestForTest1.getDescription()))
                .andExpect(jsonPath("$[0].requester").value(itemRequestForTest1.getRequester()))
                .andExpect(jsonPath("$[0].created").value(itemRequestForTest1.getCreated().toString()))
                .andExpect(jsonPath("$[0].items").value(itemRequestForTest1.getItems()));
    }

    @Test
    public void getItemRequestByIdTest() throws Exception {

        when(itemRequestService.getItemRequestById(anyInt(), anyInt()))
                .thenReturn(itemRequestForTest1);

        mvc.perform(get("/requests/{requestId}", 1)
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemRequestForTest1.getId()))
                .andExpect(jsonPath("$.description").value(itemRequestForTest1.getDescription()))
                .andExpect(jsonPath("$.requester").value(itemRequestForTest1.getRequester()))
                .andExpect(jsonPath("$.created").value(itemRequestForTest1.getCreated().toString()))
                .andExpect(jsonPath("$.items").value(itemRequestForTest1.getItems()));
    }

    @Test
    public void getAllItemRequestOtherUserTest() throws Exception {

        when(itemRequestService.getAllItemRequestPageable(anyInt(), anyInt(), anyInt()))
                .thenReturn(List.of(itemRequestForTest1));

        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", "1")
                        .param("from","0")
                        .param("size", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(itemRequestForTest1.getId()))
                .andExpect(jsonPath("$[0].description").value(itemRequestForTest1.getDescription()))
                .andExpect(jsonPath("$[0].requester").value(itemRequestForTest1.getRequester()))
                .andExpect(jsonPath("$[0].created").value(itemRequestForTest1.getCreated().toString()))
                .andExpect(jsonPath("$[0].items").value(itemRequestForTest1.getItems()));
    }
}
