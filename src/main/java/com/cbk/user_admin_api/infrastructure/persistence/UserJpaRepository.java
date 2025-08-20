package com.cbk.user_admin_api.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUserId(String userId);

    List<UserEntity> findByAgeGroup(int ageGroup);

    boolean existsByUserId(String userId);

    boolean existsBySsn(String ssn);
}
