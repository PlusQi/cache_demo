package com.example.cache.config;

import com.example.cache.msg.RedisMessageReceiver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

/**
 * Redis消息监听配置
 * @author PlusQi
 */
@Configuration
public class CacheMsgConfig {
    public final static String TOPIC = "cache.msg";

    /**
     * 为消息监听器提供异步行为的容器，可以提供消息转换、分派等底层功能
     * @param listenerAdapter
     * @param redisConnectionFactory
     * @return
     */
    @Bean
    RedisMessageListenerContainer listenerContainer(MessageListenerAdapter listenerAdapter,
                                                    RedisConnectionFactory redisConnectionFactory) {
        RedisMessageListenerContainer listenerContainer = new RedisMessageListenerContainer();
        listenerContainer.setConnectionFactory(redisConnectionFactory);
        listenerContainer.addMessageListener(listenerAdapter, new PatternTopic(TOPIC));
        return listenerContainer;
    }

    /**
     * 消息监听适配器，可以指定自定义的监听代理类，可以自定义监听逻辑
     * @param redisMessageReceiver
     * @return
     */
    @Bean
    MessageListenerAdapter listenerAdapter(RedisMessageReceiver redisMessageReceiver) {
        return new MessageListenerAdapter(redisMessageReceiver, "redisMessageReceiver");
    }
}
