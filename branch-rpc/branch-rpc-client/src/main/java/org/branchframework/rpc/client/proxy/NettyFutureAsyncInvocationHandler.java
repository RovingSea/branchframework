package org.branchframework.rpc.client.proxy;

import io.netty.channel.Channel;
import io.netty.util.concurrent.DefaultPromise;
import org.branchframework.rpc.client.handler.RpcPromises;
import org.branchframework.rpc.core.protocol.message.RpcRequestMessage;
import org.branchframework.rpc.core.protocol.message.SequenceIdGenerator;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 使用 Netty 的 Future 类进行异步调用
 * @author Haixin Wu
 * @since 1.0
 */
public class NettyFutureAsyncInvocationHandler extends NettyInvocationHandler implements InvocationHandler {

    public NettyFutureAsyncInvocationHandler(String targetClassName, Channel channel, String methodVersion) {
        this.targetClassName = targetClassName;
        this.channel = channel;
        this.methodVersion = methodVersion;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        int sequenceId = SequenceIdGenerator.nextId();
        RpcRequestMessage message = new RpcRequestMessage(
                sequenceId,
                getTargetClassName(),
                method.getName(),
                method.getReturnType(),
                method.getParameterTypes(),
                args,
                getMethodVersion());
        return sendMessage(message);
    }

    public Object sendMessage(RpcRequestMessage message) {
        getChannel().writeAndFlush(message);
        DefaultPromise<Object> promise = new DefaultPromise<>(getChannel().eventLoop());
        RpcPromises.PROMISES.put(message.getSequenceId(), promise);

        try {
            promise.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (promise.isSuccess()) {
            return promise.getNow();
        } else {
            throw new RuntimeException(promise.cause());
        }
    }

}

