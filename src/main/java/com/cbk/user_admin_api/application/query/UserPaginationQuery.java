package com.cbk.user_admin_api.application.query;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(staticName = "of")
@Getter
public class UserPaginationQuery {
    private int page;
    private int size;

    public int getPage() {
        return page - 1;
    }
}
