package org.branchframework.rpc.client.transmission;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.branchframework.rpc.client.proxy.NettyAsyncInvocationHandler;
import org.branchframework.rpc.client.proxy.NettySyncInvocationHandler;
import org.branchframework.rpc.core.discovery.DiscoveryService;
import org.branchframework.rpc.core.registry.RegistryServiceNode;
import org.branchframework.rpc.core.registry.util.RegistryServiceUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * Rpc通讯程序应用
 *
 * @author Haixin Wu
 * @since 1.0
 */
@Slf4j
public class NettyRpcClientManager implements StartRpcClient {

    private static String serverAddress = null;

    private static int port = -1;

    private static String methodVersion = null;

    private Object target;


    public <T> T setTarget(Class<T> target, DiscoveryService discoveryService, String version, boolean sync) throws Exception {
        this.target = target;
        InvocationHandler invocationHandler = null;
        RegistryServiceNode serviceNode = null;
        ClassLoader classLoader = null;
        Class<?>[] interfaces = null;
        String serviceNodeNameAndVersion = RegistryServiceUtil.bindNameAndVersion(target.getName(), version);

        serviceNode = discoveryService.discovery(serviceNodeNameAndVersion);
        if (serviceNode == null) {
            throw new RuntimeException("没有找到" + serviceNodeNameAndVersion + "这个服务节点");
        }
        serverAddress = serviceNode.getAddress();
        port = serviceNode.getPort();
        methodVersion = serviceNode.getVersion();

        classLoader = target.getClassLoader();
        interfaces = new Class[]{target};
        invocationHandler = getRpcWay(target.getName(), getChannel(), methodVersion, sync);
        Object o = Proxy.newProxyInstance(classLoader, interfaces, invocationHandler);
        return (T) o;
    }

    // 同步和异步调用方式
    private InvocationHandler getRpcWay(String targetClassName, Channel channel, String methodVersion, boolean sync) {
        if (sync) {
            return new NettySyncInvocationHandler(targetClassName, channel, methodVersion);
        } else {
            return new NettyAsyncInvocationHandler(targetClassName, channel, methodVersion);
        }
    }

    @Override
    public boolean establishCommunication() {
        return HolderChannel.CHANNEL != null;
    }

    /**
     * IoDH 单例模式，确保连接建立后使用单例的 Channel 对象
     */
    public static Channel getChannel() {
        return HolderChannel.CHANNEL;
    }

    public static class HolderChannel {

        private final static Channel CHANNEL = new RpcChannel(serverAddress, port).getChannel();

    }

}

