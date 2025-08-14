package com.cbk.user_admin_api.application.service;

import com.cbk.user_admin_api.application.command.UserSignupCommand;
import com.cbk.user_admin_api.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserSignupService {
    private final UserRepository userRepository;

    public void signup(UserSignupCommand command) {
        userRepository.create(command.toUser());
    }
}
