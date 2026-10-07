package com.aiticket;

import com.aiticket.controller.TicketController;
import com.aiticket.dto.request.TicketRequest;
import com.aiticket.dto.response.TicketResponse;
import com.aiticket.entity.TicketCategory;
import com.aiticket.entity.TicketPriority;
import com.aiticket.entity.TicketStatus;
import com.aiticket.service.TicketService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import com.aiticket.security.JwtAuthenticationFilter;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@WebMvcTest(TicketController.class)
@AutoConfigureMockMvc(addFilters = false)
class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private TicketService ticketService;

    private TicketRequest ticketRequest;

    private TicketResponse ticketResponse;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    void setUp() {

        ticketRequest = new TicketRequest();

        ticketRequest.setTitle(
                "Payment failed"
        );

        ticketRequest.setDescription(
                "Payment was deducted but transaction failed."
        );

        ticketRequest.setCustomerName(
                "Amit"
        );

        ticketRequest.setCategory(
                TicketCategory.PAYMENT
        );

        ticketRequest.setPriority(
                TicketPriority.MEDIUM
        );

        ticketRequest.setStatus(
                TicketStatus.OPEN
        );

        ticketResponse = TicketResponse.builder()
                .id(1L)
                .title("Payment failed")
                .description(
                        "Payment was deducted but transaction failed."
                )
                .customerName("Amit")
                .category(TicketCategory.PAYMENT)
                .priority(TicketPriority.MEDIUM)
                .status(TicketStatus.OPEN)
                .build();
    }

    @Test
    void shouldCreateTicket() throws Exception {

        when(ticketService.createTicket(any(TicketRequest.class)))
                .thenReturn(ticketResponse);

        mockMvc.perform(
                        post("/api/tickets")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                ticketRequest
                                        )
                                )
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title")
                        .value("Payment failed"))
                .andExpect(jsonPath("$.customerName")
                        .value("Amit"))
                .andExpect(jsonPath("$.category")
                        .value("PAYMENT"))
                .andExpect(jsonPath("$.priority")
                        .value("MEDIUM"))
                .andExpect(jsonPath("$.status")
                        .value("OPEN"));

        verify(ticketService, times(1))
                .createTicket(any(TicketRequest.class));
    }

    @Test
    void shouldGetTicketById() throws Exception {

        when(ticketService.getTicketById(1L))
                .thenReturn(ticketResponse);

        mockMvc.perform(
                        get("/api/tickets/1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title")
                        .value("Payment failed"))
                .andExpect(jsonPath("$.customerName")
                        .value("Amit"));

        verify(ticketService, times(1))
                .getTicketById(1L);
    }

    @Test
    void shouldUpdateTicket() throws Exception {

        TicketResponse updatedResponse =
                TicketResponse.builder()
                        .id(1L)
                        .title("Updated Payment Issue")
                        .description(
                                "Updated payment description."
                        )
                        .customerName("Amit")
                        .category(TicketCategory.PAYMENT)
                        .priority(TicketPriority.HIGH)
                        .status(TicketStatus.IN_PROGRESS)
                        .build();

        when(ticketService.updateTicket(
                eq(1L),
                any(TicketRequest.class)
        )).thenReturn(updatedResponse);

        ticketRequest.setTitle(
                "Updated Payment Issue"
        );

        ticketRequest.setDescription(
                "Updated payment description."
        );

        ticketRequest.setPriority(
                TicketPriority.HIGH
        );

        ticketRequest.setStatus(
                TicketStatus.IN_PROGRESS
        );

        mockMvc.perform(
                        put("/api/tickets/1")
                                .contentType(
                                        MediaType.APPLICATION_JSON
                                )
                                .content(
                                        objectMapper.writeValueAsString(
                                                ticketRequest
                                        )
                                )
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title")
                        .value("Updated Payment Issue"))
                .andExpect(jsonPath("$.priority")
                        .value("HIGH"))
                .andExpect(jsonPath("$.status")
                        .value("IN_PROGRESS"));

        verify(ticketService, times(1))
                .updateTicket(
                        eq(1L),
                        any(TicketRequest.class)
                );
    }

    @Test
    void shouldDeleteTicket() throws Exception {

        doNothing()
                .when(ticketService)
                .deleteTicket(1L);

        mockMvc.perform(
                        delete("/api/tickets/1")
                )
                .andExpect(status().isNoContent());

        verify(ticketService, times(1))
                .deleteTicket(1L);
    }

    @Test
    void shouldRejectInvalidCreateRequest() throws Exception {

        TicketRequest invalidRequest =
                new TicketRequest();

        invalidRequest.setTitle("");

        invalidRequest.setDescription("");

        invalidRequest.setCustomerName("");

        mockMvc.perform(
                        post("/api/tickets")
                                .contentType(
                                        MediaType.APPLICATION_JSON
                                )
                                .content(
                                        objectMapper.writeValueAsString(
                                                invalidRequest
                                        )
                                )
                )
                .andExpect(status().isBadRequest());

        verify(
                ticketService,
                never()
        ).createTicket(any(TicketRequest.class));
    }
}