package com.cbk.user_admin_api.infrastructure.persistence;

import com.cbk.user_admin_api.domain.User;
import com.cbk.user_admin_api.domain.UserCommandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
@RequiredArgsConstructor
public class UserCommandRepositoryImpl implements UserCommandRepository {
    private final UserJpaRepository userJpaRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void create(User user) {
        userJpaRepository.save(UserEntity.of(user, passwordEncoder.encode(user.getPassword())));
    }

    @Override
    public void update(User user) {
        userJpaRepository.findByUserId(user.getUserId())
                .ifPresent(u -> {
                    if (!user.getPassword().equals(u.getPassword())) {
                        u.updatePassword(passwordEncoder.encode(user.getPassword()));
                    }
                    u.updateAddress(user.getAddress());
                });
    }

    @Override
    public void deleteByUserId(String userId) {
        userJpaRepository.findByUserId(userId)
                .ifPresent(userJpaRepository::delete);
    }
}
