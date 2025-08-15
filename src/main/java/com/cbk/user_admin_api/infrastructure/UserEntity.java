package com.cbk.user_admin_api.infrastructure;

import com.cbk.user_admin_api.domain.Address;
import com.cbk.user_admin_api.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users",
        indexes = {@Index(name = "idx_age_group", columnList = "age_group")},
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_user_id", columnNames = {"user_id"}),
                @UniqueConstraint(name = "uk_ssn", columnNames = {"ssn"})
        })
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id", nullable = false, unique = true)
    private String userId;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "ssn", nullable = false, unique = true)
    private String ssn;

    @Column(name = "age_group", nullable = false)
    private int ageGroup;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "top_level_region_address")
    private String topLevelRegionAddress;

    @Column(name = "remainder_address")
    private String remainderAddress;

    public static UserEntity from(User user) {
        UserEntity userEntity = new UserEntity();
        userEntity.userId = user.getUserId();
        userEntity.password = user.getPassword();
        userEntity.name = user.getName();
        userEntity.ssn = user.getSsn();
        userEntity.ageGroup = user.getAgeGroup();
        userEntity.phoneNumber = user.getPhoneNumber();

        Address address = user.getAddress();
        userEntity.topLevelRegionAddress = address.getTopLevelRegion();
        userEntity.remainderAddress = address.getRemainder();

        return userEntity;
    }

    public User toDomain() {
        return User.of(userId,
                       password,
                       name,
                       ssn,
                       ageGroup,
                       phoneNumber,
                       Address.of(topLevelRegionAddress, remainderAddress));
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateAddress(Address address) {
        this.topLevelRegionAddress = address.getTopLevelRegion();
        this.remainderAddress = address.getRemainder();
    }
}
