package com.cbk.user_admin_api.member.application.command;

import com.cbk.user_admin_api.member.domain.Member;
import lombok.AllArgsConstructor;

@AllArgsConstructor(staticName = "of")
public class MemberSignupCommand {
    private String memberId;
    private String password;
    private String name;
    private String ssn;
    private String phoneNumber;
    private String address;

    public Member toMember() {
        return Member.create(memberId, password, name, ssn, phoneNumber, address);
    }
}
