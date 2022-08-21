package com.moon.jwtsecurity.controller.dto;

import lombok.Data;

@Data
public class AuthResponse {

    private String email;
    private String accessToken;
}
