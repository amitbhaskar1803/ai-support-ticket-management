package com.aiticket.service.impl;

import com.aiticket.dto.request.TicketRequest;
import com.aiticket.dto.response.TicketResponse;
import com.aiticket.entity.Ticket;
import com.aiticket.exception.TicketNotFoundException;
import com.aiticket.repository.TicketRepository;
import com.aiticket.service.TicketService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class TicketServiceImpl implements TicketService {

    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of(
            "id", "title", "createdAt", "updatedAt", "priority", "status"
    );

    private final TicketRepository ticketRepository;

    // CREATE
    @Override
    public TicketResponse createTicket(TicketRequest request) {

        log.info("Creating ticket");

        Ticket ticket = Ticket.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .customerName(request.getCustomerName())
                .category(request.getCategory())
                .priority(request.getPriority())
                .status(request.getStatus())
                .build();

        Ticket savedTicket = ticketRepository.save(ticket);

        log.info("Ticket created with id: {}",
                savedTicket.getId());

        return mapToResponse(savedTicket);
    }

    // GET ALL + PAGINATION + SORTING + SEARCH
    @Override
    public Page<TicketResponse> getAllTickets(
            int page,
            int size,
            String sortBy,
            String direction,
            String search) {

        if (!ALLOWED_SORT_FIELDS.contains(sortBy)) {
            throw new IllegalArgumentException(
                    "Invalid sort field. Allowed fields: "
                            + String.join(", ", ALLOWED_SORT_FIELDS)
            );
        }

        // Prevent invalid page number
        if (page < 0) {
            page = 0;
        }

        // Maximum 100 records per request
        if (size <= 0) {
            size = 10;
        }

        size = Math.min(size, 100);

        // Determine sorting direction
        Sort.Direction sortDirection;

        if ("desc".equalsIgnoreCase(direction)) {
            sortDirection = Sort.Direction.DESC;
        } else {
            sortDirection = Sort.Direction.ASC;
        }

        // Create Pageable object
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(sortDirection, sortBy)
        );

        Page<Ticket> tickets;

        // SEARCH
        if (StringUtils.hasText(search)) {

            log.info("Searching tickets");

            tickets =
                    ticketRepository
                            .findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                                    search,
                                    search,
                                    pageable
                            );

        } else {

            log.info(
                    "Fetching tickets - page: {}, size: {}",
                    page,
                    size
            );

            tickets = ticketRepository.findAll(pageable);
        }

        return tickets.map(this::mapToResponse);
    }

    // GET BY ID
    @Override
    public TicketResponse getTicketById(Long id) {

        log.info("Fetching ticket with id: {}", id);

        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() ->
                        new TicketNotFoundException(
                                "Ticket not found with id: " + id
                        )
                );

        return mapToResponse(ticket);
    }

    // UPDATE
    @Override
    public TicketResponse updateTicket(
            Long id,
            TicketRequest request) {

        log.info("Updating ticket with id: {}", id);

        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() ->
                        new TicketNotFoundException(
                                "Ticket not found with id: " + id
                        )
                );

        ticket.setTitle(request.getTitle());
        ticket.setDescription(request.getDescription());
        ticket.setCustomerName(request.getCustomerName());
        ticket.setCategory(request.getCategory());
        ticket.setPriority(request.getPriority());
        ticket.setStatus(request.getStatus());

        Ticket updatedTicket =
                ticketRepository.save(ticket);

        log.info("Ticket updated with id: {}",
                updatedTicket.getId());

        return mapToResponse(updatedTicket);
    }

    // DELETE
    @Override
    public void deleteTicket(Long id) {

        log.info("Deleting ticket with id: {}", id);

        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() ->
                        new TicketNotFoundException(
                                "Ticket not found with id: " + id
                        )
                );

        ticketRepository.delete(ticket);

        log.info("Ticket deleted with id: {}", id);
    }

    // ENTITY -> RESPONSE DTO
    private TicketResponse mapToResponse(Ticket ticket) {

        return TicketResponse.builder()
                .id(ticket.getId())
                .title(ticket.getTitle())
                .description(ticket.getDescription())
                .customerName(ticket.getCustomerName())
                .category(ticket.getCategory())
                .priority(ticket.getPriority())
                .status(ticket.getStatus())
                .sentiment(ticket.getSentiment())
                .aiSummary(ticket.getAiSummary())
                .aiSuggestedResponse(ticket.getAiSuggestedResponse())
                .createdAt(ticket.getCreatedAt())
                .updatedAt(ticket.getUpdatedAt())
                .build();
    }
}
