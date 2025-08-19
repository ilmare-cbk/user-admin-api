package com.cbk.user_admin_api.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

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

    @ParameterizedTest
    @ValueSource(strings = {"newPass", "12345678", "Password!@#"})
    @DisplayName("updatePassword: 정상 문자열 입력 시 패스워드가 변경된다")
    void updatePassword_shouldChangePassword_whenValid(String newPassword) {
        User user = User.create("user1", "oldPass", "홍길동", "900101-1234567", "01012345678", "서울특별시");

        user.updatePassword(newPassword);

        assertEquals(newPassword, user.getPassword());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @DisplayName("updatePassword: 공백 또는 null 입력 시 패스워드는 변경되지 않는다")
    void updatePassword_shouldNotChangePassword_whenBlankOrNull(String newPassword) {
        User user = User.create("user1", "oldPass", "홍길동", "900101-1234567", "01012345678", "서울특별시");

        user.updatePassword(newPassword);

        assertEquals("oldPass", user.getPassword());
    }

    @Test
    @DisplayName("2000년대 생(2005년) -> 20대")
    void testExtractAgeGroup_2000s() {
        String ssn = "050101-1234567";
        User user = User.create(
                "user1",
                "password",
                "홍길동",
                ssn,
                "010-1234-5678",
                "서울특별시"
        );

        assertEquals(20, user.getAgeGroup());
    }

    @Test
    @DisplayName("1990년대 생(1995년) -> 30대")
    void testExtractAgeGroup_1990s() {
        String ssn = "950101-1234567";
        User user = User.create(
                "user2",
                "password",
                "김철수",
                ssn,
                "010-9876-5432",
                "부산광역시"
        );

        assertEquals(30, user.getAgeGroup());
    }

    @Test
    @DisplayName("2020년생 -> 0~9세")
    void testExtractAgeGroup_0to9() {
        String ssn = "200101-1234567";
        User user = User.create(
                "user3",
                "password",
                "이영희",
                ssn,
                "010-1111-2222",
                "대구광역시"
        );

        assertEquals(0, user.getAgeGroup());
    }
}