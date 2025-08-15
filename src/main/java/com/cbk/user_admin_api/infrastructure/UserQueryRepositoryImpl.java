package com.cbk.user_admin_api.infrastructure;

import com.cbk.user_admin_api.domain.User;
import com.cbk.user_admin_api.domain.UserQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserQueryRepositoryImpl implements UserQueryRepository {
    private final UserJpaRepository userJpaRepository;

    @Override
    public List<User> findUsers(int page, int size) {
        return userJpaRepository.findAll(PageRequest.of(page, size))
                .stream()
                .map(UserEntity::toDomain)
                .toList();
    }

    @Override
    public Optional<User> findByUserId(String userId) {
        return userJpaRepository.findByUserId(userId)
                .map(UserEntity::toDomain);
    }

    @Override
    public List<User> findAllByAgeGroup(int ageGroup) {
        return userJpaRepository.findByAgeGroup(ageGroup)
                .stream()
                .map(UserEntity::toDomain)
                .toList();
    }
}
