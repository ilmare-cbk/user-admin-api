package com.cbk.user_admin_api.infrastructure;

import com.cbk.user_admin_api.domain.User;
import com.cbk.user_admin_api.domain.UserQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserQueryRepositoryImpl implements UserQueryRepository {
    private final UserJpaRepository userJpaRepository;

    @Override
    public List<User> findUsers(int page, int size) {
        return userJpaRepository.findAll(PageRequest.of(page, size))
                .stream()
                .map(it -> User.create(it.getUserId(),
                                       it.getPassword(),
                                       it.getName(),
                                       it.getSsn(),
                                       it.getPhoneNumber(),
                                       it.getAddress()))
                .toList();
    }
}
