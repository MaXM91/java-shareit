package ru.practicum.shareit.storage;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.TestPropertySource;
import ru.practicum.shareit.request.dto.ItemRequestSimplified;
import ru.practicum.shareit.request.dto.ItemRequestSimplifiedDto;
import ru.practicum.shareit.request.dto.ShortUserDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class ItemRequestStorageTest {
    @Autowired
    ItemRequestStorage itemRequestStorage;

    @Autowired
    UserStorage userStorage;

    @Test
    void saveItemRequestTest() {
        User user = new User(0, "email1@mail.ru", "name1");
        User actualUser = userStorage.save(user);
        LocalDateTime thisTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        ItemRequest itemRequest = new ItemRequest(0, "description1", actualUser, thisTime, null);
        Optional<ItemRequest> actualItemRequest = Optional.of(itemRequestStorage.save(itemRequest));

        Assertions.assertTrue(actualItemRequest.isPresent());
        Assertions.assertNotNull(actualItemRequest.get().getId());

        assertThat(itemRequest)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(actualItemRequest.get());
    }

    @Test
    void findAllByRequesterIdTest() {
        User user = new User(0, "email2@mail.ru", "name2");
        User actualUser = userStorage.save(user);
        LocalDateTime thisTime2 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        ItemRequest itemRequest = new ItemRequest(0, "description2", actualUser, thisTime2, null);
        ItemRequest actualItemRequest = itemRequestStorage.save(itemRequest);

        ShortUserDto shortU = new ShortUserDto(actualUser.getId());
        ItemRequestSimplifiedDto itemRequestSimplifiedDto =
                new ItemRequestSimplifiedDto(1, "description2", shortU, thisTime2, null);

        List<ItemRequestSimplified> simplifild = itemRequestStorage.findAllByRequesterId(actualUser.getId());

        Assertions.assertNotNull(simplifild);

        Assertions.assertEquals("description2", simplifild.get(0).getDescription());
        Assertions.assertEquals(thisTime2, simplifild.get(0).getCreated());
    }

    @Test
    void getItemRequestByIdTest() {
        User user = new User(0, "email3@mail.ru", "name3");
        User actualUser = userStorage.save(user);
        LocalDateTime thisTime2 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        ItemRequest itemRequest = new ItemRequest(0, "description3", actualUser, thisTime2, null);
        ItemRequest actualItemRequest = itemRequestStorage.save(itemRequest);

        ShortUserDto shortU = new ShortUserDto(actualUser.getId());
        ItemRequestSimplifiedDto itemRequestSimplifiedDto =
                new ItemRequestSimplifiedDto(1, "description3", shortU, thisTime2, null);

        ItemRequestSimplified simplifild = itemRequestStorage.getItemRequestById(actualItemRequest.getId());

        Assertions.assertNotNull(simplifild);

        Assertions.assertEquals("description3", simplifild.getDescription());
        Assertions.assertEquals(thisTime2, simplifild.getCreated());
    }

    @Test
    void findAllByTest() {
        User user = new User(0, "email4@mail.ru", "name4");
        User actualUser = userStorage.save(user);
        LocalDateTime thisTime2 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        ItemRequest itemRequest = new ItemRequest(0, "description4", actualUser, thisTime2, null);
        ItemRequest actualItemRequest = itemRequestStorage.save(itemRequest);

        ShortUserDto shortU = new ShortUserDto(actualUser.getId());
        ItemRequestSimplifiedDto itemRequestSimplifiedDto =
                new ItemRequestSimplifiedDto(1, "description4", shortU, thisTime2, null);

        Page<ItemRequestSimplified> simplifild = itemRequestStorage.findAllBy(PageRequest.of(0,
                1, Sort.by(Sort.Direction.DESC, "created")));

        Assertions.assertNotNull(simplifild);

        Assertions.assertEquals("description4", simplifild.getContent().get(0).getDescription());
        Assertions.assertEquals(thisTime2, simplifild.getContent().get(0).getCreated());
    }
}
