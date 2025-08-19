package com.cbk.user_admin_api.infrastructure.ratelimit;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.BucketConfiguration;
import lombok.experimental.UtilityClass;

import java.time.Duration;
import java.util.Map;
import java.util.function.Supplier;

@UtilityClass
public class BucketConfigFactory {
    // API별 Rate Limit 설정
    private static final Map<String, Supplier<BucketConfiguration>> CONFIG_MAP = Map.of(
            "kakao", () -> BucketConfiguration.builder()
                    .addLimit(Bandwidth.builder()
                                      .capacity(100)
                                      .refillGreedy(100, Duration.ofSeconds(60L))
                                      .build())
                    .build(),
            "sms", () -> BucketConfiguration.builder()
                    .addLimit(Bandwidth.builder()
                                      .capacity(500)
                                      .refillGreedy(500, Duration.ofSeconds(60L))
                                      .build())
                    .build()
    );

    public static Supplier<BucketConfiguration> get(String apiPath) {
        return CONFIG_MAP.getOrDefault(apiPath,
                                       () -> BucketConfiguration.builder()
                                               .addLimit(Bandwidth.builder()
                                                                 .capacity(100)
                                                                 .refillGreedy(100, Duration.ofSeconds(60L))
                                                                 .build())
                                               .build());
    }
}
