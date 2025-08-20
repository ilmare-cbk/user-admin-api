package com.cbk.user_admin_api.domain;

import com.cbk.user_admin_api.domain.exception.DomainException;
import lombok.*;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor(staticName = "of")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Address {
    private static final List<String> TOP_LEVEL_REGIONS = List.of("서울특별시", "부산광역시", "대구광역시", "인천광역시", "광주광역시", "대전광역시", "울산광역시", "세종특별자치시", "경기도", "강원특별자치도", "충청북도", "충청남도", "전라북도", "전라남도", "경상북도", "경상남도", "제주특별자치도");

    @EqualsAndHashCode.Include
    private String topLevelRegion;

    @EqualsAndHashCode.Include
    private String remainder;

    public static Address create(String address) {
        if (!StringUtils.hasText(address)) {
            throw new DomainException("Address is empty");
        }

        Address addr = new Address();
        addr.topLevelRegion = TOP_LEVEL_REGIONS.stream()
                .filter(address::startsWith)
                .findFirst()
                .orElse("");
        addr.remainder = address.replaceFirst(addr.topLevelRegion, "").trim();
        return addr;
    }

    public static Optional<Address> nullable(String address) {
        if (StringUtils.hasText(address)) {
            return Optional.of(create(address));
        }
        return Optional.empty();
    }

    public String getFullAddress() {
        return this.topLevelRegion.concat(" ").concat(remainder);
    }
}
