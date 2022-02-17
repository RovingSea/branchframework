package org.branchframework.rpc.server.transmission;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import org.branchframework.rpc.core.codec.ProtocolFrameDecoder;
import org.branchframework.rpc.core.codec.SharableMessageCodec;
import org.branchframework.rpc.server.handler.RpcRequestMessageHandler;

import java.net.InetAddress;

/**
 * @author Haixin Wu
 * @since 1.0
 */
@Slf4j
public class NettyStartRpcServer implements StartRpcServer {

    @Override
    public void start(int port) {
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();
        ProtocolFrameDecoder protocolFrameDecoder = new ProtocolFrameDecoder();
        LoggingHandler loggingHandler = new LoggingHandler(LogLevel.DEBUG);
        SharableMessageCodec messageCodec = new SharableMessageCodec();
        RpcRequestMessageHandler rpcRequestMessageHandler = new RpcRequestMessageHandler();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.group(boss, worker);
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast(protocolFrameDecoder);
                    socketChannel.pipeline().addLast(loggingHandler);
                    socketChannel.pipeline().addLast(messageCodec);
                    socketChannel.pipeline().addLast(rpcRequestMessageHandler);
                }
            });
            String serverAddress = InetAddress.getLocalHost().getHostAddress();
            Channel channel = serverBootstrap.bind(port).sync().channel();
            log.info("Netty 在 ip 地址为{}，端口为{}上启动", serverAddress, port);
            channel.closeFuture().sync();
        } catch (Exception e) {
            log.error("Netty 启动服务失败", e);
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}

