package com.example.cache.common;

import com.example.cache.config.DoubleCacheConfig;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.cache.support.AbstractValueAdaptingCache;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 声明了缓存的各种操作，实现具体缓存操作的管理
 * @author PlusQi
 */
@Slf4j
public class DoubleCache extends AbstractValueAdaptingCache {

    private String cacheName; // 缓存key前缀
    private Cache<Object, Object> caffeineCache;
    private RedisTemplate<Object, Object> redisTemplate;
    private DoubleCacheConfig doubleCacheConfig; // 缓存配置类

    public DoubleCache(boolean allowNullValues) {
        super(allowNullValues);
    }

    public DoubleCache(String cacheName, RedisTemplate<Object, Object> redisTemplate, Cache<Object, Object> caffeineCache, DoubleCacheConfig doubleCacheConfig) {
        super(doubleCacheConfig.getAllowNull());
        this.cacheName = cacheName;
        this.redisTemplate = redisTemplate;
        this.caffeineCache = caffeineCache;
        this.doubleCacheConfig = doubleCacheConfig;
    }

    /**
     * 在缓存中实际执行查找的操作，父类的get()方法会调用这个方法
     */
    @Override
    protected Object lookup(Object key) {
        Object cacheValue = this.caffeineCache.getIfPresent(key);
        if (Objects.nonNull(cacheValue)) {
            log.info("已获取Caffeine缓存: {}", cacheValue);
            return cacheValue;
        }

        String redisKey = this.cacheName + ":" + key;
        // caffeine 缓存不存在 则查询redis，将值存入caffeineCache并返回结果
        cacheValue = this.redisTemplate.opsForValue().get(redisKey);
        if (Objects.nonNull(cacheValue)) {
            this.caffeineCache.put(key, cacheValue);
            log.info("已获取Redis缓存: {}", cacheValue);
            return cacheValue;
        }
        return cacheValue;
    }

    @Override
    public String getName() {
        return this.cacheName;
    }

    @Override
    public Object getNativeCache() {
        return this;
    }

    /**
     * 通过key获取缓存值，如果缓存不存在，会调用valueLoader的call()方法(可以采用旁路缓存策略：先查缓存，查不到则查询数据库并写入缓存)
     */
    @Override
    public <T> T get(Object key, Callable<T> callable) {
        ReentrantLock reentrantLock = new ReentrantLock();
        reentrantLock.lock();
        try {
            Object cacheValue = lookup(key);
            if (Objects.nonNull(cacheValue)) {
                return (T) cacheValue;
            }

            // 未查询到key对应的缓存
            cacheValue = callable.call();
            put(key, cacheValue);
            return (T) cacheValue;
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            reentrantLock.unlock();
        }
        return null;
    }

    /**
     * 把数据存入缓存
     * 默认支持缓存null值，可以避免缓存穿透
     */
    @Override
    public void put(Object key, Object value) {
        if (!isAllowNullValues() && Objects.isNull(value)) {
            log.error("缓存不能为空");
            return;
        }

        // caffeineCache不支持存储null，所以使用toStoreValue()包装value
        this.caffeineCache.put(key, toStoreValue(value));

        // value为null时，只需要存入caffeineCache，不需要存入Redis
        if (Objects.isNull(value)) {
            return;
        }

        String redisKey = this.cacheName + ":" + key;
        // 获取配置的redis缓存过期时间
        Optional<Long> redisExpireOpt = Optional.ofNullable(doubleCacheConfig).map(DoubleCacheConfig::getRedisExpire);

        // 存入Redis，并设置缓存过期时间(配置类已设置默认值)
        redisExpireOpt.ifPresent(redisExpire -> this.redisTemplate.opsForValue().set(redisKey, toStoreValue(value), redisExpire, TimeUnit.SECONDS));

    }

    @Override
    public ValueWrapper putIfAbsent(Object o, Object o1) {
        return null;
    }

    /**
     * 通过key删除指定缓存
     */
    @Override
    public void evict(Object key) {
        this.redisTemplate.delete(this.cacheName + ":" + key);
        caffeineCache.invalidate(key);
    }

    /**
     * 删除所有缓存
     */
    @Override
    public void clear() {
        Set<Object> keys = redisTemplate.keys(this.cacheName.concat(":*"));
        for (Object key : keys) {
            this.redisTemplate.delete(String.valueOf(key));
        }

        caffeineCache.invalidateAll();
    }
}
