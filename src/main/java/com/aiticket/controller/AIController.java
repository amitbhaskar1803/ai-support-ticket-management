package com.aiticket.controller;

import com.aiticket.ai.AIResponse;
import com.aiticket.ai.AIService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
@Tag(name = "Ticket AI", description = "Gemini actions; requires SUPPORT_AGENT or ADMIN role")
@SecurityRequirement(name = "bearerAuth")
public class AIController {

    private final AIService aiService;

    @PostMapping("/{id}/analyze")
    @PreAuthorize("hasAnyRole('SUPPORT_AGENT', 'ADMIN')")
    @Operation(summary = "Analyze a ticket", description = "Requires a valid JWT and SUPPORT_AGENT or ADMIN role.")
    @ApiResponse(responseCode = "200", description = "AI analysis completed")
    @ApiResponse(responseCode = "401", description = "Missing or invalid JWT")
    @ApiResponse(responseCode = "403", description = "Requires SUPPORT_AGENT or ADMIN role")
    @ApiResponse(responseCode = "404", description = "Ticket not found")
    @ApiResponse(responseCode = "503", description = "AI service unavailable")
    public ResponseEntity<AIResponse> analyzeTicket(
            @PathVariable Long id) {

        AIResponse response = aiService.analyzeTicket(id);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/generate-response")
    @PreAuthorize("hasAnyRole('SUPPORT_AGENT', 'ADMIN')")
    @Operation(summary = "Generate a suggested response", description = "Requires a valid JWT and SUPPORT_AGENT or ADMIN role.")
    @ApiResponse(responseCode = "200", description = "Suggested response generated")
    @ApiResponse(responseCode = "401", description = "Missing or invalid JWT")
    @ApiResponse(responseCode = "403", description = "Requires SUPPORT_AGENT or ADMIN role")
    @ApiResponse(responseCode = "404", description = "Ticket not found")
    @ApiResponse(responseCode = "503", description = "AI service unavailable")
    public ResponseEntity<String> generateSuggestedResponse(
            @PathVariable Long id) {

        String response =
                aiService.generateSuggestedResponse(id);

        return ResponseEntity.ok(response);
    }
}
