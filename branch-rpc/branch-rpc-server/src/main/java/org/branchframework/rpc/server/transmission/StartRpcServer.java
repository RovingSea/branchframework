package org.branchframework.rpc.server.transmission;

/**
 * @author Haixin Wu
 * @since 1.0
 */
@FunctionalInterface
public interface StartRpcServer {
    void start(int port);
}

