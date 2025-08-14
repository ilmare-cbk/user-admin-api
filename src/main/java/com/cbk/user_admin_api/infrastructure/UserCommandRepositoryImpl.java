package com.cbk.user_admin_api.infrastructure;

import com.cbk.user_admin_api.domain.User;
import com.cbk.user_admin_api.domain.UserCommandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
@RequiredArgsConstructor
public class UserCommandRepositoryImpl implements UserCommandRepository {
    private final UserJpaRepository userJpaRepository;

    @Override
    public void create(User user) {
        userJpaRepository.save(UserEntity.from(user));
    }
}
