package com.cbk.user_admin_api.application.service;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

public interface TokenProvider {
    String createToken(String userId);

    String getUserId(String token);

    boolean validateToken(String token);

    Optional<String> resolveToken(HttpServletRequest request);
}
