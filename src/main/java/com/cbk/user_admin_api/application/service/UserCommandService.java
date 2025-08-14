package com.cbk.user_admin_api.application.service;

import com.cbk.user_admin_api.application.command.UserSignupCommand;
import com.cbk.user_admin_api.application.command.UserUpdateCommand;
import com.cbk.user_admin_api.domain.User;
import com.cbk.user_admin_api.domain.UserCommandRepository;
import com.cbk.user_admin_api.domain.UserQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserCommandService {
    private final UserCommandRepository userCommandRepository;
    private final UserQueryRepository userQueryRepository;

    public void signup(UserSignupCommand command) {
        userCommandRepository.create(command.toUser());
    }

    public void update(UserUpdateCommand command) {
        Optional<User> userOpt = userQueryRepository.findByUserId(command.getUserId());

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.updatePassword(command.getPassword());
            user.updateAddress(command.getAddress());
            userCommandRepository.update(user);
        }
    }

    public void delete(String userId) {
        userCommandRepository.deleteByUserId(userId);
    }
}
