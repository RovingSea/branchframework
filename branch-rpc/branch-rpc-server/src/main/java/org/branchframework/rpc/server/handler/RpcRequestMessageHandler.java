package org.branchframework.rpc.server.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.branchframework.rpc.core.cache.LocalServerCache;
import org.branchframework.rpc.core.protocol.message.RpcRequestMessage;
import org.branchframework.rpc.core.protocol.message.RpcResponseMessage;
import org.branchframework.rpc.core.registry.util.RegistryServiceUtil;

import java.lang.reflect.Method;

/**
 * @author Haixin Wu
 * @since 1.0
 */
@Slf4j
@ChannelHandler.Sharable
public class RpcRequestMessageHandler extends SimpleChannelInboundHandler<RpcRequestMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcRequestMessage rpcRequestMessage) throws Exception {
        RpcResponseMessage rpcResponseMessage = new RpcResponseMessage();
        rpcResponseMessage.setSequenceId(rpcRequestMessage.getSequenceId());
        String interfaceName = rpcRequestMessage.getInterfaceName();
        String methodVersion = rpcRequestMessage.getMethodVersion();
        try {
            Object perform = LocalServerCache.get(RegistryServiceUtil.bindNameAndVersion(interfaceName, methodVersion));
            if (perform == null) {
                throw new RuntimeException("注册中心不存在服务节点：" + interfaceName);
            }
            Method method = perform.getClass().getMethod(rpcRequestMessage.getMethodName(), rpcRequestMessage.getParameterTypes());
            Object returnValue = method.invoke(perform, rpcRequestMessage.getParameterValue());
            rpcResponseMessage.setReturnValue(returnValue);
        } catch (Exception e) {
            e.printStackTrace();
            String exceptionMessage = e.getCause().getMessage();
            rpcResponseMessage.setExceptionValue(new Exception("远程调用出错：" + exceptionMessage));
        } finally {
            channelHandlerContext.writeAndFlush(rpcResponseMessage);
        }
    }
}