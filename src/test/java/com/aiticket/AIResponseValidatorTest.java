package com.aiticket;

import com.aiticket.ai.AIResponse;
import com.aiticket.ai.AIResponseValidator;
import com.aiticket.exception.AIServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AIResponseValidatorTest {

    private AIResponseValidator validator;

    @BeforeEach
    void setUp() {
        validator = new AIResponseValidator();
    }

    @Test
    void shouldValidateCorrectAIResponse() {

        AIResponse response = new AIResponse();

        response.setCategory("PAYMENT");
        response.setPriority("HIGH");
        response.setSentiment("NEGATIVE");
        response.setSummary("Payment failed after amount was deducted.");
        response.setSuggestedResponse(
                "We apologize for the inconvenience and will investigate your transaction."
        );

        assertDoesNotThrow(() ->
                validator.validate(response)
        );
    }

    @Test
    void shouldRejectNullResponse() {

        assertThrows(
                AIServiceException.class,
                () -> validator.validate(null)
        );
    }

    @Test
    void shouldRejectMissingCategory() {

        AIResponse response = createValidResponse();

        response.setCategory(null);

        assertThrows(
                AIServiceException.class,
                () -> validator.validate(response)
        );
    }

    @Test
    void shouldRejectInvalidCategory() {

        AIResponse response = createValidResponse();

        response.setCategory("BANKING");

        assertThrows(
                AIServiceException.class,
                () -> validator.validate(response)
        );
    }

    @Test
    void shouldRejectInvalidPriority() {

        AIResponse response = createValidResponse();

        response.setPriority("URGENT");

        assertThrows(
                AIServiceException.class,
                () -> validator.validate(response)
        );
    }

    @Test
    void shouldRejectInvalidSentiment() {

        AIResponse response = createValidResponse();

        response.setSentiment("ANGRY");

        assertThrows(
                AIServiceException.class,
                () -> validator.validate(response)
        );
    }

    @Test
    void shouldRejectMissingSummary() {

        AIResponse response = createValidResponse();

        response.setSummary(null);

        assertThrows(
                AIServiceException.class,
                () -> validator.validate(response)
        );
    }

    @Test
    void shouldRejectMissingSuggestedResponse() {

        AIResponse response = createValidResponse();

        response.setSuggestedResponse(null);

        assertThrows(
                AIServiceException.class,
                () -> validator.validate(response)
        );
    }
    @Test
    void shouldAcceptLowercaseEnumValues() {

        AIResponse response = new AIResponse();

        response.setCategory("payment");
        response.setPriority("high");
        response.setSentiment("negative");
        response.setSummary("Payment failed.");
        response.setSuggestedResponse(
                "We will investigate your payment issue."
        );

        assertDoesNotThrow(() ->
                validator.validate(response)
        );
    }

    private AIResponse createValidResponse() {

        AIResponse response = new AIResponse();

        response.setCategory("PAYMENT");
        response.setPriority("HIGH");
        response.setSentiment("NEGATIVE");
        response.setSummary("Payment failed after amount was deducted.");
        response.setSuggestedResponse(
                "We apologize for the inconvenience and will investigate your transaction."
        );

        return response;
    }
}