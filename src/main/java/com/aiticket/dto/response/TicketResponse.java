package com.aiticket.dto.response;

import com.aiticket.entity.TicketCategory;
import com.aiticket.entity.TicketPriority;
import com.aiticket.entity.TicketSentiment;
import com.aiticket.entity.TicketStatus;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class TicketResponse {

    private Long id;

    private String title;

    private String description;

    private String customerName;

    private TicketCategory category;

    private TicketPriority priority;

    private TicketStatus status;

    private TicketSentiment sentiment;

    private String aiSummary;

    private String aiSuggestedResponse;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}