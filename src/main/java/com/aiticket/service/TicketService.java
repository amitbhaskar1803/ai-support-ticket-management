package com.aiticket.service;

import com.aiticket.dto.request.TicketRequest;
import com.aiticket.dto.response.TicketResponse;
import org.springframework.data.domain.Page;

public interface TicketService {

   TicketResponse createTicket(TicketRequest request);

   Page<TicketResponse> getAllTickets(
           int page,
           int size,
           String sortBy,
           String direction,
           String search
   );

   TicketResponse getTicketById(Long id);

   TicketResponse updateTicket(Long id, TicketRequest request);

   void deleteTicket(Long id);
}