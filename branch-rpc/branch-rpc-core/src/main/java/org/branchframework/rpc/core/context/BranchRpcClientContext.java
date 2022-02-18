package org.branchframework.rpc.core.context;

import org.branchframework.rpc.core.balancer.LoadBalance;
import org.branchframework.rpc.core.balancer.RandomLoadBalance;
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

    // TODO 更多的负载均衡算法
    private static LoadBalance roundRobinLoadBalance;
    private static LoadBalance randomLoadBalance;

    // TODO 更多的注册中心
    private static DiscoveryService zkDiscoveryServiceImpl;


    static {
        try {
            InputStream in = BranchRpcClientContext.class.getResourceAsStream("/application.properties");
            properties = new Properties();
            properties.load(in);
            roundRobinLoadBalance = new RoundRobinLoadBalance();
            randomLoadBalance = new RandomLoadBalance();
            zkDiscoveryServiceImpl = new ZkDiscoveryServiceImpl(getRegistryAddress());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static String getRegistryAddress() throws IOException {
        String value = properties.getProperty("branch.rpc.client.registryAddress");
        if (value == null) {
            return "127.0.0.1:2181";
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
                    return zkDiscoveryServiceImpl;
                // TODO 更多的注册中心
                default :
                    throw new IOException("branch-rpc不支持" + s + "注册中心");
            }
        }
    }

    public static LoadBalance getLoadBalance(String loadBalance) throws IOException {
        if (loadBalance == null) {
            return roundRobinLoadBalance;
        } else {
            String s = loadBalance.toLowerCase();
            switch (s) {
                case "roundrobin" :
                    return roundRobinLoadBalance;
                case "random" :
                    return randomLoadBalance;
                // TODO 更多的负载均衡算法
                default:
                    throw new IOException("branch-rpc不支持" + s + "负载均衡算法");
            }
        }
    }
}

