package ru.practicum.shareit.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.model.QBooking;
import ru.practicum.shareit.item.model.QComment;
import ru.practicum.shareit.item.model.QItem;
import ru.practicum.shareit.request.model.QItemRequest;
import ru.practicum.shareit.user.model.QUser;

@SpringBootTest
public class QEntityClassTest {
    @Test
    void qClassTest() {
        QUser qUser1 = new QUser("User");
        QUser qUser2 = new QUser("User");

        QItem qItem1 = new QItem("Item");
        QItem qItem2 = new QItem("Item");

        QItemRequest qItemRequest1 = new QItemRequest("QItemRequest");
        QItemRequest qItemRequest2 = new QItemRequest("QItemRequest");

        QBooking qBooking1 = new QBooking("QBooking");
        QBooking qBooking2 = new QBooking("QBooking");

        QComment qComment1 = new QComment("Comment");
        QComment qComment2 = new QComment("Comment");

        Assertions.assertEquals(qUser1.toString(), qUser2.toString());
        Assertions.assertEquals(qItem1.toString(), qItem2.toString());
        Assertions.assertEquals(qItemRequest1.toString(), qItemRequest2.toString());
        Assertions.assertEquals(qBooking1.toString(), qBooking2.toString());
        Assertions.assertEquals(qComment1.toString(), qComment2.toString());
    }
}
