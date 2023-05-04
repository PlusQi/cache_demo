package com.example.cache.cache.impl;


import com.example.cache.cache.DoubleCache;
import com.example.cache.config.DoubleCacheConfig;
import com.example.cache.cache.CacheManager;
import com.github.benmanes.caffeine.cache.Caffeine;

import org.springframework.cache.Cache;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;


/**
 * 自定义缓存管理器
 * @author PlusQi
 */
@Component
public class DoubleCacheManager implements CacheManager {
    Map<String, Cache> cacheMap = new ConcurrentHashMap<>();
    private RedisTemplate<Object, Object> redisTemplate;
    private DoubleCacheConfig doubleCacheConfig;

    public DoubleCacheManager(RedisTemplate<Object, Object> redisTemplate, DoubleCacheConfig doubleCacheConfig) {
        this.redisTemplate = redisTemplate;
        this.doubleCacheConfig = doubleCacheConfig;
    }

    @Override
    public Cache getCache(String cacheName) {
        Cache cache = cacheMap.get(cacheName);
        if (Objects.nonNull(cache)) {
            return cache;
        }

        cache = new DoubleCache(cacheName, redisTemplate, createCaffeineCache(), doubleCacheConfig);

        Cache existsCache = cacheMap.putIfAbsent(cacheName, cache);

        return existsCache == null ? cache : existsCache;
    }

    @Override
    public Collection<String> getCacheNames() {
        return cacheMap.keySet();
    }

    /**
     * 根据配置文件初始化Caffeine Cache
     * @return
     */
    private com.github.benmanes.caffeine.cache.Cache<Object, Object> createCaffeineCache(){
        Caffeine<Object, Object> caffeineBuilder = Caffeine.newBuilder();
        Optional<DoubleCacheConfig> dcConfigOpt = Optional.ofNullable(this.doubleCacheConfig);
        dcConfigOpt.map(DoubleCacheConfig::getInit)
                .ifPresent(caffeineBuilder::initialCapacity);
        dcConfigOpt.map(DoubleCacheConfig::getMax)
                .ifPresent(caffeineBuilder::maximumSize);
        dcConfigOpt.map(DoubleCacheConfig::getCaffeineExpire)
                .ifPresent(eaw->caffeineBuilder.expireAfterWrite(eaw, TimeUnit.SECONDS));
/*        dcConfigOpt.map(DoubleCacheConfig::getExpireAfterAccess)
                .ifPresent(eaa->caffeineBuilder.expireAfterAccess(eaa,TimeUnit.SECONDS));
        dcConfigOpt.map(DoubleCacheConfig::getRefreshAfterWrite)
                .ifPresent(raw->caffeineBuilder.refreshAfterWrite(raw,TimeUnit.SECONDS));*/
        return caffeineBuilder.build();
    }



}
