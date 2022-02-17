package org.branchframework.rpc.client.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.Promise;
import lombok.extern.slf4j.Slf4j;
import org.branchframework.rpc.core.protocol.message.RpcResponseMessage;

/**
 * @author Haixin Wu
 * @since 1.0
 */
@Slf4j
@ChannelHandler.Sharable
public class RpcResponseMessageHandler extends SimpleChannelInboundHandler<RpcResponseMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcResponseMessage RpcResponseMessage) throws Exception {
        log.debug("调用信息：{}", RpcResponseMessage);
        Promise<Object> promise = RpcPromises.PROMISES.remove(RpcResponseMessage.getSequenceId());
        if (promise != null) {
            Object returnValue = RpcResponseMessage.getReturnValue();
            Exception exceptionValue = RpcResponseMessage.getExceptionValue();
            if (exceptionValue != null) {
                promise.setFailure(exceptionValue);
            } else {
                promise.setSuccess(returnValue);
            }
        }
    }
}

