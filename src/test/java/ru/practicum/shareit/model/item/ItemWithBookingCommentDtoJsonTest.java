package ru.practicum.shareit.model.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.BookingForItemDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemWithBookingCommentDto;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class ItemWithBookingCommentDtoJsonTest {
    @Autowired
    JacksonTester<ItemWithBookingCommentDto> json;

    @Test
    void itemWithBookingCommentDtoJsonTest() throws Exception {
        LocalDateTime start1 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(50);
        LocalDateTime end1 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(250);
        BookingForItemDto last1 = new BookingForItemDto(1, 2, start1, end1, 5);

        LocalDateTime start2 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(350);
        LocalDateTime end2 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(450);
        BookingForItemDto next1 = new BookingForItemDto(2, 2, start2, end2, 5);

        LocalDateTime created1 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        CommentDto commentDto1 = new CommentDto(1, "text1", "name1", created1);

        ItemWithBookingCommentDto itemWithBookingDto1 = new ItemWithBookingCommentDto(1, "name1", "description1",
                true, last1, next1 , List.of(commentDto1));

        JsonContent<ItemWithBookingCommentDto> result = json.write(itemWithBookingDto1);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("name1");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("description1");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
        assertThat(result).extractingJsonPathNumberValue("$.lastBooking.id").isEqualTo(last1.getId());
        assertThat(result).extractingJsonPathNumberValue("$.lastBooking.bookerId").isEqualTo(last1.getBookerId());
        assertThat(result).extractingJsonPathStringValue("$.lastBooking.start").isEqualTo(last1.getStart().toString());
        assertThat(result).extractingJsonPathStringValue("$.lastBooking.end").isEqualTo(last1.getEnd().toString());
        assertThat(result).extractingJsonPathNumberValue("$.lastBooking.itemId").isEqualTo(last1.getItemId());
        assertThat(result).extractingJsonPathNumberValue("$.nextBooking.id").isEqualTo(next1.getId());
        assertThat(result).extractingJsonPathNumberValue("$.nextBooking.bookerId").isEqualTo(next1.getBookerId());
        assertThat(result).extractingJsonPathStringValue("$.nextBooking.start").isEqualTo(next1.getStart().toString());
        assertThat(result).extractingJsonPathStringValue("$.nextBooking.end").isEqualTo(next1.getEnd().toString());
        assertThat(result).extractingJsonPathNumberValue("$.nextBooking.itemId").isEqualTo(next1.getItemId());
        assertThat(result).extractingJsonPathNumberValue("$.comments[0].id").isEqualTo(commentDto1.getId());
        assertThat(result).extractingJsonPathStringValue("$.comments[0].text").isEqualTo(commentDto1.getText());
        assertThat(result).extractingJsonPathStringValue("$.comments[0].authorName").isEqualTo(commentDto1.getAuthorName());
        assertThat(result).extractingJsonPathStringValue("$.comments[0].created").isEqualTo(commentDto1.getCreated().toString());
    }
}
