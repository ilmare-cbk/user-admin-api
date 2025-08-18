package com.cbk.user_admin_api.application.service;

import com.cbk.user_admin_api.application.command.LoginCommand;
import com.cbk.user_admin_api.domain.User;
import com.cbk.user_admin_api.domain.UserQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserQueryRepository userQueryRepository;
    private final TokenProvider tokenProvider;

    public String login(LoginCommand command) {
        User user = userQueryRepository.findByUserId(command.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.matchedPassword(command.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return tokenProvider.createToken(command.getUserId());
    }
}
