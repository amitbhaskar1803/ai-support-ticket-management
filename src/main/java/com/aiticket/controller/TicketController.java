package com.aiticket.controller;

import com.aiticket.dto.request.TicketRequest;
import com.aiticket.dto.response.TicketResponse;
import com.aiticket.service.TicketService;
import org.springframework.security.access.prepost.PreAuthorize;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
@Tag(name = "Tickets", description = "Ticket operations; access is controlled by role")
@SecurityRequirement(name = "bearerAuth")
public class TicketController {

    private final TicketService ticketService;

    // CREATE
    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'SUPPORT_AGENT', 'ADMIN')")
    @Operation(summary = "Create a ticket", description = "Requires a valid JWT and USER, SUPPORT_AGENT, or ADMIN role.")
    @ApiResponse(responseCode = "201", description = "Ticket created")
    @ApiResponse(responseCode = "401", description = "Missing or invalid JWT")
    @ApiResponse(responseCode = "403", description = "Role not permitted")
    @ApiResponse(responseCode = "400", description = "Invalid ticket data")
    public ResponseEntity<TicketResponse> createTicket(
            @Valid @RequestBody TicketRequest request) {

        TicketResponse response =
                ticketService.createTicket(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // GET ALL + PAGINATION + SORTING + SEARCH
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'SUPPORT_AGENT', 'ADMIN')")
    @Operation(summary = "List tickets", description = "Requires a valid JWT and USER, SUPPORT_AGENT, or ADMIN role. Supports paging, sorting, and search.")
    @ApiResponse(responseCode = "200", description = "Tickets returned")
    @ApiResponse(responseCode = "400", description = "Invalid sort field")
    @ApiResponse(responseCode = "401", description = "Missing or invalid JWT")
    @ApiResponse(responseCode = "403", description = "Role not permitted")
    public ResponseEntity<Page<TicketResponse>> getAllTickets(

            @RequestParam(
                    defaultValue = "0"
            )
            int page,

            @RequestParam(
                    defaultValue = "10"
            )
            int size,

            @RequestParam(
                    defaultValue = "createdAt"
            )
            String sortBy,

            @RequestParam(
                    defaultValue = "desc"
            )
            String direction,

            @RequestParam(
                    required = false
            )
            String search) {

        Page<TicketResponse> response =
                ticketService.getAllTickets(
                        page,
                        size,
                        sortBy,
                        direction,
                        search
                );

        return ResponseEntity.ok(response);
    }

    // GET BY ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'SUPPORT_AGENT', 'ADMIN')")
    @Operation(summary = "Get a ticket", description = "Requires a valid JWT and USER, SUPPORT_AGENT, or ADMIN role.")
    @ApiResponse(responseCode = "200", description = "Ticket returned")
    @ApiResponse(responseCode = "401", description = "Missing or invalid JWT")
    @ApiResponse(responseCode = "403", description = "Role not permitted")
    @ApiResponse(responseCode = "404", description = "Ticket not found")
    public ResponseEntity<TicketResponse> getTicketById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                ticketService.getTicketById(id)
        );
    }

    // UPDATE
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPPORT_AGENT', 'ADMIN')")
    @Operation(summary = "Update a ticket", description = "Requires a valid JWT and SUPPORT_AGENT or ADMIN role.")
    @ApiResponse(responseCode = "200", description = "Ticket updated")
    @ApiResponse(responseCode = "400", description = "Invalid ticket data")
    @ApiResponse(responseCode = "401", description = "Missing or invalid JWT")
    @ApiResponse(responseCode = "403", description = "Requires SUPPORT_AGENT or ADMIN role")
    @ApiResponse(responseCode = "404", description = "Ticket not found")
    public ResponseEntity<TicketResponse> updateTicket(
            @PathVariable Long id,
            @Valid @RequestBody TicketRequest request) {

        return ResponseEntity.ok(
                ticketService.updateTicket(
                        id,
                        request
                )
        );
    }

    // DELETE
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a ticket", description = "Requires a valid JWT and ADMIN role.")
    @ApiResponse(responseCode = "204", description = "Ticket deleted")
    @ApiResponse(responseCode = "401", description = "Missing or invalid JWT")
    @ApiResponse(responseCode = "403", description = "Requires ADMIN role")
    @ApiResponse(responseCode = "404", description = "Ticket not found")
    public ResponseEntity<Void> deleteTicket(
            @PathVariable Long id) {

        ticketService.deleteTicket(id);

        return ResponseEntity
                .noContent()
                .build();
    }
}
