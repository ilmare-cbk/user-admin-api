package com.cbk.user_admin_api.domain;

public interface UserCommandRepository {
    void create(User user);

    void update(User user);
}
