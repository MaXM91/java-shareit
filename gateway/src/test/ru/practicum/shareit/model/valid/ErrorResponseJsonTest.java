package ru.practicum.shareit.model.valid;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.validation.ErrorResponse;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class ErrorResponseJsonTest {
    @Autowired
    JacksonTester<ErrorResponse> json;

    @Test
    void errorResponseJsonTest() throws Exception {
        ErrorResponse errorResponse1 = new ErrorResponse("Bad request");

        JsonContent<ErrorResponse> result = json.write(errorResponse1);

        assertThat(result).extractingJsonPathStringValue("$.error").isEqualTo("Bad request");
    }
}
