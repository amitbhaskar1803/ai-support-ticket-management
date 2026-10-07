package com.aiticket.service.impl;

import com.aiticket.dto.request.LoginRequest;
import com.aiticket.dto.request.RegisterRequest;
import com.aiticket.dto.response.AuthResponse;
import com.aiticket.entity.Role;
import com.aiticket.entity.User;
import com.aiticket.repository.UserRepository;
import com.aiticket.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthServiceImpl authService;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private User user;

    @BeforeEach
    void setUp() {

        registerRequest = new RegisterRequest();

        registerRequest.setName("Amit");

        registerRequest.setEmail(
                "amit@example.com"
        );

        registerRequest.setPassword(
                "Password@123"
        );

        loginRequest = new LoginRequest();

        loginRequest.setEmail(
                "amit@example.com"
        );

        loginRequest.setPassword(
                "Password@123"
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
    void shouldRegisterUserSuccessfully() {

        when(userRepository.existsByEmail(
                "amit@example.com"
        )).thenReturn(false);

        when(passwordEncoder.encode(
                "Password@123"
        )).thenReturn("encodedPassword");

        when(userRepository.save(
                any(User.class)
        )).thenReturn(user);

        AuthResponse response =
                authService.register(registerRequest);

        assertNotNull(response);

        assertEquals(
                1L,
                response.getUserId()
        );

        assertEquals(
                "Amit",
                response.getName()
        );

        assertEquals(
                "amit@example.com",
                response.getEmail()
        );

        assertEquals(
                Role.USER,
                response.getRole()
        );

        assertNull(response.getToken());

        verify(userRepository, times(1))
                .existsByEmail("amit@example.com");

        verify(passwordEncoder, times(1))
                .encode("Password@123");

        verify(userRepository, times(1))
                .save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {

        when(userRepository.existsByEmail(
                "amit@example.com"
        )).thenReturn(true);

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> authService.register(
                                registerRequest
                        )
                );

        assertTrue(
                exception.getMessage()
                        .contains("User already exists")
        );

        verify(userRepository, times(1))
                .existsByEmail("amit@example.com");

        verify(userRepository, never())
                .save(any(User.class));

        verify(passwordEncoder, never())
                .encode(anyString());
    }

    @Test
    void shouldLoginSuccessfully() {

        when(userRepository.findByEmail(
                "amit@example.com"
        )).thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                "Password@123",
                "encodedPassword"
        )).thenReturn(true);

        when(jwtService.generateToken(user))
                .thenReturn("jwt-token");

        AuthResponse response =
                authService.login(loginRequest);

        assertNotNull(response);

        assertEquals(
                1L,
                response.getUserId()
        );

        assertEquals(
                "Amit",
                response.getName()
        );

        assertEquals(
                "amit@example.com",
                response.getEmail()
        );

        assertEquals(
                Role.USER,
                response.getRole()
        );

        assertEquals(
                "jwt-token",
                response.getToken()
        );

        verify(userRepository, times(1))
                .findByEmail("amit@example.com");

        verify(passwordEncoder, times(1))
                .matches(
                        "Password@123",
                        "encodedPassword"
                );

        verify(jwtService, times(1))
                .generateToken(user);
    }

    @Test
    void shouldThrowExceptionWhenLoginEmailDoesNotExist() {

        when(userRepository.findByEmail(
                "amit@example.com"
        )).thenReturn(Optional.empty());

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> authService.login(loginRequest)
                );

        assertEquals(
                "Invalid email or password",
                exception.getMessage()
        );

        verify(userRepository, times(1))
                .findByEmail("amit@example.com");

        verify(passwordEncoder, never())
                .matches(anyString(), anyString());

        verify(jwtService, never())
                .generateToken(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenPasswordIsIncorrect() {

        when(userRepository.findByEmail(
                "amit@example.com"
        )).thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                "Password@123",
                "encodedPassword"
        )).thenReturn(false);

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> authService.login(loginRequest)
                );

        assertEquals(
                "Invalid email or password",
                exception.getMessage()
        );

        verify(userRepository, times(1))
                .findByEmail("amit@example.com");

        verify(passwordEncoder, times(1))
                .matches(
                        "Password@123",
                        "encodedPassword"
                );

        verify(jwtService, never())
                .generateToken(any(User.class));
    }
}