package com.example.cache.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 *
 * @author PlusQi
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "doublecache")
public class DoubleCacheConfig {

    public final static Boolean CACHE_ALLOWNULL = true; // 默认允许存储null值
    private final static Integer CACHE_CAPACITY_INIT = 10; // 默认初始缓存容量
    private final static Integer CACHE_CAPACITY_MAX = 50; // 默认最大缓存容量
    private final static Long CACHE_CAFFEINE_EXPIRE = 10L; // 默认caffeineCache过期时间
    private final static Long CACHE_REDIS_EXPIRE = 20L; // 默认redisCache过期时间

    /**
     * 是否允许存储null值
     */
    private Boolean allowNull = CACHE_ALLOWNULL;

    /**
     * 初始缓存容量
     */
    private Integer init = CACHE_CAPACITY_INIT;

    /**
     * 最大缓存容量
     */
    private Integer max = CACHE_CAPACITY_MAX;

    /**
     * caffeineExpire 缓存过期时间,默认为10秒
     */
    private Long caffeineExpire = CACHE_CAFFEINE_EXPIRE;

    private Long expireAfterAccess;

    private Long refreshAfterWrite;

    /**
     * redis 缓存过期时间，默认为20秒
     */
    private Long redisExpire = CACHE_REDIS_EXPIRE;
}
