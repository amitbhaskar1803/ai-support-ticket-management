package com.aiticket.repository;

import com.aiticket.entity.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    Page<Ticket> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
            String title,
            String description,
            Pageable pageable
    );
}