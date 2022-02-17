package org.branchframework.rpc.client.proxy;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 本地代理池，起缓存作用，每当客户端生成一个代理对象时，便会注册到这儿来
 * @author Haixin Wu
 * @since 1.0
 */
public class LocalRpcProxyPool {

    private static final Map<String, Object> proxies = new ConcurrentHashMap<>();

    public static Map<String, Object> getProxies() {
        return proxies;
    }

}

