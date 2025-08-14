package com.cbk.user_admin_api.application.service;

import com.cbk.user_admin_api.application.command.UserSignupCommand;
import com.cbk.user_admin_api.domain.UserCommandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserSignupService {
    private final UserCommandRepository userCommandRepository;

    public void signup(UserSignupCommand command) {
        userCommandRepository.create(command.toUser());
    }
}
