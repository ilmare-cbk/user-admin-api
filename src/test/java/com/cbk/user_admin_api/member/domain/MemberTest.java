package com.cbk.user_admin_api.member.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MemberTest {

    @DisplayName("동일한 memberId를 가진 두 사용자는 동등하다.")
    @Test
    void member_equality_sameMemberId_shouldBeEqual() {
        Member member1 = Member.create("M001", "password123", "홍길동", "90010112345", "01012345678", "서울시 강남구");
        Member member2 = Member.create("M001", "password456", "김철수", "90010212345", "01098765432", "서울시 서초구");

        assertEquals(member1, member2);   // memberId가 같으면 동일
        assertEquals(member1.hashCode(), member2.hashCode());
    }

    @DisplayName("다른 memberId를 가진 두 사용자는 동등하지 않다.")
    @Test
    void member_equality_differentMemberId_shouldNotBeEqual() {
        Member member1 = Member.create("M001", "password123", "홍길동", "90010112345", "01012345678", "서울시 강남구");
        Member member2 = Member.create("M002", "password123", "홍길동", "90010112345", "01012345678", "서울시 강남구");

        assertNotEquals(member1, member2);
    }
}