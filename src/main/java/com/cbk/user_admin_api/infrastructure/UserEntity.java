package com.cbk.user_admin_api.infrastructure;

import com.cbk.user_admin_api.domain.Address;
import com.cbk.user_admin_api.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String userId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String ssn;

    @Column(nullable = false)
    private String phoneNumber;

    private String topLevelRegionAddress;
    private String remainderAddress;

    public static UserEntity from(User user) {
        UserEntity userEntity = new UserEntity();
        userEntity.userId = user.getUserId();
        userEntity.password = user.getPassword();
        userEntity.name = user.getName();
        userEntity.ssn = user.getSsn();
        userEntity.phoneNumber = user.getPhoneNumber();

        Address address = user.getAddress();
        userEntity.topLevelRegionAddress = address.getTopLevelRegion();
        userEntity.remainderAddress = address.getRemainder();

        return userEntity;
    }

    public User toDomain() {
        return User.of(userId, password, name, ssn, phoneNumber, Address.of(topLevelRegionAddress, remainderAddress));
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateAddress(Address address) {
        this.topLevelRegionAddress = address.getTopLevelRegion();
        this.remainderAddress = address.getRemainder();
    }
}
