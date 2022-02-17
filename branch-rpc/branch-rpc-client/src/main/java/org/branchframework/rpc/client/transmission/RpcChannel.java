package org.branchframework.rpc.client.transmission;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import org.branchframework.rpc.client.handler.RpcResponseMessageHandler;
import org.branchframework.rpc.core.codec.ProtocolFrameDecoder;
import org.branchframework.rpc.core.codec.SharableMessageCodec;

/**
 * @author Haixin Wu
 * @since 1.0
 */
@Slf4j
public class RpcChannel {

    private Channel channel = null;

    public RpcChannel(String serverAddress, int port){
        NioEventLoopGroup group = new NioEventLoopGroup();
        ProtocolFrameDecoder protocolFrameDecoder = new ProtocolFrameDecoder();
        LoggingHandler loggingHandler = new LoggingHandler(LogLevel.DEBUG);
        SharableMessageCodec messageCodec = new SharableMessageCodec();
        RpcResponseMessageHandler rpcResponseMessageHandler = new RpcResponseMessageHandler();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.group(group);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(protocolFrameDecoder);
                socketChannel.pipeline().addLast(loggingHandler);
                socketChannel.pipeline().addLast(messageCodec);
                socketChannel.pipeline().addLast(rpcResponseMessageHandler);
            }
        });
        try {
            channel = bootstrap.connect(serverAddress, port).sync().channel();
            channel.closeFuture().addListener(future -> {
                group.shutdownGracefully();
            });
        } catch (Exception e) {
            log.error("客户端建立连接失败", e);
        }
    }

    public Channel getChannel() {
        return channel;
    }
}

