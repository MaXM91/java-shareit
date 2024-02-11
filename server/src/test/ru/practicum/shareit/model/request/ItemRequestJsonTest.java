package ru.practicum.shareit.model.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class ItemRequestJsonTest {
    @Autowired
    JacksonTester<ItemRequest> json;

    @Test
    void itemRequestJsonTest() throws Exception {
        User user1 = new User(1, "user@mail.ru", "name1");
        Item item1 = new Item(1, "name1", "description1",true, 5, null);
        ItemRequest itemRequest1 =
                new ItemRequest(2, "description1", user1,
                        LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS), List.of(item1));

        JsonContent<ItemRequest> result = json.write(itemRequest1);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(2);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("description1");
        assertThat(result).extractingJsonPathNumberValue("$.requester.id").isEqualTo(user1.getId());
        assertThat(result).extractingJsonPathStringValue("$.requester.name").isEqualTo(user1.getName());
        assertThat(result).extractingJsonPathStringValue("$.requester.email").isEqualTo(user1.getEmail());
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo(itemRequest1.getCreated().toString());
        assertThat(result).extractingJsonPathNumberValue("$.items[0].id").isEqualTo(item1.getId());
        assertThat(result).extractingJsonPathStringValue("$.items[0].name").isEqualTo(item1.getName());
        assertThat(result).extractingJsonPathStringValue("$.items[0].description").isEqualTo(item1.getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.items[0].available").isEqualTo(item1.getAvailable());
        assertThat(result).extractingJsonPathNumberValue("$.items[0].ownerId").isEqualTo(item1.getOwnerId());
        assertThat(result).extractingJsonPathValue("$.items[0].request").isEqualTo(item1.getRequest());
    }
}
