package com.cbk.user_admin_api.member.application.service;

import com.cbk.user_admin_api.member.application.command.MemberSignupCommand;
import com.cbk.user_admin_api.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberSignupService {
    private final MemberRepository memberRepository;

    public void signup(MemberSignupCommand command) {
        memberRepository.create(command.toMember());
    }
}
