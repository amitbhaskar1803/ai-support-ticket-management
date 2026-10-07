package com.aiticket.ai;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AIResponse {

    private String category;
    private String priority;
    private String sentiment;
    private String summary;
    private String suggestedResponse;
}