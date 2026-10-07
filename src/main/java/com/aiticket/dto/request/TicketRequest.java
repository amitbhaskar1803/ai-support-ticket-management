package com.aiticket.dto.request;

import com.aiticket.entity.TicketCategory;
import com.aiticket.entity.TicketPriority;
import com.aiticket.entity.TicketStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketRequest {

    @NotBlank(message = "Title is required")
    @Size(max = 150,message = "Title must not exceed 150 characters")
    private String title;
    @NotBlank(message = "Description is required")
    @Size(max = 5000,message = "Description must not exceed 5000 characters")
    private String description;
    @NotBlank(message = "Customer name is required")
    @Size(max = 100,message = "Customer name must not exceed 100 characters")
    private String customerName;
    @NotNull(message = "Category is required")
    private TicketCategory category;
    @NotNull(message = "Priority is required")
    private TicketPriority priority;
    @NotNull(message = "Status is required")
    private TicketStatus status;


}
