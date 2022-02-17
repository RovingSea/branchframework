package org.branchframework.rpc.client.proxy;


import io.netty.channel.Channel;
import lombok.Data;

/**
 * @author Haixin Wu
 * @since 1.0
 */
@Data
public class NettyInvocationHandler {

    protected String targetClassName;

    protected Channel channel;

    protected String methodVersion;
}

