package com.cbk.user_admin_api.application.command;

import com.cbk.user_admin_api.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(staticName = "of")
@Getter
public class UserSignupCommand {
    private String userId;
    private String password;
    private String name;
    private String ssn;
    private String phoneNumber;
    private String address;

    public User toUser() {
        return User.create(userId, password, name, ssn, phoneNumber, address);
    }
}
