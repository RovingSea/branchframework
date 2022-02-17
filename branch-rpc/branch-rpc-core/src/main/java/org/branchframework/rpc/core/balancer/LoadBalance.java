package org.branchframework.rpc.core.balancer;

import org.branchframework.rpc.core.registry.RegistryServiceNode;

import java.util.List;

/**
 * 负载均衡算法接口
 * @author Haixin Wu
 * @since 1.0
 */
public interface LoadBalance {
    /**
     * 从多个节点中获取一个节点
     * @param nodes 服务节点
     * @return 服务节点
     */
    RegistryServiceNode chooseOne(List<RegistryServiceNode> nodes);
}

