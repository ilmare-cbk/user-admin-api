package com.cbk.user_admin_api.member.infrastructure;

import com.cbk.user_admin_api.member.domain.Member;
import com.cbk.user_admin_api.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
@RequiredArgsConstructor
public class MemberCommandRepositoryImpl implements MemberRepository {
    private final MemberJpaRepository memberJpaRepository;

    @Override
    public void create(Member member) {
        memberJpaRepository.save(MemberEntity.from(member));
    }
}
