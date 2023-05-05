package com.example.cache.util;

import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 奇怪的工具类
 * @author PlusQi
 */
public class CommonUtil {

    /**
     * @return 本机IP:端口号
     * @throws UnknownHostException
     */
    public static String getLocalAddress() throws UnknownHostException {
        Environment env = SpringContextUtil.getBean(Environment.class);
        String serverPort = env.getProperty("server.port");
        String host = InetAddress.getLocalHost().getHostAddress();
        // 获取本机ip及端口号

        return host + ":" + serverPort;
    }

}
