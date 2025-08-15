package com.cbk.user_admin_api.application.service;

import com.cbk.user_admin_api.config.BucketConfigFactory;
import io.github.bucket4j.distributed.BucketProxy;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RateLimiterService {
    private final ProxyManager<String> proxyManager;

    public boolean tryConsume(String key, String configKey) {
        BucketProxy bucket = proxyManager.builder().build(key, BucketConfigFactory.get(configKey));
        return bucket.tryConsume(1);
    }
}
