package org.branchframework.rpc.core.registry;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;

import java.io.IOException;

/**
 * 基于 Zookeeper 的注册中心服务节点操作的实现类，用于在 Zookeeper 上对服务节点进行注册、注销、更新和销毁等操作
 * @author Haixin Wu
 * @since 1.0
 */
@Slf4j
public class ZkRegistryServiceNodeOpImpl implements RegistryServiceNodeOperation {

    public static final int BaseSleepTimeMs = 1000;
    public static final int MaxRetries = 3;
    public static final String ZkBasePath = "/BranchRpc";

    private ServiceDiscovery<RegistryServiceNode> serviceDiscovery;

    public ZkRegistryServiceNodeOpImpl(String registryAddress) {
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
            log.error("服务注册功能启动失败：{}", e.getMessage());
        }
    }

    @Override
    public void register(RegistryServiceNode node) throws Exception {
        ServiceInstance<RegistryServiceNode> serviceInstance = ServiceInstance.<RegistryServiceNode>builder()
                .name(node.getServiceName())
                .address(node.getAddress())
                .port(node.getPort())
                .payload(node)
                .build();
        serviceDiscovery.registerService(serviceInstance);
    }

    @Override
    public void unRegister(RegistryServiceNode node) throws Exception {
        ServiceInstance<RegistryServiceNode> serviceInstance = ServiceInstance.<RegistryServiceNode>builder()
                .name(node.getServiceName())
                .address(node.getAddress())
                .port(node.getPort())
                .payload(node)
                .build();
        serviceDiscovery.unregisterService(serviceInstance);
    }

    @Override
    public void update(RegistryServiceNode node) throws Exception {
        ServiceInstance<RegistryServiceNode> serviceInstance = ServiceInstance.<RegistryServiceNode>builder()
                .name(node.getServiceName())
                .address(node.getAddress())
                .port(node.getPort())
                .payload(node)
                .build();
        serviceDiscovery.updateService(serviceInstance);
    }

    @Override
    public void destroy() throws IOException {
        serviceDiscovery.close();
    }
}

