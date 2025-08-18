package com.cbk.user_admin_api.infrastructure.ratelimit;

import com.cbk.user_admin_api.config.BucketConfigFactory;
import io.github.bucket4j.distributed.BucketProxy;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RateLimiter {
    private final ProxyManager<String> proxyManager;

    public boolean tryConsume(String key, String configKey) {
        BucketProxy bucket = proxyManager.builder().build(key, BucketConfigFactory.get(configKey));
        return bucket.tryConsume(1);
    }
}
