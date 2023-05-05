package com.example.cache.msg;

import com.example.cache.util.SpringContextUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.core.env.Environment;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 消息对象，用作Redis发布消息
 * @author PlusQi
 */
@AllArgsConstructor
@Data
public class CacheMessage implements Serializable {

    private static final long serialVersionUID = 4917027550855608520L;

    private String cacheName;

    private Object key;

    private Object value;

    /**
     * 标识缓存操作类型
     */
    private CacheMsgType cacheMsgType;

    /**
     * 主机标识，避免重复操作
     */
    private String msgSource;

    public String getMsgSource() throws UnknownHostException {
        Environment env = SpringContextUtil.getBean(Environment.class);
        String serverPort = env.getProperty("server.port");

        String host = InetAddress.getLocalHost().getHostAddress();

        return host + ":" + serverPort;

    }

    public enum CacheMsgType {

        // 更新操作
        UPDATE,

        // 删除操作
        DELETE;
    }
}
