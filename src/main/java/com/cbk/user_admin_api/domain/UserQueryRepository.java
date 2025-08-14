package com.cbk.user_admin_api.domain;

import java.util.List;
import java.util.Optional;

public interface UserQueryRepository {
    List<User> findUsers(int page, int size);

    Optional<User> findByUserId(String userId);
}
