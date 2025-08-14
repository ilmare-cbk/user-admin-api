package com.cbk.user_admin_api.member.infrastructure;

import com.cbk.user_admin_api.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String memberId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String ssn;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String address;

    public static MemberEntity from(Member member) {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.memberId = member.getMemberId();
        memberEntity.password = member.getPassword();
        memberEntity.name = member.getName();
        memberEntity.ssn = member.getSsn();
        memberEntity.phoneNumber = member.getPhoneNumber();
        memberEntity.address = member.getAddress();
        return memberEntity;
    }
}
