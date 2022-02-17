package org.branchframework.rpc.core.discovery;

import org.branchframework.rpc.core.registry.RegistryServiceNode;

/**
 * @author Haixin Wu
 * @since 1.0
 */
public interface DiscoveryService {
    /**
     * 发现服务
     * @param serviceName 服务名
     * @return 该节点
     */
    RegistryServiceNode discovery(String serviceName) throws Exception;
}

