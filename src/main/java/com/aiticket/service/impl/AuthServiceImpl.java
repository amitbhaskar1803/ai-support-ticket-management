package com.aiticket.service.impl;

import com.aiticket.dto.request.LoginRequest;
import com.aiticket.dto.request.RegisterRequest;
import com.aiticket.dto.response.AuthResponse;
import com.aiticket.entity.Role;
import com.aiticket.entity.User;
import com.aiticket.repository.UserRepository;
import com.aiticket.security.JwtService;
import com.aiticket.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public AuthResponse register(RegisterRequest request) {

        String email = request.getEmail()
                .trim()
                .toLowerCase();

        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException(
                    "User already exists with email: " + email
            );
        }

        User user = User.builder()
                .name(request.getName())
                .email(email)
                .password(
                        passwordEncoder.encode(
                                request.getPassword()
                        )
                )
                .role(Role.USER)
                .build();

        User savedUser = userRepository.save(user);

        return AuthResponse.builder()
                .userId(savedUser.getId())
                .name(savedUser.getName())
                .email(savedUser.getEmail())
                .role(savedUser.getRole())
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        String email = request.getEmail()
                .trim()
                .toLowerCase();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Invalid email or password"
                        )
                );

        boolean passwordMatches =
                passwordEncoder.matches(
                        request.getPassword(),
                        user.getPassword()
                );

        if (!passwordMatches) {
            throw new IllegalArgumentException(
                    "Invalid email or password"
            );
        }

        String token = jwtService.generateToken(user);

        return AuthResponse.builder()
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .token(token)
                .build();
    }
}