package com.aiticket.ai;

public interface AIService {

    AIResponse analyzeTicket(Long ticketId);

    String generateSuggestedResponse(Long ticketId);
}