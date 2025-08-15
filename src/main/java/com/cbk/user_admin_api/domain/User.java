package com.cbk.user_admin_api.domain;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
public class User {

    @EqualsAndHashCode.Include
    private String userId;
    private String password;
    private String name;
    private String ssn;
    private int ageGroup;
    private String phoneNumber;
    private Address address;

    public static User create(@NotNull String userId,
                              @NotNull String password,
                              @NotNull String name,
                              @NotNull String ssn,
                              @NotNull String phoneNumber,
                              @NotNull String address) {
        User user = new User();
        user.userId = userId;
        user.password = password;
        user.name = name;
        user.ssn = ssn;
        user.ageGroup = user.extractAgeGroup(ssn);
        user.phoneNumber = phoneNumber;
        user.address = Address.create(address);
        return user;
    }

    public static User of(@NotNull String userId,
                          @NotNull String password,
                          @NotNull String name,
                          @NotNull String ssn,
                          int ageGroup,
                          @NotNull String phoneNumber,
                          @NotNull Address address) {
        User user = new User();
        user.userId = userId;
        user.password = password;
        user.name = name;
        user.ssn = ssn;
        user.ageGroup = ageGroup;
        user.phoneNumber = phoneNumber;
        user.address = address;
        return user;
    }

    public void updatePassword(String password) {
        if (StringUtils.hasText(password)) {
            this.password = password;
        }
    }

    public void updateAddress(Address address) {
        this.address = address;
    }

    public boolean matchedPassword(String password) {
        return this.password.equals(password);
    }

    public String getFullAddress() {
        return this.address.getFullAddress();
    }

    public String getTopLevelRegionAddress() {
        return this.address.getTopLevelRegion();
    }

    private int extractAgeGroup(String ssn) {
        if (!StringUtils.hasText(ssn)) return 0;

        int yearYY = Integer.parseInt(ssn.substring(0, 2));
        int currentYear = LocalDate.now().getYear();
        int currentYY = currentYear % 100;

        int birthYear;

        // 현재 연도보다 작은 YY면 2000년대, 크면 1900년대로 판단
        if (yearYY <= currentYY) {
            birthYear = 2000 + yearYY;
        } else {
            birthYear = 1900 + yearYY;
        }

        int age = currentYear - birthYear;

        if (age < 0) return 0;
        if (age >= 100) return 100;

        return (age / 10) * 10;
    }
}
