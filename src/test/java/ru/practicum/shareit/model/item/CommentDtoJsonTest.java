package ru.practicum.shareit.model.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.CommentDto;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class CommentDtoJsonTest {
    @Autowired
    JacksonTester<CommentDto> json;

    @Test
    void commentDtoJsonTest() throws Exception {
        LocalDateTime created1 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        CommentDto commentDto1 = new CommentDto(1, "text1", "name1", created1);

        JsonContent<CommentDto> result = json.write(commentDto1);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo("text1");
        assertThat(result).extractingJsonPathStringValue("$.authorName").isEqualTo("name1");
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo(commentDto1.getCreated().toString());
    }
}
