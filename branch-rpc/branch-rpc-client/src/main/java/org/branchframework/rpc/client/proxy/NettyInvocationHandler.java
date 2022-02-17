package org.branchframework.rpc.client.proxy;


import io.netty.channel.Channel;
import lombok.Data;
import org.branchframework.rpc.core.protocol.message.RpcRequestMessage;
import org.branchframework.rpc.core.protocol.message.SequenceIdGenerator;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author Haixin Wu
 * @since 1.0
 */
@Data
public abstract class NettyInvocationHandler implements InvocationHandler {

    private final String targetClassName;

    private final Channel channel;

    private final String methodVersion;

    public NettyInvocationHandler(String targetClassName, Channel channel, String methodVersion) {
        this.targetClassName = targetClassName;
        this.channel = channel;
        this.methodVersion = methodVersion;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        RpcRequestMessage message = new RpcRequestMessage(
                SequenceIdGenerator.nextId(),
                targetClassName,
                method.getName(),
                method.getReturnType(),
                method.getParameterTypes(),
                args,
                methodVersion);
        return sendMessage(message);
    }

    public abstract Object sendMessage(RpcRequestMessage message);
}

