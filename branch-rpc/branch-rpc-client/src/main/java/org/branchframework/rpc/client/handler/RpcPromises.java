package org.branchframework.rpc.client.handler;

import io.netty.util.concurrent.Promise;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用来接收结果的 promise 对象
 * @author Haixin Wu
 * @since 1.0
 */
public class RpcPromises {
    public static final Map<Integer, Promise<Object>> PROMISES = new ConcurrentHashMap<>();
}

