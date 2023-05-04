package com.example.cache.config;

import com.example.cache.cache.impl.DoubleCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 配置自定义的DoubleCacheManager作为默认的缓存管理器
 * @author PlusQi
 */
@Configuration
public class CacheConfig {
    @Autowired
    DoubleCacheConfig doubleCacheConfig;

    @Bean
    public DoubleCacheManager cacheManager(RedisTemplate<Object, Object> redisTemplate, DoubleCacheConfig doubleCacheConfig) {
        return new DoubleCacheManager(redisTemplate, doubleCacheConfig);
    }
}
