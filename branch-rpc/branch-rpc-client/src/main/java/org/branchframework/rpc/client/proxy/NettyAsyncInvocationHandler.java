package org.branchframework.rpc.client.proxy;

import io.netty.util.concurrent.DefaultPromise;
import org.branchframework.rpc.client.handler.RpcPromises;
import org.branchframework.rpc.client.transmission.RpcChannel;
import org.branchframework.rpc.core.protocol.message.RpcRequestMessage;
import org.branchframework.rpc.core.registry.RegistryServiceNode;

/**
 * 使用 Netty 的 Future 类进行异步调用
 * @author Haixin Wu
 * @since 1.0
 */
public class NettyAsyncInvocationHandler extends NettyInvocationHandler {

    public NettyAsyncInvocationHandler(String targetClassName, RpcChannel rpcChannel, RegistryServiceNode registryServiceNode, String loadBalance) {
        super(targetClassName, rpcChannel, registryServiceNode, loadBalance);
    }

    @Override
    public Object sendMessage(RpcRequestMessage message) {
        super.getChannel().writeAndFlush(message);
        DefaultPromise<Object> promise = new DefaultPromise<>(super.getChannel().eventLoop());
        RpcPromises.PROMISES.put(message.getSequenceId(), promise);
        // 立即返回等待结果对象
        return promise;
    }

}

