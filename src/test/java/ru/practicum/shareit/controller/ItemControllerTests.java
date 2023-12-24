package ru.practicum.shareit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.CommentService;
import ru.practicum.shareit.item.service.ItemServiceImpl;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
public class ItemControllerTests {

    private final ItemDto itemDtoBefore1 = new ItemDto(0, "name1", "description1", true, 2);
    private final ItemDto itemDtoAfter1 = new ItemDto(1, "name1", "description1", true, 2);
    private final ItemDto getItemDtoAfterUpdate1 = new ItemDto(1, "update1", "update1", true, 2);
    private final LocalDateTime dataComment1 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    private final CommentDto commentDtoBefore1 = new CommentDto(0, "text1", "authorName1", dataComment1);
    private final CommentDto commentDtoAfter1 = new CommentDto(1, "text1", "authorName1", dataComment1);
    private final LocalDateTime start = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    private final LocalDateTime end = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(30);
    private final ItemWithBookingCommentDto itemWithBookingCommentDtoBefore1 =
            new ItemWithBookingCommentDto(null,
                    "name1", "description1", true, null, null, null);
    private final ItemWithBookingCommentDto itemWithBookingCommentDtoAfter1 =
            new ItemWithBookingCommentDto(1,
                    "name1", "description1", true, null, null, null);
    private final ItemWithBookingDto itemWithBookingDtoBefore1 = new ItemWithBookingDto(null,
            "name1", "description1", true, null, null);
    private final ItemWithBookingDto itemWithBookingDtoAfter1 = new ItemWithBookingDto(1,
            "name1", "description1", true, null, null);
    @Autowired
    ObjectMapper mapper;
    BookingForItemDto lastBooking = new BookingForItemDto(1, 2, start, end, 3);
    BookingForItemDto nextBooking = new BookingForItemDto(1, 2, start, end, 3);
    List<ItemWithBookingDto> listData = List.of(itemWithBookingDtoBefore1, itemWithBookingDtoAfter1);
    @MockBean
    private ItemServiceImpl itemService;
    @MockBean
    private CommentService commentService;
    @Autowired
    private MockMvc mvc;

    @Test
    public void addItemTest() throws Exception {
        when(itemService.addItem(anyInt(), any()))
                .thenReturn(itemDtoAfter1);

        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(itemDtoBefore1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemDtoAfter1.getId()))
                .andExpect(jsonPath("$.name").value(itemDtoAfter1.getName()))
                .andExpect(jsonPath("$.description").value(itemDtoAfter1.getDescription()))
                .andExpect(jsonPath("$.available").value(itemDtoAfter1.getAvailable()))
                .andExpect(jsonPath("$.requestId").value(itemDtoAfter1.getRequestId()));
    }

    @Test
    public void addCommentTest() throws Exception {
        when(commentService.addComment(anyInt(), anyInt(), any()))
                .thenReturn(commentDtoAfter1);

        mvc.perform(post("/items/{item}/comment", 1)
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(commentDtoBefore1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(commentDtoAfter1.getId()))
                .andExpect(jsonPath("$.text").value(commentDtoAfter1.getText()))
                .andExpect(jsonPath("$.authorName").value(commentDtoAfter1.getAuthorName()))
                .andExpect(jsonPath("$.created").value(commentDtoAfter1.getCreated().toString()));
    }

    @Test
    public void getItemByIdTest() throws Exception {
        when(itemService.getItemById(anyInt(), anyInt()))
                .thenReturn(itemWithBookingCommentDtoAfter1);

        mvc.perform(get("/items/{item}", 1)
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemWithBookingCommentDtoAfter1.getId()))
                .andExpect(jsonPath("$.name").value(itemWithBookingCommentDtoAfter1.getName()))
                .andExpect(jsonPath("$.description").value(itemWithBookingCommentDtoAfter1.getDescription()))
                .andExpect(jsonPath("$.available").value(itemWithBookingCommentDtoAfter1.getAvailable()))
                .andExpect(jsonPath("$.lastBooking").value(itemWithBookingCommentDtoAfter1.getLastBooking()))
                .andExpect(jsonPath("$.nextBooking").value(itemWithBookingCommentDtoAfter1.getNextBooking()))
                .andExpect(jsonPath("$.comments").value(itemWithBookingCommentDtoAfter1.getComments()));
    }

    @Test
    public void getItemsByUserIdTest() throws Exception {
        when(itemService.getItemsByOwnerId(anyInt(), anyInt(), anyInt()))
                .thenReturn(listData);

        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1)
                        .param("from", "0")
                        .param("size", "2")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(itemWithBookingDtoBefore1.getId()))
                .andExpect(jsonPath("$[0].name").value(itemWithBookingDtoBefore1.getName()))
                .andExpect(jsonPath("$[0].description").value(itemWithBookingDtoBefore1.getDescription()))
                .andExpect(jsonPath("$[0].available").value(itemWithBookingDtoBefore1.getAvailable()))
                .andExpect(jsonPath("$[0].lastBooking").value(itemWithBookingDtoBefore1.getLastBooking()))
                .andExpect(jsonPath("$[0].nextBooking").value(itemWithBookingDtoBefore1.getNextBooking()))
                .andExpect(jsonPath("$[1].id").value(itemWithBookingDtoAfter1.getId()))
                .andExpect(jsonPath("$[1].name").value(itemWithBookingDtoAfter1.getName()))
                .andExpect(jsonPath("$[1].description").value(itemWithBookingDtoAfter1.getDescription()))
                .andExpect(jsonPath("$[1].available").value(itemWithBookingDtoAfter1.getAvailable()))
                .andExpect(jsonPath("$[1].lastBooking").value(itemWithBookingDtoAfter1.getLastBooking()))
                .andExpect(jsonPath("$[1].nextBooking").value(itemWithBookingDtoAfter1.getNextBooking()));
    }

    @Test
    public void getItemByStringTest() throws Exception {
        when(itemService.getItemByString(anyInt(), anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(itemDtoAfter1));

        mvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", 1)
                        .param("text", "sthdfghdfg")
                        .param("from", "0")
                        .param("size", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(itemDtoAfter1.getId()))
                .andExpect(jsonPath("$[0].name").value(itemDtoAfter1.getName()))
                .andExpect(jsonPath("$[0].description").value(itemDtoAfter1.getDescription()))
                .andExpect(jsonPath("$[0].available").value(itemDtoAfter1.getAvailable()))
                .andExpect(jsonPath("$[0].requestId").value(itemDtoAfter1.getRequestId()));
    }

    @Test
    public void addUpdateTest() throws Exception {
        when(itemService.update(anyInt(), anyInt(), any()))
                .thenReturn(getItemDtoAfterUpdate1);

        mvc.perform(patch("/items/{itemId}", "1")
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(itemDtoAfter1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(getItemDtoAfterUpdate1.getId()))
                .andExpect(jsonPath("$.name").value(getItemDtoAfterUpdate1.getName()))
                .andExpect(jsonPath("$.description").value(getItemDtoAfterUpdate1.getDescription()))
                .andExpect(jsonPath("$.available").value(getItemDtoAfterUpdate1.getAvailable()))
                .andExpect(jsonPath("$.requestId").value(getItemDtoAfterUpdate1.getRequestId()));
    }
}
