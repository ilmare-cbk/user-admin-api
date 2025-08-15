package com.cbk.user_admin_api.application.query;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(staticName = "of")
@Getter
public class UserDetail {
    private String userId;
    private String name;
    private String ssn;
    private String phoneNumber;
    private String address;
}
