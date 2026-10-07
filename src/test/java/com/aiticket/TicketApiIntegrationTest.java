package com.aiticket;

import com.aiticket.entity.Role;
import com.aiticket.entity.User;
import com.aiticket.repository.UserRepository;
import com.aiticket.security.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "jwt.secret=this-is-a-test-secret-key-that-is-long-enough-for-jwt-testing-123456",
        "jwt.expiration=1000",
        "gemini.api.key=test-gemini-key",
        "gemini.model=gemini-3.6-flash"
})
class TicketApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserRepository userRepository;

    @Test
    void shouldCreateTicketThroughApi() throws Exception {

        User user = User.builder()
                .id(1L)
                .name("Amit")
                .email("amit@example.com")
                .password("encodedPassword")
                .role(Role.USER)
                .build();

        when(jwtService.extractEmail(anyString()))
                .thenReturn("amit@example.com");

        when(userRepository.findByEmail("amit@example.com"))
                .thenReturn(
                        java.util.Optional.of(user)
                );

        when(jwtService.isTokenValid(
                anyString(),
                anyString()
        )).thenReturn(true);

        String requestBody = """
                {
                    "title": "Payment failed",
                    "description": "Payment was deducted but transaction failed.",
                    "customerName": "Amit",
                    "category": "PAYMENT",
                    "priority": "MEDIUM",
                    "status": "OPEN"
                }
                """;

        mockMvc.perform(
                        post("/api/tickets")
                                .header(
                                        "Authorization",
                                        "Bearer test-token"
                                )
                                .contentType(
                                        "application/json"
                                )
                                .content(requestBody)
                )
                .andExpect(status().isCreated());
    }
}