package com.aiticket;

import com.aiticket.entity.Ticket;
import com.aiticket.entity.TicketCategory;
import com.aiticket.entity.TicketPriority;
import com.aiticket.entity.TicketStatus;
import com.aiticket.repository.TicketRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
class TicketRepositoryIntegrationTest {

    @Autowired
    private TicketRepository ticketRepository;

    @Test
    void shouldSaveAndFindTicket() {

        Ticket ticket = Ticket.builder()
                .title("Integration test payment issue")
                .description(
                        "Payment was deducted but transaction failed."
                )
                .customerName("Integration Test User")
                .category(TicketCategory.PAYMENT)
                .priority(TicketPriority.HIGH)
                .status(TicketStatus.OPEN)
                .build();

        Ticket savedTicket =
                ticketRepository.save(ticket);

        assertNotNull(savedTicket.getId());

        Ticket foundTicket =
                ticketRepository.findById(
                        savedTicket.getId()
                ).orElseThrow();

        assertEquals(
                "Integration test payment issue",
                foundTicket.getTitle()
        );

        assertEquals(
                TicketCategory.PAYMENT,
                foundTicket.getCategory()
        );

        assertEquals(
                TicketPriority.HIGH,
                foundTicket.getPriority()
        );

        assertEquals(
                TicketStatus.OPEN,
                foundTicket.getStatus()
        );
    }

    @Test
    void shouldSearchTickets() {

        Ticket ticket = Ticket.builder()
                .title("Payment integration issue")
                .description(
                        "Customer payment failed during checkout."
                )
                .customerName("Test Customer")
                .category(TicketCategory.PAYMENT)
                .priority(TicketPriority.MEDIUM)
                .status(TicketStatus.OPEN)
                .build();

        ticketRepository.save(ticket);

        Page<Ticket> result =
                ticketRepository
                        .findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                                "payment",
                                "payment",
                                PageRequest.of(0, 10)
                        );

        assertFalse(result.isEmpty());

        assertTrue(
                result.getContent()
                        .stream()
                        .anyMatch(
                                t -> t.getTitle()
                                        .contains("Payment")
                        )
        );
    }
}