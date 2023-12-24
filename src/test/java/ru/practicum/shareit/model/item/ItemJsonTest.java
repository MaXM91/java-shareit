package ru.practicum.shareit.model.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.model.Item;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
@JsonTest
public class ItemJsonTest {
    @Autowired
    JacksonTester<Item> json;

    @Test
    void itemJsonTest() throws Exception {
        Item item1 = new Item(
                1,
                "name1",
                "description1",
                true,
                1,
                null);

        JsonContent<Item> result = json.write(item1);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("name1");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("description1");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
        assertThat(result).extractingJsonPathNumberValue("$.ownerId").isEqualTo(1);
        assertThat(result).extractingJsonPathValue("$.requester").isEqualTo(null);
    }
}
