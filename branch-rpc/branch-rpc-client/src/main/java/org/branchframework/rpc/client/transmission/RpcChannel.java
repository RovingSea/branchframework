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
import org.branchframework.rpc.core.registry.RegistryServiceNode;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Haixin Wu
 * @since 1.0
 */
@Slf4j
public class RpcChannel {

    private final Map<String, Channel> channelMap = new ConcurrentHashMap<>();

    private final NioEventLoopGroup group;

    public RpcChannel(List<RegistryServiceNode> serviceNodes){
        group = new NioEventLoopGroup();
        try {
            for (RegistryServiceNode serviceNode : serviceNodes) {
                Bootstrap bootstrap = initBootstrap();
                String address = serviceNode.getAddress();
                Integer port = serviceNode.getPort();
                Channel channel = bootstrap.connect(address, port).sync().channel();
                channel.closeFuture().addListener(future -> {
                    group.shutdownGracefully();
                });
                channelMap.put(address + ":" + port, channel);
            }
        } catch (Exception e) {
            log.error("客户端建立连接失败", e);
        }
    }

    private Bootstrap initBootstrap() {
        Bootstrap bootstrap = new Bootstrap();
        ProtocolFrameDecoder protocolFrameDecoder = new ProtocolFrameDecoder();
        LoggingHandler loggingHandler = new LoggingHandler(LogLevel.DEBUG);
        SharableMessageCodec messageCodec = new SharableMessageCodec();
        RpcResponseMessageHandler rpcResponseMessageHandler = new RpcResponseMessageHandler();
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
        return bootstrap;
    }

    public Map<String, Channel> getChannels() {
        return channelMap;
    }

    public Channel getChannel(String ipAndPort) {
        return channelMap.get(ipAndPort);
    }
}

