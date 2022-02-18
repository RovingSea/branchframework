package org.branchframework.rpc.core.balancer;

import org.branchframework.rpc.core.registry.RegistryServiceNode;

import java.util.List;
import java.util.Random;

/**
 * @author Haixin Wu
 * @since 1.0
 */
public class RandomLoadBalance implements LoadBalance {
    @Override
    public RegistryServiceNode chooseOne(List<RegistryServiceNode> nodes) {
        return nodes.get(new Random().nextInt(nodes.size()));
    }
}

