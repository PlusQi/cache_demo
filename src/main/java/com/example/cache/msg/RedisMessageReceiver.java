package com.example.cache.msg;

import com.example.cache.cache.DoubleCache;
import com.example.cache.cache.impl.DoubleCacheManager;
import com.example.cache.util.CommonUtil;
import com.example.cache.util.SpringContextUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

/**
 * 接受并消费消息
 * @author PlusQi
 */
@Slf4j
@AllArgsConstructor
@Component
public class RedisMessageReceiver {
    private final RedisTemplate<Object, Object> redisTemplate;
    private final DoubleCacheManager doubleCacheManager;

    /**
     * 接受并处理消息
     * @param message 接受的消息
     * @throws UnknownHostException
     */
    public void receiveAndHandle(String message) throws UnknownHostException {
        CacheMessage msg = (CacheMessage) redisTemplate.getValueSerializer().deserialize(message.getBytes(StandardCharsets.UTF_8));
        log.debug("已接受消息:{}", msg);

        // 不处理本机消息
        if (CommonUtil.getLocalAddress().equals(msg.getMsgSource())) {
            log.debug("收到本机消息：{}，不做处理", msg);
            return;
        }

        DoubleCache doubleCache = (DoubleCache) doubleCacheManager.getCache(msg.getCacheName());
        if (CacheMessage.CacheMsgType.UPDATE.equals(msg.getCacheMsgType())) {
            // 更新本地缓存
            log.debug("已更新CaffeineCache");
            return;
        }

        if (CacheMessage.CacheMsgType.DELETE.equals(msg.getCacheMsgType())) {
            // 删除本地缓存
            log.debug("已删除CaffeineCache");
            return;
        }

    }
}
