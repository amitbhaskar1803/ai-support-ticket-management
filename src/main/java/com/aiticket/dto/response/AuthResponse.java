package com.aiticket.dto.response;

import com.aiticket.entity.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthResponse {

    private Long userId;
    private String name;
    private String email;
    private Role role;
    private String token;
}