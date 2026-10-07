package com.aiticket;

import com.aiticket.repository.UserRepository;
import com.aiticket.repository.TicketRepository;
import com.aiticket.entity.Role;
import com.aiticket.entity.User;
import com.aiticket.service.TicketService;
import com.aiticket.ai.AIService;
import com.aiticket.security.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "spring.autoconfigure.exclude=org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration,org.springframework.boot.hibernate.autoconfigure.HibernateJpaAutoConfiguration,org.springframework.boot.data.jpa.autoconfigure.DataJpaRepositoriesAutoConfiguration",
        "jwt.secret=this-is-a-test-secret-key-that-is-long-enough-for-jwt-testing-123456",
        "jwt.expiration=1000",
        "gemini.api.key=test-gemini-key",
        "gemini.model=gemini-3.6-flash"
})
class SecurityIntegrationTest {

    private static final String TEST_BEARER = "Bearer test-token";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TicketService ticketService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private TicketRepository ticketRepository;

    @MockitoBean
    private AIService aiService;

    @Test
    void shouldReturn401WhenJwtIsMissing() throws Exception {

        mockMvc.perform(
                        get("/api/tickets")
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturn401WhenJwtIsInvalid() throws Exception {

        when(jwtService.extractEmail(anyString()))
                .thenThrow(
                        new RuntimeException("Invalid JWT")
                );

        mockMvc.perform(
                        get("/api/tickets")
                                .header(
                                        "Authorization",
                                        "Bearer invalid-token"
                                )
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldExposeOpenApiAndSwaggerWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.info.title")
                        .value("AI Support Ticket Management System API"))
                .andExpect(jsonPath("$.components.securitySchemes.bearerAuth.scheme")
                        .value("bearer"));

        mockMvc.perform(get("/swagger-ui/index.html"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldKeepHealthEndpointPublic() throws Exception {
        mockMvc.perform(get("/actuator/health"))
                .andExpect(status().isOk());
    }

    @Test
    void userCanReadTicketsButCannotUpdateOrUseAi() throws Exception {
        authorizeAs(Role.USER);

        mockMvc.perform(get("/api/tickets").header("Authorization", TEST_BEARER))
                .andExpect(status().isOk());
        mockMvc.perform(post("/api/tickets")
                        .header("Authorization", TEST_BEARER)
                        .contentType("application/json")
                        .content("""
                                {"title":"test","description":"test","customerName":"test","category":"OTHER","priority":"LOW","status":"OPEN"}
                                """))
                .andExpect(status().isCreated());
        mockMvc.perform(put("/api/tickets/1").header("Authorization", TEST_BEARER)
                        .contentType("application/json")
                        .content("""
                                {"title":"test","description":"test","customerName":"test","category":"OTHER","priority":"LOW","status":"OPEN"}
                                """))
                .andExpect(status().isForbidden());
        mockMvc.perform(post("/api/tickets/1/analyze").header("Authorization", TEST_BEARER))
                .andExpect(status().isForbidden());
        mockMvc.perform(delete("/api/tickets/1").header("Authorization", TEST_BEARER))
                .andExpect(status().isForbidden());
    }

    @Test
    void supportAgentCannotDeleteTickets() throws Exception {
        authorizeAs(Role.SUPPORT_AGENT);

        mockMvc.perform(put("/api/tickets/1").header("Authorization", TEST_BEARER)
                        .contentType("application/json")
                        .content("""
                                {"title":"test","description":"test","customerName":"test","category":"OTHER","priority":"LOW","status":"OPEN"}
                                """))
                .andExpect(status().isOk());
        mockMvc.perform(post("/api/tickets/1/analyze").header("Authorization", TEST_BEARER))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/api/tickets/1").header("Authorization", TEST_BEARER))
                .andExpect(status().isForbidden());
    }

    @Test
    void adminCanDeleteAndAnalyzeTickets() throws Exception {
        authorizeAs(Role.ADMIN);

        mockMvc.perform(delete("/api/tickets/1").header("Authorization", TEST_BEARER))
                .andExpect(status().isNoContent());
        mockMvc.perform(post("/api/tickets/1/analyze").header("Authorization", TEST_BEARER))
                .andExpect(status().isOk());
    }

    private void authorizeAs(Role role) {
        User user = User.builder()
                .id(1L)
                .name("Test User")
                .email("test@example.com")
                .password("encoded")
                .role(role)
                .build();

        when(jwtService.extractEmail(anyString())).thenReturn(user.getEmail());
        when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(java.util.Optional.of(user));
        when(jwtService.isTokenValid(anyString(), anyString())).thenReturn(true);
        when(ticketService.getAllTickets(
                org.mockito.ArgumentMatchers.anyInt(),
                org.mockito.ArgumentMatchers.anyInt(),
                anyString(), anyString(),
                org.mockito.ArgumentMatchers.isNull()))
                .thenReturn(org.springframework.data.domain.Page.empty());
    }
}
