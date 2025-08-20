package com.cbk.user_admin_api.application.service;

import com.cbk.user_admin_api.application.command.LoginCommand;
import com.cbk.user_admin_api.application.exception.ApplicationException;
import com.cbk.user_admin_api.domain.User;
import com.cbk.user_admin_api.domain.UserQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserQueryRepository userQueryRepository;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;

    public String login(LoginCommand command) {
        User user = userQueryRepository.findByUserId(command.getUserId())
                .orElseThrow(() -> new ApplicationException("User not found"));

        if (!passwordEncoder.matches(command.getPassword(), user.getPassword())) {
            throw new ApplicationException("Invalid password");
        }

        return tokenProvider.createToken(command.getUserId());
    }
}
