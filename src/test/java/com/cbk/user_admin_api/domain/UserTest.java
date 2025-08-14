package com.cbk.user_admin_api.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @DisplayName("동일한 userId를 가진 두 사용자는 동등하다.")
    @Test
    void user_equality_sameUserId_shouldBeEqual() {
        User user1 = User.create("M001", "password123", "홍길동", "90010112345", "01012345678", "서울시 강남구");
        User user2 = User.create("M001", "password456", "김철수", "90010212345", "01098765432", "서울시 서초구");

        assertEquals(user1, user2);   // userId가 같으면 동일
        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @DisplayName("다른 userId를 가진 두 사용자는 동등하지 않다.")
    @Test
    void user_equality_differentUserId_shouldNotBeEqual() {
        User user1 = User.create("M001", "password123", "홍길동", "90010112345", "01012345678", "서울시 강남구");
        User user2 = User.create("M002", "password123", "홍길동", "90010112345", "01012345678", "서울시 강남구");

        assertNotEquals(user1, user2);
    }
}