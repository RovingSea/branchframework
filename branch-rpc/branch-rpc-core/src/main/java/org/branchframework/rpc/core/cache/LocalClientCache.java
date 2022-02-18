package org.branchframework.rpc.core.cache;

import org.branchframework.rpc.core.registry.RegistryServiceNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Haixin Wu
 * @since 1.0
 */
public class LocalClientCache {

    public static final Map<String, List<RegistryServiceNode>> serviceNodeMap = new ConcurrentHashMap<>();

    public static void add(String serviceName, RegistryServiceNode node) {
        if (!serviceNodeMap.containsKey(serviceName)) {
            serviceNodeMap.put(serviceName, new ArrayList<>());
            serviceNodeMap.get(serviceName).add(node);
        } else {
            serviceNodeMap.get(serviceName).add(node);
        }
    }

    public static void add(String serviceName, List<RegistryServiceNode> nodes) {
        serviceNodeMap.put(serviceName, nodes);
    }
}

