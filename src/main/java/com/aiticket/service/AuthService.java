package com.aiticket.service;

import com.aiticket.dto.request.LoginRequest;
import com.aiticket.dto.request.RegisterRequest;
import com.aiticket.dto.response.AuthResponse;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}