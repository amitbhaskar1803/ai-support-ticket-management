package com.aiticket;

import com.aiticket.entity.Role;
import com.aiticket.entity.User;
import com.aiticket.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;

    private User user;

    @BeforeEach
    void setUp() {

        jwtService = new JwtService(
                "this-is-a-very-long-secret-key-for-testing-jwt-service-123456",
                3600000L
        );

        user = User.builder()
                .id(1L)
                .name("Amit")
                .email("amit@example.com")
                .password("encodedPassword")
                .role(Role.USER)
                .build();
    }

    @Test
    void shouldGenerateToken() {

        String token =
                jwtService.generateToken(user);

        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    void shouldExtractEmailFromToken() {

        String token =
                jwtService.generateToken(user);

        String email =
                jwtService.extractEmail(token);

        assertEquals(
                "amit@example.com",
                email
        );
    }

    @Test
    void shouldValidateToken() {

        String token =
                jwtService.generateToken(user);

        boolean valid =
                jwtService.isTokenValid(
                        token,
                        "amit@example.com"
                );

        assertTrue(valid);
    }

    @Test
    void shouldRejectTokenForDifferentEmail() {

        String token =
                jwtService.generateToken(user);

        boolean valid =
                jwtService.isTokenValid(
                        token,
                        "other@example.com"
                );

        assertFalse(valid);
    }
}