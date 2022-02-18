package org.branchframework.rpc.core.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Haixin Wu
 * @since 1.0
 */
public class LocalServerCache {

    private static final Map<String, Object> serverCacheMap = new ConcurrentHashMap<>();

    public static void put(String serverName, Object obj) {
        // 添加缓存时，如果发现节点已存在，新节点就会覆盖老节点
        serverCacheMap.merge(serverName, obj, (Object oldObj, Object newObj) -> newObj);
    }

    public static Object get(String serverName) {
        return serverCacheMap.get(serverName);
    }
}

