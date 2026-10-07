package com.aiticket.ai;

import com.aiticket.entity.TicketCategory;
import com.aiticket.entity.TicketPriority;
import com.aiticket.entity.TicketSentiment;
import com.aiticket.exception.AIServiceException;
import org.springframework.stereotype.Component;

@Component
public class AIResponseValidator {

    public void validate(AIResponse response) {

        if (response == null) {
            throw new AIServiceException(
                    "AI service returned an empty response."
            );
        }

        validateRequiredFields(response);

        validateCategory(response.getCategory());
        validatePriority(response.getPriority());
        validateSentiment(response.getSentiment());
    }

    private void validateRequiredFields(AIResponse response) {

        if (isBlank(response.getCategory())) {
            throw new AIServiceException(
                    "AI response is missing category."
            );
        }

        if (isBlank(response.getPriority())) {
            throw new AIServiceException(
                    "AI response is missing priority."
            );
        }

        if (isBlank(response.getSentiment())) {
            throw new AIServiceException(
                    "AI response is missing sentiment."
            );
        }

        if (isBlank(response.getSummary())) {
            throw new AIServiceException(
                    "AI response is missing summary."
            );
        }

        if (isBlank(response.getSuggestedResponse())) {
            throw new AIServiceException(
                    "AI response is missing suggested response."
            );
        }
    }

    private void validateCategory(String category) {

        try {

            TicketCategory.valueOf(
                    category.trim().toUpperCase()
            );

        } catch (IllegalArgumentException e) {

            throw new AIServiceException(
                    "AI returned an invalid category: " + category,
                    e
            );
        }
    }

    private void validatePriority(String priority) {

        try {

            TicketPriority.valueOf(
                    priority.trim().toUpperCase()
            );

        } catch (IllegalArgumentException e) {

            throw new AIServiceException(
                    "AI returned an invalid priority: " + priority,
                    e
            );
        }
    }

    private void validateSentiment(String sentiment) {

        try {

            TicketSentiment.valueOf(
                    sentiment.trim().toUpperCase()
            );

        } catch (IllegalArgumentException e) {

            throw new AIServiceException(
                    "AI returned an invalid sentiment: " + sentiment,
                    e
            );
        }
    }

    private boolean isBlank(String value) {

        return value == null || value.trim().isEmpty();
    }
}