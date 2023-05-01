package com.example.cache.service;

import com.example.cache.common.DoubleCache;
import org.springframework.cache.Cache;

import java.util.Collection;


/**
 * @author PlusQi
 */
public interface CacheManager {
    /**
     * 根据cacheName获取缓存实例，不存在时则创建
     * @param cacheName
     * @return
     */
    Cache getCache(String cacheName);

    /**
     * 返回所有的cacheName
     * @return
     */
    Collection<String> getCacheNames();

}
