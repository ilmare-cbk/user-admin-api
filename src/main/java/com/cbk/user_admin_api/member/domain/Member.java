package com.cbk.user_admin_api.member.domain;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
public class Member {

    @EqualsAndHashCode.Include
    private String memberId;
    private String password;
    private String name;
    private String ssn;
    private String phoneNumber;
    private String address;

    public static Member create(@NotNull String memberId,
                                @NotNull String password,
                                @NotNull String name,
                                @NotNull String ssn,
                                @NotNull String phoneNumber,
                                @NotNull String address) {
        Member member = new Member();
        member.memberId = memberId;
        member.password = password;
        member.name = name;
        member.ssn = ssn;
        member.phoneNumber = phoneNumber;
        member.address = address;
        return member;
    }
}
