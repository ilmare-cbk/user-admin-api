package com.cbk.user_admin_api.domain;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
public class User {

    @EqualsAndHashCode.Include
    private String userId;
    private String password;
    private String name;
    private String ssn;
    private String phoneNumber;
    private String address;

    public static User create(@NotNull String userId,
                              @NotNull String password,
                              @NotNull String name,
                              @NotNull String ssn,
                              @NotNull String phoneNumber,
                              @NotNull String address) {
        User user = new User();
        user.userId = userId;
        user.password = password;
        user.name = name;
        user.ssn = ssn;
        user.phoneNumber = phoneNumber;
        user.address = address;
        return user;
    }

    public void updatePassword(String password) {
        if (StringUtils.hasText(password)) {
            this.password = password;
        }
    }

    public void updateAddress(String address) {
        if (StringUtils.hasText(address)) {
            this.address = address;
        }
    }
}
