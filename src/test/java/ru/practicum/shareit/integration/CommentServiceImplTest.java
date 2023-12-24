package ru.practicum.shareit.integration;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.StatusBooking;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.CommentServiceImpl;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.item.storage.CommentStorage;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserServiceImpl;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CommentServiceImplTest {
    private final CommentServiceImpl commentService;
    private final CommentStorage commentStorage;
    private final UserServiceImpl userService;
    private final ItemServiceImpl itemService;
    private final BookingServiceImpl bookingService;

    @AfterEach
    void deleteAll() {
        commentStorage.deleteAll();
    }

    @Test
    void addComment() {
        //Юзер создающий запрос на вещь и пишущий комментарий
        UserDto userDtoBefore1 = new UserDto(null, "user1", "user1@mail.ru");
        UserDto userDtoAfter1 = userService.addUser(userDtoBefore1);

        //Юзер владелец итема
        UserDto userDtoBefore2 = new UserDto(null, "user2", "user2@mail.ru");
        UserDto userDtoAfter2 = userService.addUser(userDtoBefore2);

        //Итем владельца юзер2
        ItemDto itemDtoBefore1 = new ItemDto(null, "name1", "description1", true, null);
        ItemDto itemDtoAfter1 = itemService.addItem(userDtoAfter2.getId(), itemDtoBefore1);

        //Букингш от юзера 2 на вещь юзер1
        LocalDateTime start = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).minusMinutes(200);
        LocalDateTime end = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).minusMinutes(100);
        BookingDto bookingDtoBefore1 =
                new BookingDto(null, start, end, StatusBooking.WAITING, userDtoAfter1.getId(),
                        itemDtoAfter1.getId(), itemDtoAfter1.getName());
        BookingResponseDto bookingResponseDtoAfter1 = bookingService.addBooking(userDtoAfter1.getId(), bookingDtoBefore1);
        //Подтверждение статуса букинга
        bookingService.updateStatusBooking(userDtoAfter2.getId(), bookingResponseDtoAfter1.getId(),true);

        LocalDateTime creation = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        CommentDto commentDtoBefore1 = new CommentDto(null, "text1", userDtoAfter1.getName(), creation);
        //Добавляем комментарий
        CommentDto commentDtoAfter1 = commentService.addComment(userDtoAfter1.getId(), itemDtoAfter1.getId(), commentDtoBefore1);

        assertThat(commentDtoAfter1)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(commentDtoBefore1);
    }
}
