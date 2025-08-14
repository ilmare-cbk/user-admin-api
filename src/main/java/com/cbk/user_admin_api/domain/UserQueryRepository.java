package com.cbk.user_admin_api.domain;

import java.util.List;

public interface UserQueryRepository {
    List<User> findUsers(int page, int size);
}
