package org.branchframework.rpc.core.context;

import org.branchframework.rpc.core.balancer.LoadBalance;
import org.branchframework.rpc.core.balancer.RoundRobinLoadBalance;
import org.branchframework.rpc.core.discovery.DiscoveryService;
import org.branchframework.rpc.core.discovery.ZkDiscoveryServiceImpl;
import org.branchframework.rpc.core.protocol.serialization.GsonSerializer;
import org.branchframework.rpc.core.protocol.serialization.JdkSerializer;
import org.branchframework.rpc.core.protocol.serialization.SerializationAlgorithm;
import org.branchframework.rpc.core.protocol.serialization.Serializer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Haixin Wu
 * @since 1.0
 */
public class BranchRpcClientContext {

    static Properties properties;

    static {
        try (InputStream in = BranchRpcClientContext.class.getResourceAsStream("/application.properties")) {
            properties = new Properties();
            properties.load(in);
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static String getRegistryAddress() throws IOException {
        String value = properties.getProperty("branch.rpc.client.registry.address");
        if (value == null) {
            throw new IOException("没有配置注册中心的地址");
        } else {
            return value;
        }
    }

    public static Serializer getSerializer() throws IOException {
        String value = properties.getProperty("branch.rpc.client.serialization");
        // 如果为空，默认采取JDK的序列化机制
        if (value == null) {
            return new JdkSerializer();
        } else {
            String s = value.toLowerCase();
            switch (s) {
                case "jdk":
                    return new JdkSerializer();
                case "gson":
                    return new GsonSerializer();
                default:
                    throw new IOException("没有找到匹配的序列化算法");
            }
        }
    }

    public static SerializationAlgorithm getSerializationAlgorithm() {
        String value = properties.getProperty("branch.rpc.client.serialization");
        if (value == null) {
            return SerializationAlgorithm.Java;
        } else {
            return SerializationAlgorithm.valueOf(value);
        }
    }

    public static DiscoveryService getRegistry() throws IOException {
        String value = properties.getProperty("branch.rpc.client.registry");
        if (value == null) {
            throw new IOException("没有选择配置注册中心");
        } else {
            String s = value.toLowerCase();
            switch (s) {
                case "zookeeper" :
                    return new ZkDiscoveryServiceImpl(getRegistryAddress(), getLoadBalance());
                // TODO 更多的注册中心
                default :
                    throw new IOException("branch-rpc不支持" + s + "注册中心");
            }
        }
    }

    public static LoadBalance getLoadBalance() throws IOException {
        String value = properties.getProperty("branch.rpc.client.balance");
        if (value == null) {
            return new RoundRobinLoadBalance();
        } else {
            String s = value.toLowerCase();
            switch (s) {
                case "roundrobin" :
                    return new RoundRobinLoadBalance();
                // TODO 更多的负载均衡算法
                default:
                    throw new IOException("branch-rpc不支持" + s + "负载均衡算法");
            }
        }
    }
}

