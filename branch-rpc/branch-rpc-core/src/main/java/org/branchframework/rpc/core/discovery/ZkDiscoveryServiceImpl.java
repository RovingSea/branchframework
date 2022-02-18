package org.branchframework.rpc.core.discovery;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;
import org.branchframework.rpc.core.balancer.LoadBalance;
import org.branchframework.rpc.core.balancer.RoundRobinLoadBalance;
import org.branchframework.rpc.core.cache.LocalClientCache;
import org.branchframework.rpc.core.registry.RegistryServiceNode;

import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * 基于 Zookeeper 发现服务类
 * @author Haixin Wu
 * @since 1.0
 */
@Slf4j
public class ZkDiscoveryServiceImpl implements DiscoveryService {

    public static final int BaseSleepTimeMs = 1000;
    public static final int MaxRetries = 3;
    public static final String ZkBasePath = "/BranchRpc";

    private ServiceDiscovery<RegistryServiceNode> serviceDiscovery;

    private LoadBalance loadBalance;

    public ZkDiscoveryServiceImpl(String registryAddress) {
        this(registryAddress, new RoundRobinLoadBalance());
    }

    public ZkDiscoveryServiceImpl(String registryAddress, LoadBalance loadBalance) {
        this.loadBalance = loadBalance;
        try {
            CuratorFramework client = CuratorFrameworkFactory.newClient(registryAddress, new ExponentialBackoffRetry(BaseSleepTimeMs, MaxRetries));
            client.start();
            JsonInstanceSerializer<RegistryServiceNode> serializer = new JsonInstanceSerializer<>(RegistryServiceNode.class);
            this.serviceDiscovery = ServiceDiscoveryBuilder.builder(RegistryServiceNode.class)
                    .basePath(ZkBasePath)
                    .client(client)
                    .serializer(serializer)
                    .build();
            this.serviceDiscovery.start();
        } catch (Exception e) {
            log.error("发现服务功能启动失败：{}", e.getMessage());
        }
    }

    @Override
    public RegistryServiceNode discovery(String serviceName) throws Exception {
        Collection<ServiceInstance<RegistryServiceNode>> serviceInstances = serviceDiscovery.queryForInstances(serviceName);
        List<RegistryServiceNode> nodeList = serviceInstances.stream().map(ServiceInstance::getPayload).collect(Collectors.toList());
        LocalClientCache.add(serviceName, nodeList);
        return serviceInstances.isEmpty() ? null : loadBalance.chooseOne(nodeList);
    }
}

