package com.cbk.user_admin_api.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AddressTest {

    @Test
    @DisplayName("같은 topLevelRegion과 remainder를 가진 Address는 동등성을 가진다")
    void testEquals_sameValues() {
        Address addr1 = Address.of("서울특별시", "강남구");
        Address addr2 = Address.of("서울특별시", "강남구");

        assertThat(addr1).isEqualTo(addr2);
        assertThat(addr1.hashCode()).hasSameHashCodeAs(addr2.hashCode());
    }

    @Test
    @DisplayName("다른 topLevelRegion 또는 remainder를 가진 Address는 동등성이 아니다")
    void testEquals_differentValues() {
        Address addr1 = Address.of("서울특별시", "강남구");
        Address addr2 = Address.of("경기도", "수원시");

        assertThat(addr1).isNotEqualTo(addr2);
    }

    @ParameterizedTest
    @CsvSource({
            "서울특별시 강남구, 서울특별시, 강남구",
            "경기도 수원시, 경기도, 수원시",
            "제주특별자치도 서귀포시, 제주특별자치도, 서귀포시",
            "세종특별자치시, 세종특별자치시, ''",
            "수원시, '', 수원시"
    })
    @DisplayName("create()는 topLevelRegion과 remainder를 올바르게 분리한다")
    void testCreate(String input, String expectedTop, String expectedRemainder) {
        Address addr = Address.create(input);

        assertThat(addr.getTopLevelRegion()).isEqualTo(expectedTop);
        assertThat(addr.getRemainder()).isEqualTo(expectedRemainder);
    }

    @Test
    @DisplayName("create()는 null 또는 빈 문자열 입력 시 예외를 발생시킨다")
    void testCreate_emptyAddress() {
        assertThatThrownBy(() -> Address.create(null))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Address is empty");

        assertThatThrownBy(() -> Address.create(""))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Address is empty");
    }

    @Test
    @DisplayName("getFullAddress()는 topLevelRegion과 remainder를 결합한다")
    void testGetFullAddress() {
        Address addr = Address.of("서울특별시", "강남구");
        assertThat(addr.getFullAddress()).isEqualTo("서울특별시 강남구");
    }
}