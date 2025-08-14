package com.cbk.user_admin_api.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
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

    @ParameterizedTest
    @ValueSource(strings = {"경기도 수원시", "부산광역시 해운대구", "서울 강남구"})
    @DisplayName("updateAddress: 정상 문자열 입력 시 주소가 변경된다")
    void updateAddress_shouldChangeAddress_whenValid(String newAddress) {
        User user = User.create("user1", "oldPass", "홍길동", "900101-1234567", "01012345678", "서울특별시");

        user.updateAddress(newAddress);

        assertEquals(newAddress, user.getAddress());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @DisplayName("updateAddress: 공백 또는 null 입력 시 주소는 변경되지 않는다")
    void updateAddress_shouldNotChangeAddress_whenBlankOrNull(String newAddress) {
        User user = User.create("user1", "oldPass", "홍길동", "900101-1234567", "01012345678", "서울특별시");

        user.updateAddress(newAddress);

        assertEquals("서울특별시", user.getAddress());
    }

    @Test
    @DisplayName("비밀번호가 일치하면 true를 반환한다")
    void matchedPassword_True_WhenPasswordMatches() {
        // given
        User user = User.create(
                "user01",
                "secret123",
                "홍길동",
                "9001011234567",
                "01012345678",
                "서울특별시 종로구"
        );

        // when
        boolean result = user.matchedPassword("secret123");

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("비밀번호가 불일치하면 false를 반환한다")
    void matchedPassword_False_WhenPasswordDoesNotMatch() {
        // given
        User user = User.create(
                "user01",
                "secret123",
                "홍길동",
                "9001011234567",
                "01012345678",
                "서울특별시 종로구"
        );

        // when
        boolean result = user.matchedPassword("wrongPass");

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("입력 비밀번호가 null이면 false를 반환한다")
    void matchedPassword_False_WhenPasswordIsNull() {
        // given
        User user = User.create(
                "user01",
                "secret123",
                "홍길동",
                "9001011234567",
                "01012345678",
                "서울특별시 종로구"
        );

        // when
        boolean result = user.matchedPassword(null);

        // then
        assertThat(result).isFalse();
    }
}