package org.branchframework.rpc.server;

import org.branchframework.rpc.server.transmission.NettyStartRpcServer;

/**
 * @author Haixin Wu
 * @since 1.0
 */
public class ServerTest {
    public static void main(String[] args) {
        NettyStartRpcServer nettyStartRpcServer = new NettyStartRpcServer();
        nettyStartRpcServer.start(8080);
    }
}

