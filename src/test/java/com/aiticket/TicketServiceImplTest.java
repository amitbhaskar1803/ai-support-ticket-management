package com.aiticket;
import com.aiticket.service.impl.TicketServiceImpl;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import java.util.List;
import java.util.Optional;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.Pageable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import com.aiticket.dto.request.TicketRequest;
import com.aiticket.dto.response.TicketResponse;
import com.aiticket.entity.Ticket;
import com.aiticket.entity.TicketCategory;
import com.aiticket.entity.TicketPriority;
import com.aiticket.entity.TicketStatus;
import com.aiticket.exception.TicketNotFoundException;
import com.aiticket.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
@ExtendWith(MockitoExtension.class)
class TicketServiceImplTest {

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private TicketServiceImpl ticketService;

    private TicketRequest ticketRequest;
    private Ticket ticket;

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

        ticket = Ticket.builder()
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
    void shouldCreateTicket() {

        when(ticketRepository.save(any(Ticket.class)))
                .thenReturn(ticket);

        TicketResponse response =
                ticketService.createTicket(ticketRequest);

        assertNotNull(response);

        assertEquals(
                1L,
                response.getId()
        );

        assertEquals(
                "Payment failed",
                response.getTitle()
        );

        assertEquals(
                TicketCategory.PAYMENT,
                response.getCategory()
        );

        verify(ticketRepository, times(1))
                .save(any(Ticket.class));
    }

    @Test
    void shouldGetTicketById() {

        when(ticketRepository.findById(1L))
                .thenReturn(
                        java.util.Optional.of(ticket)
                );

        TicketResponse response =
                ticketService.getTicketById(1L);

        assertNotNull(response);

        assertEquals(
                1L,
                response.getId()
        );

        assertEquals(
                "Amit",
                response.getCustomerName()
        );

        verify(ticketRepository, times(1))
                .findById(1L);
    }

    @Test
    void shouldRejectUnsupportedSortField() {
        assertThrows(IllegalArgumentException.class,
                () -> ticketService.getAllTickets(0, 10, "password", "asc", null));

        verifyNoInteractions(ticketRepository);
    }

    @Test
    void shouldThrowExceptionWhenTicketDoesNotExist() {

        when(ticketRepository.findById(999L))
                .thenReturn(
                        java.util.Optional.empty()
                );

        assertThrows(
                TicketNotFoundException.class,
                () -> ticketService.getTicketById(999L)
        );

        verify(ticketRepository, times(1))
                .findById(999L);
    }

    @Test
    void shouldDeleteTicket() {

        when(ticketRepository.findById(1L))
                .thenReturn(
                        java.util.Optional.of(ticket)
                );

        ticketService.deleteTicket(1L);

        verify(ticketRepository, times(1))
                .findById(1L);

        verify(ticketRepository, times(1))
                .delete(ticket);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingTicket() {

        when(ticketRepository.findById(999L))
                .thenReturn(
                        java.util.Optional.empty()
                );

        assertThrows(
                TicketNotFoundException.class,
                () -> ticketService.deleteTicket(999L)
        );

        verify(ticketRepository, never())
                .delete(any(Ticket.class));
    }
    @Test
    void shouldUpdateTicket() {

        Ticket updatedTicket = Ticket.builder()
                .id(1L)
                .title("Updated Payment Issue")
                .description("Payment issue has been updated.")
                .customerName("Amit")
                .category(TicketCategory.PAYMENT)
                .priority(TicketPriority.HIGH)
                .status(TicketStatus.IN_PROGRESS)
                .build();

        ticketRequest.setTitle("Updated Payment Issue");
        ticketRequest.setDescription(
                "Payment issue has been updated."
        );
        ticketRequest.setPriority(
                TicketPriority.HIGH
        );
        ticketRequest.setStatus(
                TicketStatus.IN_PROGRESS
        );

        when(ticketRepository.findById(1L))
                .thenReturn(Optional.of(ticket));

        when(ticketRepository.save(any(Ticket.class)))
                .thenReturn(updatedTicket);

        TicketResponse response =
                ticketService.updateTicket(
                        1L,
                        ticketRequest
                );

        assertNotNull(response);

        assertEquals(
                1L,
                response.getId()
        );

        assertEquals(
                "Updated Payment Issue",
                response.getTitle()
        );

        assertEquals(
                TicketPriority.HIGH,
                response.getPriority()
        );

        assertEquals(
                TicketStatus.IN_PROGRESS,
                response.getStatus()
        );

        verify(ticketRepository, times(1))
                .findById(1L);

        verify(ticketRepository, times(1))
                .save(any(Ticket.class));
    }
    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingTicket() {

        when(ticketRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                TicketNotFoundException.class,
                () -> ticketService.updateTicket(
                        999L,
                        ticketRequest
                )
        );

        verify(ticketRepository, times(1))
                .findById(999L);

        verify(ticketRepository, never())
                .save(any(Ticket.class));
    }
    @Test
    void shouldGetAllTicketsWithPagination() {

        PageImpl<Ticket> ticketPage =
                new PageImpl<>(
                        List.of(ticket)
                );

        when(ticketRepository.findAll(any(PageRequest.class)))
                .thenReturn(ticketPage);

        var response =
                ticketService.getAllTickets(
                        0,
                        10,
                        "createdAt",
                        "desc",
                        null
                );

        assertNotNull(response);

        assertEquals(
                1,
                response.getTotalElements()
        );

        assertEquals(
                1,
                response.getContent().size()
        );

        assertEquals(
                "Payment failed",
                response.getContent()
                        .get(0)
                        .getTitle()
        );

        verify(ticketRepository, times(1))
                .findAll(any(PageRequest.class));
    }
    @Test
    void shouldSearchTickets() {

        PageImpl<Ticket> ticketPage =
                new PageImpl<>(
                        List.of(ticket)
                );

        when(
                ticketRepository
                        .findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                                eq("payment"),
                                eq("payment"),
                                any(PageRequest.class)
                        )
        ).thenReturn(ticketPage);

        var response =
                ticketService.getAllTickets(
                        0,
                        10,
                        "createdAt",
                        "desc",
                        "payment"
                );

        assertNotNull(response);

        assertEquals(
                1,
                response.getTotalElements()
        );

        assertEquals(
                "Payment failed",
                response.getContent()
                        .get(0)
                        .getTitle()
        );

        verify(
                ticketRepository,
                times(1)
        ).findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                eq("payment"),
                eq("payment"),
                any(PageRequest.class)
        );

        verify(
                ticketRepository,
                never()
        ).findAll(any(PageRequest.class));
    }
    @Test
    void shouldLimitPageSizeTo100() {

        PageImpl<Ticket> ticketPage =
                new PageImpl<>(List.of(ticket));

        when(ticketRepository.findAll(any(Pageable.class)))
                .thenReturn(ticketPage);

        ticketService.getAllTickets(
                0,
                500,
                "createdAt",
                "desc",
                null
        );

        ArgumentCaptor<Pageable> pageableCaptor =
                ArgumentCaptor.forClass(Pageable.class);

        verify(ticketRepository, times(1))
                .findAll(pageableCaptor.capture());

        Pageable pageable =
                pageableCaptor.getValue();

        assertEquals(
                100,
                pageable.getPageSize()
        );
    }
}
