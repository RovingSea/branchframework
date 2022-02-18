package org.branchframework.rpc.client.proxy;


import io.netty.channel.Channel;
import lombok.Data;
import org.branchframework.rpc.client.transmission.RpcChannel;
import org.branchframework.rpc.core.balancer.LoadBalance;
import org.branchframework.rpc.core.cache.LocalClientCache;
import org.branchframework.rpc.core.context.BranchRpcClientContext;
import org.branchframework.rpc.core.protocol.message.RpcRequestMessage;
import org.branchframework.rpc.core.protocol.message.SequenceIdGenerator;
import org.branchframework.rpc.core.registry.RegistryServiceNode;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author Haixin Wu
 * @since 1.0
 */
@Data
public abstract class NettyInvocationHandler implements InvocationHandler {

    private final String targetClassName;

    private final RpcChannel rpcChannel;

    private final RegistryServiceNode registryServiceNode;

    private final String loadBalance;

    private Channel channel;

    public NettyInvocationHandler(String targetClassName, RpcChannel rpcChannel, RegistryServiceNode registryServiceNode, String loadBalance) {
        this.targetClassName = targetClassName;
        this.rpcChannel = rpcChannel;
        this.registryServiceNode = registryServiceNode;
        this.loadBalance = loadBalance;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws IOException {
        RpcRequestMessage message = new RpcRequestMessage(
                SequenceIdGenerator.nextId(),
                targetClassName,
                method.getName(),
                method.getReturnType(),
                method.getParameterTypes(),
                args,
                registryServiceNode.getVersion());
        // 选择负载均衡算法
        List<RegistryServiceNode> nodes = LocalClientCache.serviceNodeMap.get(registryServiceNode.getServiceName());
        LoadBalance loadBalance = BranchRpcClientContext.getLoadBalance(this.loadBalance);
        RegistryServiceNode serviceNode = loadBalance.chooseOne(nodes);
        channel = rpcChannel.getChannel(serviceNode.getAddress() + ":" +serviceNode.getPort());
        return sendMessage(message);
    }

    public abstract Object sendMessage(RpcRequestMessage message);
}

