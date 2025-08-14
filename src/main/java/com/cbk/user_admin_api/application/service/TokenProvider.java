package com.cbk.user_admin_api.application.service;

public interface TokenProvider {
    String createToken(String userId);

    String getUserId(String token);

    boolean validateToken(String token);
}
