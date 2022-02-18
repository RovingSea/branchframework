package org.branchframework.rpc.core.balancer;

import org.branchframework.rpc.core.registry.RegistryServiceNode;

import java.util.List;

/**
 * @author Haixin Wu
 * @since 1.0
 */
public class RoundRobinLoadBalance implements LoadBalance {

    private int index;

    @Override
    public synchronized RegistryServiceNode chooseOne(List<RegistryServiceNode> nodes) {
        // 加锁，防止多线程访问，避免共用服务对象及 index 超出 nodes.size()
        if (index >= nodes.size()) {
            index = 0;
        }
        return nodes.get(index++);
    }
}

